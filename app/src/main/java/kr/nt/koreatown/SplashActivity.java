package kr.nt.koreatown;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;

import kr.lhj.chichapermission.ChichaPermission;
import kr.lhj.chichapermission.PermissionListener;
import kr.nt.koreatown.main.MainAct;
import kr.nt.koreatown.util.MyLocation;
import kr.nt.koreatown.util.Utils;

public class SplashActivity extends AppCompatActivity {

    private boolean gps = false;
    AlertDialog GpsAlert, Gpsservice;
    MyLocation mlocation = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new ChichaPermission(this).setConfirmMessage("앱을 사용하려면 권한을 승인하셔야 합니다.")
                .setDeniedMessage("디나인메시지")
                .setPermissionListener(permissionlistener)
                .setPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.READ_PHONE_STATE,
                })
                .goCheck();
    }

    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {

            inita();
           // Toast.makeText(SplashActivity.this, "권한 허가", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(SplashActivity.this, "권한 거부\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            finish();
        }
    };

    @Override
    protected void onRestart() {
        super.onRestart();
        chkgps();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mlocation.LocationDestory();
        locationResult = null;
    }

    private void inita(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                chkgps();
            }
        }, 1000);
    }

    MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {
        @Override
        public void gotLocation(Location location) {
            //  String msg = "lon: "+location.getLongitude()+" -- lat: "+location.getLatitude();
            // Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            if(location == null){
                MyLocation location2 = new MyLocation();
                location2.getLocation(SplashActivity.this,locationResult);
            }else{
                KoreaTown.myLocation = location;
               // mlocation.LocationDestory();
              //  Intent intent = new Intent(SplashActivity.this,LoginAct.class);
                Intent intent = new Intent(SplashActivity.this,MainAct.class);
                startActivity(intent);
                finish();
            }
        }
    };



    private void goNext(){
        //Intent intent = new Intent(SplashActivity.this,MainAct.class);

        mlocation = new MyLocation();
        mlocation.getLocation(SplashActivity.this,locationResult);

      /*  Intent intent = new Intent(SplashActivity.this,LoginAct.class);
        startActivity(intent);
        finish();*/
    }

    private void chkgps(){
        gps = Utils.ChkGps(SplashActivity.this);
        if (gps) {//
            String gs = Utils.whatGps(SplashActivity.this);
            if (gs.equals("gps") || gs.equals("network,gps") || gs.equals("gps,network")) {
                goNext();
            }else {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ShowDialog("GPS ON", "정확한 위치정보를 위해 GPS를 켜주시기바랍니다.");
                    }
                }, 1000);
            }
        } else {
            GpsService(SplashActivity.this);
        }
    }

    public void GpsService(final Activity activity) {
        Gpsservice = new AlertDialog.Builder(activity)
                .setTitle(getString(R.string.splash_gps_d2_title))
                .setMessage(getString(R.string.splash_gps_d2_msg))
                .setCancelable(false)
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        intent.addCategory(Intent.CATEGORY_DEFAULT);
                        activity.startActivity(intent);
                    }
                }).show();
    }

    public void ShowDialog(String title, String msg) { // 다이얼로그
        GpsAlert = new AlertDialog.Builder(this).setTitle(title)
                .setMessage(msg).setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        intent.addCategory(Intent.CATEGORY_DEFAULT);
                        startActivity(intent);
                    }
                }).show();
    }
}
