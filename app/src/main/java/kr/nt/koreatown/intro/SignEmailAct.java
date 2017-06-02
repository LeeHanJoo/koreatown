package kr.nt.koreatown.intro;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import kr.nt.koreatown.Common;
import kr.nt.koreatown.R;
import kr.nt.koreatown.databinding.SignEmailBinding;
import kr.nt.koreatown.retrofit.RetrofitAdapter;
import kr.nt.koreatown.retrofit.RetrofitUtil;
import kr.nt.koreatown.util.Utils;
import kr.nt.koreatown.vo.MsgVO;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by user on 2017-05-08.
 */

public class SignEmailAct extends BaseLogin{

    SignEmailBinding binding = null;
    String mSex = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.sign_email);
        binding.toolbar.setTitle("이메일가입");
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        init();


    }

    private void init(){
        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validation();
            }
        });


        binding.sexLayout.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch(checkedId){
                    case R.id.radio_1:
                        mSex = "M";
                        break;
                    case R.id.radio_2:
                        mSex = "W";
                        break;
                }
            }
        });

    }

    private void validation(){
        String id = binding.inputId.getText().toString().trim();
        String emailAddr = binding.inputEmail.getText().toString().trim();
        String nickName = binding.inputNick.getText().toString().trim();
        String password = binding.inputPw.getText().toString().trim();
        String password2 = binding.inputPw2.getText().toString().trim();
        String birth = binding.inputBirth.getText().toString().trim();

        if(id.isEmpty() || id.length() == 0){
            binding.inputId.hasFocus();
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            binding.inputId.startAnimation(shake);
            Toast.makeText(this,"아이디를 입력해주세요.",Toast.LENGTH_SHORT).show();
            return;
        }

        if(emailAddr.isEmpty() || emailAddr.length() == 0){
            binding.inputEmail.hasFocus();
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            binding.inputEmail.startAnimation(shake);
            Toast.makeText(this,"이메일주소를 입력해주세요.",Toast.LENGTH_SHORT).show();
            return;
        }

        if(!Utils.validEmail(emailAddr)){
            binding.inputEmail.hasFocus();
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            binding.inputEmail.startAnimation(shake);
            Toast.makeText(this,"이메일주소를 정확히 입력해주세요.",Toast.LENGTH_SHORT).show();
            return;
        }

        if(nickName.isEmpty() || nickName.length() == 0){
            binding.inputNick.hasFocus();
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            binding.inputNick.startAnimation(shake);
            Toast.makeText(this,"닉네임을 입력해주세요.",Toast.LENGTH_SHORT).show();
            return;
        }

        if(password.isEmpty() || password.length() == 0){
            binding.inputPw.hasFocus();
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            binding.inputPw.startAnimation(shake);
            Toast.makeText(this,"비밀번호를 입력해주세요.",Toast.LENGTH_SHORT).show();
            return;
        }

        if(password.length() < 6){
            binding.inputPw.hasFocus();
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            binding.inputPw.startAnimation(shake);
            Toast.makeText(this,"비밀번호는 6자리이상 입력하셔야합니다",Toast.LENGTH_SHORT).show();
            return;
        }

        if(!Utils.Passwrodvalidate(password)){
            binding.inputPw.hasFocus();
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            binding.inputPw.startAnimation(shake);
            Toast.makeText(this,"비밀번호는 6자리이상 15이하 영문,숫자,특수문자를 포함하여야합니다.",Toast.LENGTH_SHORT).show();
            return;
        }

        if(password2.isEmpty() || password2.length() == 0){
            binding.inputPw2.hasFocus();
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            binding.inputPw2.startAnimation(shake);
            Toast.makeText(this,"비밀번호를 한번더 입력해주세요.",Toast.LENGTH_SHORT).show();
            return;
        }

        if(password2.length() < 6){
            binding.inputPw2.hasFocus();
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            binding.inputPw2.startAnimation(shake);
            Toast.makeText(this,"비밀번호는 6자리이상 입력하셔야합니다",Toast.LENGTH_SHORT).show();
            return;
        }

        if(!Utils.Passwrodvalidate(password2)){
            binding.inputPw2.hasFocus();
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            binding.inputPw2.startAnimation(shake);
            Toast.makeText(this,"비밀번호는 6자리이상 15이하 영문,숫자,특수문자를 포함하여야합니다.",Toast.LENGTH_SHORT).show();
            return;
        }

        if(!password.equals(password2)){
            binding.inputPw2.hasFocus();
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            binding.inputPw2.startAnimation(shake);
            Toast.makeText(this,"비밀번호가 다릅니다. 정확히 입력해주세요.",Toast.LENGTH_SHORT).show();
            return;
        }

        if(birth.isEmpty() || birth.length() == 0){
            binding.inputBirth.hasFocus();
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            binding.inputBirth.startAnimation(shake);
            Toast.makeText(this,"생년월일을 입력해주세요.",Toast.LENGTH_SHORT).show();
            return;
        }

        if(birth.length() < 8){
            binding.inputBirth.hasFocus();
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            binding.inputBirth.startAnimation(shake);
            Toast.makeText(this,"생년월일을 정확히 입력해주세요.",Toast.LENGTH_SHORT).show();
            return;
        }

        if(mSex.isEmpty() || mSex.length() == 0){
            Toast.makeText(this,"성별을 입력해주세요.",Toast.LENGTH_SHORT).show();
            return;
        }

        if(!binding.termsCheck.isChecked()){
            Toast.makeText(this,"약관 동의를 체크해주세요.",Toast.LENGTH_SHORT).show();
            return;
        }

        sendSign(id,emailAddr,password,nickName,mSex,birth);

    }


    private void sendSign(final String ID,String EMAIL,final String PASSWORD , String NICK_NAME,String SEX,String BIRTHDAY){
        String PUSH_KEY = "";
        String OS_VER = Utils.getOsVersion();
        String DEVICE_ID = Utils.getAndroidID(this);

        Map<String, RequestBody> params = new HashMap<String, RequestBody>();
        params.put(Common.ID, RetrofitUtil.toRequestBody(ID));
        params.put(Common.EMAIL,RetrofitUtil.toRequestBody(EMAIL));
        params.put(Common.PASSWORD,RetrofitUtil.toRequestBody(PASSWORD));
        params.put(Common.BIRTHDAY,RetrofitUtil.toRequestBody(BIRTHDAY));

        params.put(Common.NICK_NAME,RetrofitUtil.toRequestBody(NICK_NAME));
        params.put(Common.SEX,RetrofitUtil.toRequestBody(SEX));
        params.put(Common.PUSH_KEY,RetrofitUtil.toRequestBody(PUSH_KEY));

        params.put(Common.OS_VER,RetrofitUtil.toRequestBody(OS_VER));
        params.put(Common.DEVICE_ID,RetrofitUtil.toRequestBody(DEVICE_ID));

        params.put(Common.OS_TYPE,RetrofitUtil.toRequestBody(Common.TYPE_OS_ANDROID));
        params.put(Common.MEMBER_TYPE,RetrofitUtil.toRequestBody(Common.TYPE_EMAIL));

        Call<MsgVO> call = RetrofitAdapter.getInstance().postSign(params);
        call.enqueue(new Callback<MsgVO>() {
            @Override
            public void onResponse(Call<MsgVO> call, Response<MsgVO> response) {
                MsgVO result = response.body();
                if(result != null ){
                    if(result.getResult().equals("1")){ // 성공
                        doLogin(ID,PASSWORD,Common.TYPE_EMAIL);
                    }else{

                    }
                }

            }

            @Override
            public void onFailure(Call<MsgVO> call, Throwable t) {
                Log.e("","");
            }
        });
    }

    /*public void doLogin(final String ID , final String PASSWORD, final String loginType){

        String PUSH_KEY = "";
        String OS_VER = Utils.getOsVersion();
        String DEVICE_ID = Utils.getAndroidID(this);

        Map<String, RequestBody> params = new HashMap<String, RequestBody>();
        params.put(Common.ID, RetrofitUtil.toRequestBody(ID));
        params.put(Common.PASSWORD,RetrofitUtil.toRequestBody(PASSWORD));
        params.put(Common.PUSH_KEY,RetrofitUtil.toRequestBody(PUSH_KEY));
        params.put(Common.OS_VER,RetrofitUtil.toRequestBody(OS_VER));
        params.put(Common.OS_TYPE,RetrofitUtil.toRequestBody(Common.TYPE_OS_ANDROID));
        params.put(Common.DEVICE_ID,RetrofitUtil.toRequestBody(DEVICE_ID));
        params.put(Common.LAT,RetrofitUtil.toRequestBody(String.valueOf(KoreaTown.myLocation.getLatitude())));
        params.put(Common.LON,RetrofitUtil.toRequestBody(String.valueOf(KoreaTown.myLocation.getLongitude())));
        params.put(Common.MEMBER_TYPE,RetrofitUtil.toRequestBody(loginType));
        Call<MsgVO> call =  RetrofitAdapter.getInstance().doLogin(params);
        call.enqueue(new Callback<MsgVO>() {
            @Override
            public void onResponse(Call<MsgVO> call, Response<MsgVO> response) {
                MsgVO item = response.body();
                if(item != null ){
                    if(item.getResult().equals("1")){ // 성공
                        MemberVO memberVO = new MemberVO();
                        memberVO.setMEMBER_ID(ID);
                        memberVO.setPASSWORD(PASSWORD);
                        memberVO.setMEMBER_TYPE(loginType);
                        BusProvider.getInstance().post(new LoginEvent(memberVO));
                       *//* Intent intent = new Intent(BaseLogin.this, MainAct.class);
                        startActivity(intent);
                        finish();*//*
                    }else if(item.getResult().equals("2") || item.getResult().equals("3")){ // 무조건 로그인 api 재호출
                        doOnlyLogin(ID,PASSWORD,loginType);
                    }
                }
            }

            @Override
            public void onFailure(Call<MsgVO> call, Throwable t) {

            }
        });


    }*/

    /*public void doOnlyLogin(final String ID , final String PASSWORD, final String loginType){

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
        params.put(Common.MEMBER_TYPE,RetrofitUtil.toRequestBody(loginType));
        Call<MsgVO> call =  RetrofitAdapter.getInstance().doOnlyLogin(params);
        call.enqueue(new Callback<MsgVO>() {
            @Override
            public void onResponse(Call<MsgVO> call, Response<MsgVO> response) {
                MsgVO item = response.body();
                if(item != null ){
                    if(item.getResult().equals("1")){ // 성공
                        MemberVO memberVO = new MemberVO();
                        memberVO.setMEMBER_ID(ID);
                        memberVO.setPASSWORD(PASSWORD);
                        memberVO.setMEMBER_TYPE(loginType);
                        BusProvider.getInstance().post(new LoginEvent(memberVO));

                       *//* Intent intent = new Intent(BaseLogin.this, MainAct.class);
                        startActivity(intent);
                        finish();*//*
                    }else {

                    }
                }
            }

            @Override
            public void onFailure(Call<MsgVO> call, Throwable t) {

            }
        });


    }*/
}
