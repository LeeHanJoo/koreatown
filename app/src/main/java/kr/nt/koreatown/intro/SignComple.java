package kr.nt.koreatown.intro;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import kr.nt.koreatown.Common;
import kr.nt.koreatown.R;
import kr.nt.koreatown.databinding.SignCompleBinding;

/**
 * Created by user on 2017-06-25.
 */

public class SignComple extends  BaseLogin {


    SignCompleBinding binding = null;

    private String ID, PASSWORD,TYPE;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.sign_comple);

        ID = getIntent().getStringExtra(Common.ID);
        PASSWORD = getIntent().getStringExtra(Common.PASSWORD);
        TYPE = getIntent().getStringExtra(Common.TYPE);

        init();
    }

    private void init(){
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin(ID,PASSWORD,TYPE);
                finish();
            }
        });
    }
}
