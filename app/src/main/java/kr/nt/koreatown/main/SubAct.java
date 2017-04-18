package kr.nt.koreatown.main;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import kr.nt.koreatown.R;
import kr.nt.koreatown.databinding.MainactBinding;
import kr.nt.koreatown.databinding.SubactivityBinding;

/**
 * Created by user on 2017-04-18.
 */

public class SubAct extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        SubactivityBinding binding = DataBindingUtil.setContentView(this, R.layout.subactivity);

    }
}
