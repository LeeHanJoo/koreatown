package kr.nt.koreatown.feed;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import kr.nt.koreatown.R;
import kr.nt.koreatown.databinding.RoomactBinding;

/**
 * Created by user on 2017-04-27.
 */

public class RoomAct extends AppCompatActivity {

    private RoomactBinding binding = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.roomact);

        binding.toolbar.setTitle("등록");
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

       // ((LinearLayout.LayoutParams) binding.viewDetail.getLayoutParams()).topMargin = 1 + -633;

        binding.visibleDetailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(binding.viewDetail.getVisibility() == View.VISIBLE){
                    binding.viewDetail.setVisibility(View.GONE);
                }else{
                    binding.viewDetail.setVisibility(View.VISIBLE);
                }

                scrollToEnd();


            }
        });
    }
    public void scrollToEnd(){
        binding.scrollview.post(new Runnable() {
            @Override
            public void run() {
                binding.scrollview.fullScroll(View.FOCUS_DOWN);
            }

        });

    }


    @Override
    protected void onResume() {
        super.onResume();

    }


}
