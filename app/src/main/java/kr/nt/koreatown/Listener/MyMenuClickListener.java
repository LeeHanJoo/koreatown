package kr.nt.koreatown.Listener;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import kr.nt.koreatown.R;
import kr.nt.koreatown.setting.SettingAct;

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
                Intent intent = new Intent(context, SettingAct.class);
                context.startActivity(intent);
                break;
        }
    }
}
