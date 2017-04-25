package kr.nt.koreatown.intro;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;

import org.json.JSONObject;

/**
 * Created by user on 2017-04-25.
 */

public class BaseLogin extends AppCompatActivity {

    CallbackManager callbackManager;
    AccessTokenTracker accessTokenTracker;
    AccessToken accessToken;
    ProfileTracker profileTracker;
    private ISessionCallback mKakaocallback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(this.getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, callback);
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                // Set the access token using
                // currentAccessToken when it's loaded or set.
            }
        };
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(
                    Profile oldProfile,
                    Profile currentProfile) {
                // App code
            }
        };
        // If the access token is available already assign it.
        accessToken = AccessToken.getCurrentAccessToken();
        accessTokenTracker.startTracking();

    }

    public void isKakaoLogin() {
        // 카카오 세션을 오픈한다
        mKakaocallback = new ISessionCallback() {
            @Override
            public void onSessionOpened() {
                KakaorequestMe();
            }

            @Override
            public void onSessionOpenFailed(KakaoException exception) {
                Log.e("", "");
                if(exception != null){
                    Toast.makeText(getApplicationContext(),exception.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        };
        com.kakao.auth.Session.getCurrentSession().addCallback(mKakaocallback);
        com.kakao.auth.Session.getCurrentSession().checkAndImplicitOpen();
        com.kakao.auth.Session.getCurrentSession().open(AuthType.KAKAO_TALK_EXCLUDE_NATIVE_LOGIN, this);
    }
    protected void KakaorequestMe() {
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                int ErrorCode = errorResult.getErrorCode();
                int ClientErrorCode = -777;

                if (ErrorCode == ClientErrorCode) {
                    Toast.makeText(getApplicationContext(), "카카오톡 서버의 네트워크가 불안정합니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("TAG", "오류로 카카오로그인 실패 ");
                }
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.d("TAG", "오류로 카카오로그인 실패 ");
            }

            @Override
            public void onSuccess(UserProfile userProfile) {
                //ImageView profileUrl = userProfile.getProfileImagePath();

                //long serviceUserid = userProfile.getServiceUserId();
                //String uuid = userProfile.getUUID();


                String	userId = String.valueOf(userProfile.getId());
                String userName = userProfile.getNickname();
                signKakao(userId,userId,userName);
            }

            @Override
            public void onNotSignedUp() {
                // 자동가입이 아닐경우 동의창
                Log.e("","");
            }
        });
    }

    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
            Toast.makeText(getApplicationContext(), loginResult.getAccessToken().getUserId(), Toast.LENGTH_LONG).show();

            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {
                            try{
                                //String userId = response.getJSONObject().getString("email");
                                String userId = response.getJSONObject().getString("id");
                                String userPw = response.getJSONObject().getString("id");
                                String gender = response.getJSONObject().getString("gender");
                                String name = response.getJSONObject().getString("name");
                                singFacebook(userId,userPw,gender,name);
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,link,email,gender,age_range");
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {
            Toast.makeText(getApplicationContext(), "User sign in canceled!", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onError(FacebookException e) {

        }
    };

    public void signKakao(final String id,final  String pw,final String name){
       /* final String pushkey = SharedManager.getInstance().getString(this, Common.PUSH_KEY);
        final String deviceId = CommonUtil.getDeviceId(this);
        Call<MemberVO> call = RetrofitAdapter.getInstance().postSign(id,id, pw, "", "", "", "", pushkey, Common.OS_TYPE, deviceId,name,"K");
        call.enqueue(new Callback<MemberVO>() {
            @Override
            public void onResponse(Call<MemberVO> call, Response<MemberVO> response) {
                MemberVO item = response.body();
                if(item == null || item.getResult() == null){
                    CommonUtil.showOnBtnDialog(BaseLogin.this,"서버오류","잠시후 다시시도해주세요",null);
                }else{
                    if(item.getResult().equals("1")){ // 가입완료
                        SharedManager.getInstance().setString(BaseLogin.this,Common.USER_ID,id);
                        SharedManager.getInstance().setString(BaseLogin.this,Common.USER_PW,pw);
                        SharedManager.getInstance().setString(BaseLogin.this,Common.APP_LOGINTYPE,"K");
                        SharedManager.getInstance().setBoolean(BaseLogin.this, Common.AUTOLOGIN,true);
                        //Intent intent = new Intent(BaseLogin.this,SignHopeActivity.class);
                        Intent intent = new Intent(BaseLogin.this,KakaoFaceActivity.class);
                        //intent.putExtra("weight",weight);
                        intent.putExtra("name",name);
                        startActivity(intent);
                        finish();
                        //	Intent intent = new Intent(BaseLogin.this,FavHospitalActivity.class);
                        //	startActivity(intent);
                    }else if(item.getResult().equals("2")){ // 이미아이디있음 로그인 시도
                        doLogin(id,pw,pushkey,deviceId,"K");
                    }else{
                        CommonUtil.showOnBtnDialog(BaseLogin.this,"카카오로그인",item.getData().getMsg(),null);
                    }
                }
            }

            @Override
            public void onFailure(Call<MemberVO> call, Throwable t) {
                CommonUtil.showOnBtnDialog(BaseLogin.this,"서버오류","잠시후 다시시도해주세요",null);
            }
        });*/
    }

    public void singFacebook(final String id,final  String pw,final String gender,final String name){
        /*final String pushkey = SharedManager.getInstance().getString(this, Common.PUSH_KEY);
        final String deviceId = CommonUtil.getDeviceId(this);
        Call<MemberVO> call = RetrofitAdapter.getInstance().postSign(id,id, pw, "", "", getGender(gender), "", pushkey, Common.OS_TYPE, deviceId,name,"F");
        call.enqueue(new Callback<MemberVO>() {
            @Override
            public void onResponse(Call<MemberVO> call, Response<MemberVO> response) {
                MemberVO item = response.body();
                if(item == null || item.getResult() == null){
                    CommonUtil.showOnBtnDialog(BaseLogin.this,"서버오류","잠시후 다시시도해주세요",null);
                }else{
                    if(item.getResult().equals("1")){ // 가입완료
                        SharedManager.getInstance().setString(BaseLogin.this,Common.USER_ID,id);
                        SharedManager.getInstance().setString(BaseLogin.this,Common.USER_PW,pw);
                        SharedManager.getInstance().setString(BaseLogin.this,Common.APP_LOGINTYPE,"F");
                        SharedManager.getInstance().setBoolean(BaseLogin.this, Common.AUTOLOGIN, true);

                        Intent intent = new Intent(BaseLogin.this,KakaoFaceActivity.class);
                        intent.putExtra("name", name);
                        startActivity(intent);
                        finish();

					*//*	Intent intent = new Intent(BaseLogin.this,SignHopeActivity.class);
						intent.putExtra("mSex",getGender(gender));
						startActivity(intent);
						finish();*//*
                        //	Intent intent = new Intent(BaseLogin.this,FavHospitalActivity.class);
                        //	startActivity(intent);
                    }else if(item.getResult().equals("2")){ // 이미아이디있음 로그인 시도
                        doLogin(id,pw,pushkey,deviceId,"F");
                    }else{
                        CommonUtil.showOnBtnDialog(BaseLogin.this,"페이스북",item.getData().getMsg(),null);
                    }
                }
            }

            @Override
            public void onFailure(Call<MemberVO> call, Throwable t) {
                CommonUtil.showOnBtnDialog(BaseLogin.this,"서버오류","잠시후 다시시도해주세요",null);
            }
        });*/
    }

    public void doLogin(final String id,final String pw, final String pushkey, final String deviceId,final String loginType){
        /*Call<MemberVO> call = RetrofitAdapter.getInstance().doLogin(id, Common.OS_TYPE, pushkey, pw, deviceId);
        call.enqueue(new Callback<MemberVO>() {
            @Override
            public void onResponse(Call<MemberVO> call, Response<MemberVO> response) {
                MemberVO item = response.body();
                if(item == null || item.getResult() == null){
                    CommonUtil.showOnBtnDialog(BaseLogin.this,"서버오류","잠시후 다시시도해주세요",null);
                }else{
                    if(item.getResult().equals("1")) { // 로그인 성공
                        SharedManager.getInstance().setString(BaseLogin.this,Common.USER_ID,id);
                        SharedManager.getInstance().setString(BaseLogin.this,Common.USER_PW,pw);
                        SharedManager.getInstance().setString(BaseLogin.this, Common.APP_LOGINTYPE, loginType);
                        SharedManager.getInstance().setBoolean(BaseLogin.this, Common.AUTOLOGIN, true);
                        Intent intent = new Intent(BaseLogin.this, MainActivity.class);
                        intent.putExtra("PUSHYN", getIntent().getBooleanExtra("PUSHYN", false));
                        intent.putExtra("PUSHITEM", getIntent().getParcelableExtra("PUSHITEM"));
                        startActivity(intent);
                        finish();
                    }else{
                        if(item.getResult().equals("2") ||item.getResult().equals("3") ){
                            CommonUtil.showTwoBtnDialog(BaseLogin.this, "로그인", item.getData().getMsg() + "\n 계속 진행하시겠습니까?", new CommonUtil.onDialogClick() {
                                @Override
                                public void setonConfirm() {
                                    goLogin(id,pw,pushkey,deviceId,loginType);
                                }

                                @Override
                                public void setonCancel() {

                                }
                            });

                        }else{
                            CommonUtil.showOnBtnDialog(BaseLogin.this,"로그인",item.getData().getMsg(),null);
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<MemberVO> call, Throwable t) {
                CommonUtil.showOnBtnDialog(BaseLogin.this,"서버오류", "잠시후 다시시도해주세요",null);
            }
        });*/
    }

    public String getGender(String gender){
        String result = "";
        if (gender != null && gender.equals("male")){
            result = "남자";
        }else if(gender != null && gender.equals("female")){
            result = "여자";
        }
        return result;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
        Session.getCurrentSession().removeCallback(mKakaocallback);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }


        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
