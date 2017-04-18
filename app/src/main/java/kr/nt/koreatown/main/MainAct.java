package kr.nt.koreatown.main;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import kr.nt.koreatown.Listener.MyMenuClickListener;
import kr.nt.koreatown.R;
import kr.nt.koreatown.databinding.MainactBinding;

/**
 * Created by user on 2017-04-17.
 */

public class MainAct extends AppCompatActivity {

    public final Toolbar toolbar = null;
    public DrawerLayout drawer;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainactBinding binding = DataBindingUtil.setContentView(this, R.layout.mainact);

        setSupportActionBar(binding.includeMain.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, binding.drawerLayout, binding.includeMain.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();
        binding.includeMain.toolbar.setNavigationIcon(R.drawable.icon_menu);
        binding.navMap.setOnClickListener(new MyMenuClickListener());
        binding.navSetting.setOnClickListener(new MyMenuClickListener());


    }
}
