package kr.nt.koreatown;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.kakao.auth.KakaoSDK;

import kr.nt.koreatown.kakao.KakaoSDKAdapter;

/**
 * Created by user on 2017-04-20.
 */

public class KoreaTown extends Application {

    public static KoreaTown instance ;
    public static double[] my_gps = new double[2];
    private static volatile Activity currentActivity = null;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        KakaoSDK.init(new KakaoSDKAdapter());
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static Activity getCurrentActivity() {
        Log.d("TAG", "++ currentActivity : " + (currentActivity != null ? currentActivity.getClass().getSimpleName() : ""));
        return currentActivity;
    }

    public static KoreaTown getGlobalApplicationContext() {
        if(instance == null)
            throw new IllegalStateException("this application does not inherit GlobalApplication");
        return instance;
    }
}
