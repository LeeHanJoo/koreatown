package kr.nt.koreatown.feed;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import kr.nt.koreatown.R;
import kr.nt.koreatown.databinding.StoryactBinding;

/**
 * Created by user on 2017-04-27.
 */

public class StoryAct extends AppCompatActivity {

    private StoryactBinding binding = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.storyact);

        binding.toolbar.setTitle("소식등록");
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }


}
