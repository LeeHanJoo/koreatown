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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.nt.koreatown.Common;
import kr.nt.koreatown.KoreaTown;
import kr.nt.koreatown.R;
import kr.nt.koreatown.bus.BusProvider;
import kr.nt.koreatown.bus.LoginEvent;
import kr.nt.koreatown.retrofit.RetrofitAdapter;
import kr.nt.koreatown.retrofit.RetrofitUtil;
import kr.nt.koreatown.util.CommonUtil;
import kr.nt.koreatown.util.Utils;
import kr.nt.koreatown.vo.MemberVO;
import kr.nt.koreatown.vo.MsgVO;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        List<String> propertyKeys = new ArrayList<String>();
        propertyKeys.add("kaccount_email");
        propertyKeys.add("nickname");
        propertyKeys.add("kaccount_email_verified");
        propertyKeys.add("thumbnail_image");

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


                String userId = String.valueOf(userProfile.getId());
                String userNickname = userProfile.getNickname();
                String email = userProfile.getProperties().get("kaccount_email") ==  null ? "" : "";
                String kaccount_email_verified = userProfile.getProperties().get("kaccount_email_verified");
                //String userImage = userProfile.getThumbnailImagePath();
                // userProfile.get
                //userProfile.getNickname();
                signKakao(userId,userId,userNickname,email);
            }

            @Override
            public void onNotSignedUp() {
                // 자동가입이 아닐경우 동의창
                Log.e("","");
            }
        },propertyKeys,false);
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
                                String userEmail = response.getJSONObject().getString("email");
                                String userId = response.getJSONObject().getString("id");
                                String userPw = userId;
                                String gender = response.getJSONObject().getString("gender");
                                String name = response.getJSONObject().getString("name");
                               // String birthday = response.getJSONObject().getString("birthday");
                                singFacebook(userId,userPw,userEmail,getGender(gender),name);
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender,birthday");
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

    public void signKakao(final String ID,final  String PASSWORD,final String NICK_NAME,String EMAIL){
        String PUSH_KEY = "";
        String OS_VER = Utils.getOsVersion();
        String DEVICE_ID = Utils.getAndroidID(this);

        Map<String, RequestBody> params = new HashMap<String, RequestBody>();
        params.put(Common.ID, RetrofitUtil.toRequestBody(ID));
        params.put(Common.EMAIL,RetrofitUtil.toRequestBody(EMAIL));
        params.put(Common.PASSWORD,RetrofitUtil.toRequestBody(PASSWORD));
        //params.put(BIRTHDAY,RetrofitUtil.toRequestBody(BIRTHDAY));

        params.put(Common.NICK_NAME,RetrofitUtil.toRequestBody(NICK_NAME));
      //  params.put(SEX,RetrofitUtil.toRequestBody(SEX));
        params.put(Common.PUSH_KEY,RetrofitUtil.toRequestBody(PUSH_KEY));

        params.put(Common.OS_VER,RetrofitUtil.toRequestBody(OS_VER));
        params.put(Common.DEVICE_ID,RetrofitUtil.toRequestBody(DEVICE_ID));

        params.put(Common.OS_TYPE,RetrofitUtil.toRequestBody(Common.TYPE_OS_ANDROID));
        params.put(Common.MEMBER_TYPE,RetrofitUtil.toRequestBody(Common.TYPE_KAKAO));

        Call<MsgVO> call = RetrofitAdapter.getInstance().postSign(params);
        call.enqueue(new Callback<MsgVO>() {
            @Override
            public void onResponse(Call<MsgVO> call, Response<MsgVO> response) {
                MsgVO result = response.body();
                if(result != null ){
                    if(result.getResult().equals("1")){ // 성공
                        doLogin(ID,PASSWORD,Common.TYPE_KAKAO);
                        //BusProvider.getInstance().post(new LoginEvent());
                        //finish();
                    }else if(result.getResult().equals("2")){ // 이미아이디있음 로그인 시도
                        doLogin(ID,PASSWORD,Common.TYPE_KAKAO);
                    }else{
                        CommonUtil.showOnBtnDialog(BaseLogin.this,"서버오류","잠시후 다시시도해주세요",null);
                        //CommonUtil.showOnBtnDialog(BaseLogin.this,"카카오로그인",item.getData().getMsg(),null);
                    }
                }

            }

            @Override
            public void onFailure(Call<MsgVO> call, Throwable t) {
                CommonUtil.showOnBtnDialog(BaseLogin.this,"서버오류","잠시후 다시시도해주세요",null);
                Log.e("","");
            }
        });
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

    public void singFacebook(final String ID,final  String PASSWORD,String EMAIL,final String SEX,final String NICK_NAME){

        String PUSH_KEY = "";
        String OS_VER = Utils.getOsVersion();
        String DEVICE_ID = Utils.getAndroidID(this);

        Map<String, RequestBody> params = new HashMap<String, RequestBody>();
        params.put(Common.ID, RetrofitUtil.toRequestBody(ID));
        params.put(Common.EMAIL,RetrofitUtil.toRequestBody(EMAIL));
        params.put(Common.PASSWORD,RetrofitUtil.toRequestBody(PASSWORD));
        //params.put(BIRTHDAY,RetrofitUtil.toRequestBody(BIRTHDAY));

        params.put(Common.NICK_NAME,RetrofitUtil.toRequestBody(NICK_NAME));
        params.put(Common.SEX,RetrofitUtil.toRequestBody(SEX));
        params.put(Common.PUSH_KEY,RetrofitUtil.toRequestBody(PUSH_KEY));

        params.put(Common.OS_VER,RetrofitUtil.toRequestBody(OS_VER));
        params.put(Common.DEVICE_ID,RetrofitUtil.toRequestBody(DEVICE_ID));

        params.put(Common.OS_TYPE,RetrofitUtil.toRequestBody(Common.TYPE_OS_ANDROID));
        params.put(Common.MEMBER_TYPE,RetrofitUtil.toRequestBody(Common.TYPE_FACEBOOK));

        Call<MsgVO> call = RetrofitAdapter.getInstance().postSign(params);
        call.enqueue(new Callback<MsgVO>() {
            @Override
            public void onResponse(Call<MsgVO> call, Response<MsgVO> response) {
                MsgVO result = response.body();
                if(result != null ){
                    if(result.getResult().equals("1")){ // 성공
                        doLogin(ID,PASSWORD,Common.TYPE_FACEBOOK);
                        //BusProvider.getInstance().post(new LoginEvent());
                       // finish();
                    }else if(result.getResult().equals("2")){ // 이미아이디있음 로그인 시도
                        doLogin(ID,PASSWORD,Common.TYPE_FACEBOOK);
                    }else{
                        CommonUtil.showOnBtnDialog(BaseLogin.this,"로그인실패",result.getData().getMsg(),null);
                        //CommonUtil.showOnBtnDialog(BaseLogin.this,"카카오로그인",item.getData().getMsg(),null);
                    }
                }
            }

            @Override
            public void onFailure(Call<MsgVO> call, Throwable t) {
                CommonUtil.showOnBtnDialog(BaseLogin.this,"로그인실패",getString(R.string.server_err),null);
            }
        });
    }

    public void doLogin(final String ID , final String PASSWORD, final String loginType){

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

                    }else if(item.getResult().equals("2") || item.getResult().equals("3")){ // 무조건 로그인 api 재호출
                        doOnlyLogin(ID,PASSWORD,loginType);
                    }else{
                        CommonUtil.showOnBtnDialog(BaseLogin.this,"로그인실패",item.getData().getMsg(),null);
                    }
                }
            }

            @Override
            public void onFailure(Call<MsgVO> call, Throwable t) {
                CommonUtil.showOnBtnDialog(BaseLogin.this,"로그인실패",getString(R.string.server_err),null);
            }
        });
    }

    public void doOnlyLogin(final String ID , final String PASSWORD, final String loginType){

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

                    }else {
                        CommonUtil.showOnBtnDialog(BaseLogin.this,"로그인실패",item.getData().getMsg(),null);
                    }
                }
            }

            @Override
            public void onFailure(Call<MsgVO> call, Throwable t) {
                CommonUtil.showOnBtnDialog(BaseLogin.this,"로그인실패",getString(R.string.server_err),null);
            }
        });
    }

    public String getGender(String gender){
        String result = "";
        if (gender != null && gender.equals("male")){
            result = "M";
        }else if(gender != null && gender.equals("female")){
            result = "W";
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
