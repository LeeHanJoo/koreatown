package kr.nt.koreatown.main;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.pnikosis.materialishprogress.ProgressWheel;

/**
 * Created by user on 2017-07-10.
 */

public class BaseActivity extends AppCompatActivity {

    public void progressOn(ProgressWheel view){
        view.setVisibility(View.VISIBLE);
    }

    public void progressOff(ProgressWheel view){
        view.setVisibility(View.GONE);
    }
}
