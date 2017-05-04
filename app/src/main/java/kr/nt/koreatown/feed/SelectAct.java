package kr.nt.koreatown.feed;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import kr.nt.koreatown.R;
import kr.nt.koreatown.databinding.AddlayoutBinding;

/**
 * Created by user on 2017-05-04.
 */

public class SelectAct extends Activity implements View.OnClickListener{

    AddlayoutBinding binding = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.7f;

        // layoutParams.softInputMode =
        // WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
        // |WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
        getWindow().setAttributes(layoutParams);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // 다이얼로그
        //getWindow().setBackgroundDrawable(new ColorDrawable(Color.YELLOW)); // 다이얼로그
        binding = DataBindingUtil.setContentView(this, R.layout.addlayout);
        binding.btnAddhouse.setOnClickListener(this);
        binding.btnAddnews.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_addhouse:
                setResult(RESULT_OK,new Intent().putExtra("ADD_FLAG","1"));
                finish();
                break;
            case R.id.btn_addnews:
                setResult(RESULT_OK,new Intent().putExtra("ADD_FLAG","2"));
                finish();
                break;
        }
    }
}
