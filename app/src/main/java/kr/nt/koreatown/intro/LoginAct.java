package kr.nt.koreatown.intro;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.squareup.otto.Subscribe;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import kr.nt.koreatown.Common;
import kr.nt.koreatown.KoreaTown;
import kr.nt.koreatown.R;
import kr.nt.koreatown.bus.BusProvider;
import kr.nt.koreatown.bus.LoginEvent;
import kr.nt.koreatown.databinding.LoginactBinding;
import kr.nt.koreatown.main.MainAct;
import kr.nt.koreatown.retrofit.RetrofitAdapter;
import kr.nt.koreatown.retrofit.RetrofitUtil;
import kr.nt.koreatown.util.SharedManager;
import kr.nt.koreatown.util.Utils;
import kr.nt.koreatown.vo.MemberVO;
import kr.nt.koreatown.vo.MsgVO;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by user on 2017-04-20.
 */


public class LoginAct extends BaseLogin {

    LoginactBinding binding = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.loginact);
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String keyhash = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.d("KeyHash:", keyhash);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        BusProvider.getInstance().register(this);
        initUI();
    }

    private void initUI(){
        binding.toolbar.setTitle("로그인");
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        binding.emailLogin.setOnClickListener(signClick);
        binding.facebookLogin.setOnClickListener(signClick);
        binding.kakaoLogin.setOnClickListener(signClick);
        binding.loginBtn.setOnClickListener(signClick);

        boolean autoLogin = SharedManager.getInstance().getBoolean(this,Common.AUTO_LOGIN);
        if(autoLogin){
            String ID = SharedManager.getInstance().getString(this, Common.ID);
            String PASS = SharedManager.getInstance().getString(this, Common.PASSWORD);
            String TYPE = SharedManager.getInstance().getString(this, Common.MEMBER_TYPE);
            doLogin(ID,PASS,TYPE);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
    }

    View.OnClickListener signClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.email_login:
                    Intent intent = new Intent(LoginAct.this,SignEmailAct.class);
                    startActivity(intent);
                    break;
                case R.id.facebook_login:
                    LoginManager.getInstance().logInWithReadPermissions(LoginAct.this, Arrays.asList("public_profile", "email"));
                    break;
                case R.id.kakao_login:
                    isKakaoLogin();
                    break;
                case R.id.login_btn:
                    validation();
                    break;
            }
        }
    };

    private void validation(){
        String id = binding.inputId.getText().toString().trim();
        String password = binding.inputPw.getText().toString().trim();

        if(id.isEmpty() || id.length() == 0){
            binding.inputId.hasFocus();
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            binding.inputId.startAnimation(shake);
            Toast.makeText(this,"아이디를 입력해주세요.",Toast.LENGTH_SHORT).show();
            return;
        }

        if(password.isEmpty() || password.length() == 0){
            binding.inputPw.hasFocus();
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            binding.inputPw.startAnimation(shake);
            Toast.makeText(this,"비밀번호를 입력해주세요.",Toast.LENGTH_SHORT).show();
            return;
        }
        doLogin(id,password,Common.TYPE_EMAIL);
        //sendLogin(id,password);
    }

    private void sendLogin(String ID,String PASSWORD){
        String PUSH_KEY = "";
        String OS_VER = Utils.getOsVersion();
        String DEVICE_ID = Utils.getAndroidID(this);

        Map<String, RequestBody> params = new HashMap<String, RequestBody>();
        params.put(Common.ID, RetrofitUtil.toRequestBody(ID));
        params.put(Common.PASSWORD,RetrofitUtil.toRequestBody(PASSWORD));
        params.put(Common.PUSH_KEY,RetrofitUtil.toRequestBody(PUSH_KEY));
        params.put(Common.OS_VER,RetrofitUtil.toRequestBody(OS_VER));
        params.put(Common.DEVICE_ID,RetrofitUtil.toRequestBody(DEVICE_ID));
        params.put(Common.LAT,RetrofitUtil.toRequestBody(String.valueOf(KoreaTown.myLocation.getLatitude())));
        params.put(Common.LON,RetrofitUtil.toRequestBody(String.valueOf(KoreaTown.myLocation.getLongitude())));

        Call<MsgVO> call =  RetrofitAdapter.getInstance().doLogin(params);
        call.enqueue(new Callback<MsgVO>() {
            @Override
            public void onResponse(Call<MsgVO> call, Response<MsgVO> response) {
                MsgVO item = response.body();
                if(item != null ){
                    if(item.getResult().equals("1")){ // 성공
                        Intent intent = new Intent(LoginAct.this, MainAct.class);
                        startActivity(intent);
                        finish();
                    }else{

                    }
                }
            }

            @Override
            public void onFailure(Call<MsgVO> call, Throwable t) {

            }
        });
    }

    @Subscribe
    public void LoginComple(LoginEvent event){
        MemberVO memberVO = event.getMemberVO();
        SharedManager.getInstance().setString(LoginAct.this, Common.ID,memberVO.getMEMBER_ID());
        SharedManager.getInstance().setString(LoginAct.this, Common.PASSWORD,memberVO.getPASSWORD());
        SharedManager.getInstance().setString(LoginAct.this,Common.MEMBER_TYPE ,memberVO.getMEMBER_TYPE());
        SharedManager.getInstance().setBoolean(LoginAct.this,Common.AUTO_LOGIN,true);
        //Toast.makeText(this, "리프레쉬뷰 로그인 버스 들어옴", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(LoginAct.this, MainAct.class);
        startActivity(intent);
        finish();

    }

}

