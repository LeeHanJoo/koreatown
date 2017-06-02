package kr.nt.koreatown.setting;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import kr.nt.koreatown.R;
import kr.nt.koreatown.bus.BusProvider;
import kr.nt.koreatown.bus.LogoutEvent;
import kr.nt.koreatown.databinding.SettingactBinding;
import kr.nt.koreatown.util.CommonUtil;

/**
 * Created by user on 2017-05-10.
 */

public class SettingAct extends AppCompatActivity implements View.OnClickListener{

    private SettingactBinding binding = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.settingact);

        binding.toolbar.setTitle("설정");
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // ((LinearLayout.LayoutParams) binding.viewDetail.getLayoutParams()).topMargin = 1 + -633;
        binding.myinfoLayout.setOnClickListener(this);
        binding.logoutLayout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.myinfo_layout:
                Intent intent = new Intent(SettingAct.this,MyProfileAct.class);
                startActivity(intent);
                break;

            case R.id.logout_layout:
                CommonUtil.showTwoBtnDialog(SettingAct.this, "로그아웃", "로그아웃 하시겠습니까?", new CommonUtil.onDialogClick() {
                    @Override
                    public void setonConfirm() {

                        BusProvider.getInstance().post(new LogoutEvent());
                        finish();
                    }

                    @Override
                    public void setonCancel() {}
                });
                break;
        }
    }

    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // NavUtils.navigateUpFromSameTask(this);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    };
}
