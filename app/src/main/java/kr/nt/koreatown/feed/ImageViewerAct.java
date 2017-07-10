package kr.nt.koreatown.feed;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;

import kr.nt.koreatown.R;
import kr.nt.koreatown.databinding.ImageviewerBinding;
import kr.nt.koreatown.main.BaseActivity;
import kr.nt.koreatown.util.Utils;

/**
 * Created by user on 2017-07-10.
 */

public class ImageViewerAct extends BaseActivity {

    ImageviewerBinding binding ;
    public final static String ImageURL = "IMAGEURL";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.imageviewer);

        binding.toolbar.setTitle("크게보기");
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        binding.toolbar.setBackgroundColor(Utils.getColor(this,R.color.colorToolbar));

        String imageUrl = getIntent().getStringExtra(ImageURL);

        Glide.with(this).load(imageUrl).into(binding.ivPhoto);
    }
}
