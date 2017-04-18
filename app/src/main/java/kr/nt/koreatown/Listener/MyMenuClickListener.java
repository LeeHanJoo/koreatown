package kr.nt.koreatown.Listener;

import android.util.Log;
import android.view.View;

import kr.nt.koreatown.R;

/**
 * Created by user on 2017-04-17.
 */

public class MyMenuClickListener implements View.OnClickListener {


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.nav_map:
                Log.e("","");
                break;
        }
    }
}
