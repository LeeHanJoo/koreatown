package kr.nt.koreatown.feed;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import gun0912.tedbottompicker.TedBottomPicker;
import kr.nt.koreatown.R;
import kr.nt.koreatown.databinding.RoomactBinding;
import kr.nt.koreatown.util.BusProvider;
import kr.nt.koreatown.util.RefreshViewEvent;
import kr.nt.koreatown.util.Utils;

/**
 * Created by user on 2017-04-27.
 */

public class RoomAct extends AppCompatActivity implements View.OnClickListener{

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
        binding.visibleDetailBtn.setOnClickListener(this);
        binding.addimage.setOnClickListener(this);
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
        BusProvider.getInstance().post(new RefreshViewEvent());
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.visible_detail_btn:
                if(binding.viewDetail.getVisibility() == View.VISIBLE){
                    binding.viewDetail.setVisibility(View.GONE);
                }else{
                    binding.viewDetail.setVisibility(View.VISIBLE);
                }
                scrollToEnd();
                break;
            case R.id.addimage:
                AddImage();
                break;
        }
    }

    private void AddImage(){
        TedBottomPicker bottomSheetDialogFragment = new TedBottomPicker.Builder(RoomAct.this)
                .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
                    @Override
                    public void onImageSelected(Uri uri) {
                        // uri 활용
                        ImageView imageview = new ImageView(RoomAct.this);
                        imageview.setScaleType(ImageView.ScaleType.FIT_XY);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Utils.toPixel(RoomAct.this,80),Utils.toPixel(RoomAct.this,80));
                        params.rightMargin = Utils.toPixel(RoomAct.this,10);
                        imageview.setLayoutParams(params);
                        Glide.with(RoomAct.this).load("file://" + uri).into(imageview);
                        binding.addimagelayout.addView(imageview,0);
                        //binding.addimagelayout.addView(imageview);

                    }
                }).setMaxCount(1000)
                .create();

        bottomSheetDialogFragment.show(getSupportFragmentManager());

    }
}
