package kr.nt.koreatown.main;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import kr.nt.koreatown.Listener.MyMenuClickListener;
import kr.nt.koreatown.R;
import kr.nt.koreatown.databinding.MainactBinding;

import static kr.nt.koreatown.R.id.map;

/**
 * Created by user on 2017-04-17.
 */

public class MainAct extends AppCompatActivity implements OnMapReadyCallback{

    public final Toolbar toolbar = null;
    public DrawerLayout drawer;
    private GoogleMap gMap;
    SupportMapFragment mapFragment = null;
    private  MainactBinding binding = null;
    GroundOverlay overlay = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.mainact);

        setSupportActionBar(binding.includeMain.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, binding.drawerLayout, binding.includeMain.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();
        binding.includeMain.toolbar.setNavigationIcon(R.drawable.icon_menu);
        binding.navMap.setOnClickListener(new MyMenuClickListener());
        binding.navSetting.setOnClickListener(new MyMenuClickListener());

       // init();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);

        marker_root_view = LayoutInflater.from(this).inflate(R.layout.marker, null);

    }
    View marker_root_view;
    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (gMap == null) {
            gMap = googleMap;
           // setUpMap();
        }
        LatLng Shenzhen = new LatLng(37.383592, 126.932749);
        gMap.addMarker(new MarkerOptions().position(Shenzhen).title("테스트마커").icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, marker_root_view))));


        Bitmap bitmap =  Bitmap.createBitmap(100,100,Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(Color.parseColor("#66000000"));

        overlay = gMap.addGroundOverlay(new GroundOverlayOptions().image(BitmapDescriptorFactory.fromBitmap(bitmap)).position(Shenzhen,8600f, 6500f));
        CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(Shenzhen, (float)18.0);
        gMap.moveCamera(cu);
        gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                CameraUpdate center = CameraUpdateFactory.newLatLng(marker.getPosition());
                gMap.animateCamera(center);
                overlay.remove();
                return true;
            }
        });
       /* mTileOverlay = gMap.addTileOverlay(new TileOverlayOptions()
                .tileProvider(tileProvider).transparency(0.5f));*/




       /* CircleOptions circleOptions = new CircleOptions()
                .center(Shenzhen);
        circleOptions.radius(100); // In meters
        circleOptions.fillColor()
        gMap.addCircle(circleOptions);*/
    }

    private Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)); view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels); view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels); view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }



}
