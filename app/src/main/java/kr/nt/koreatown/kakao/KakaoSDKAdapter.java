package kr.nt.koreatown.kakao;

import android.app.Activity;
import android.content.Context;

import com.kakao.auth.ApprovalType;
import com.kakao.auth.AuthType;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.ISessionConfig;
import com.kakao.auth.KakaoAdapter;

import kr.nt.koreatown.KoreaTown;

/**
 * Created by user on 2016-05-18.
 */
public class KakaoSDKAdapter extends KakaoAdapter {

    public KakaoSDKAdapter() {
        super();
    }

    @Override
    public IApplicationConfig getApplicationConfig() {
        return new IApplicationConfig() {
            @Override
            public Activity getTopActivity() {
                return KoreaTown.getCurrentActivity();
            }

            @Override
            public Context getApplicationContext() {
                return KoreaTown.getGlobalApplicationContext();
            }
        };
    }

    @Override
    public ISessionConfig getSessionConfig() {
        return new ISessionConfig() {
            @Override
            public AuthType[] getAuthTypes() {
                return new AuthType[] {AuthType.KAKAO_LOGIN_ALL};
            }

            @Override
            public boolean isUsingWebviewTimer() {
                return false;
            }

            @Override
            public ApprovalType getApprovalType() {
                return ApprovalType.INDIVIDUAL;
            }

            @Override
            public boolean isSaveFormData() {
                return true;
            }
        };
    }
}
