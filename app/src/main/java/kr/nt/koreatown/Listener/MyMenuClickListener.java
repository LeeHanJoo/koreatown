package kr.nt.koreatown.Listener;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import kr.nt.koreatown.KoreaTown;
import kr.nt.koreatown.R;
import kr.nt.koreatown.intro.LoginAct;
import kr.nt.koreatown.setting.SettingAct;
import kr.nt.koreatown.util.CommonUtil;

/**
 * Created by user on 2017-04-17.
 */

public class MyMenuClickListener implements View.OnClickListener {

    Activity context = null;
    public MyMenuClickListener(Activity context){
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.nav_map:
                Log.e("","");
                break;
            case R.id.nav_setting:
                if(KoreaTown.useLogin){
                    Intent intent = new Intent(context, SettingAct.class);
                    context.startActivity(intent);
                }else{
                    CommonUtil.showTwoBtnDialog(context, "로그인", "로그인이 필요합니다. 로그인페이지로 이동하시겠습니까?", new CommonUtil.onDialogClick() {
                        @Override
                        public void setonConfirm() {
                            Intent intent = new Intent(context, LoginAct.class);
                            context.startActivity(intent);
                        }

                        @Override
                        public void setonCancel() {

                        }
                    });
                }

                break;
        }
    }
}
