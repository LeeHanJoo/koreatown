package kr.nt.koreatown.intro;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.facebook.login.LoginManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import kr.nt.koreatown.R;
import kr.nt.koreatown.databinding.LoginactBinding;

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
    }

    View.OnClickListener signClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.email_login:

                    break;
                case R.id.facebook_login:
                    LoginManager.getInstance().logInWithReadPermissions(LoginAct.this, Arrays.asList("public_profile", "email"));
                    break;
                case R.id.kakao_login:
                    isKakaoLogin();
                    break;
            }
        }
    };


}

