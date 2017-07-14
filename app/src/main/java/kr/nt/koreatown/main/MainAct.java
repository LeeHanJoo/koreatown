package kr.nt.koreatown.main;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.squareup.otto.Subscribe;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import gun0912.tedbottompicker.TedBottomPicker;
import kr.nt.koreatown.Common;
import kr.nt.koreatown.KoreaTown;
import kr.nt.koreatown.Listener.MyMenuClickListener;
import kr.nt.koreatown.R;
import kr.nt.koreatown.SplashActivity;
import kr.nt.koreatown.bus.BusProvider;
import kr.nt.koreatown.bus.LoginEvent;
import kr.nt.koreatown.bus.LogoutEvent;
import kr.nt.koreatown.bus.RefreshViewEvent;
import kr.nt.koreatown.databinding.CommentItemBinding;
import kr.nt.koreatown.databinding.MainactBinding;
import kr.nt.koreatown.feed.CommentPopup;
import kr.nt.koreatown.feed.FeedDetailAct;
import kr.nt.koreatown.feed.RoomAct;
import kr.nt.koreatown.feed.SelectAct;
import kr.nt.koreatown.feed.StoryAct;
import kr.nt.koreatown.intro.BaseLogin;
import kr.nt.koreatown.intro.LoginAct;
import kr.nt.koreatown.retrofit.RetrofitAdapter;
import kr.nt.koreatown.retrofit.RetrofitUtil;
import kr.nt.koreatown.util.CommonUtil;
import kr.nt.koreatown.util.MyLocation;
import kr.nt.koreatown.util.SharedManager;
import kr.nt.koreatown.util.Utils;
import kr.nt.koreatown.view.ImagePagerAdapter;
import kr.nt.koreatown.view.MultiDirectionSlidingDrawer;
import kr.nt.koreatown.view.MyClusterRenderer;
import kr.nt.koreatown.vo.ClusterVO;
import kr.nt.koreatown.vo.FeedVO;
import kr.nt.koreatown.vo.MemberVO;
import kr.nt.koreatown.vo.MsgVO;
import kr.nt.koreatown.vo.RoomVO;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static kr.nt.koreatown.R.id.map;
import static kr.nt.koreatown.R.layout.marker;

/**
 * Created by user on 2017-04-17.
 */

public class MainAct extends BaseLogin implements OnMapReadyCallback{

    public static MainAct Instance = null;
    public static MainAct getInstance (){
        return Instance;
    }
    private GoogleMap gMap;
    SupportMapFragment mapFragment = null;
    private  MainactBinding binding = null;
    GroundOverlay overlay = null;
    public View marker_root_view;
    public boolean markerClickFlag = false;
    public Menu menu;
    private boolean moveStart = false;
    FeedVO Item = null;
    private FeedVO.Feed mClickMarker = null;
    public MyClusterRenderer mcr = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.mainact);
        Instance = this;
        setSupportActionBar(binding.includeMain.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, binding.drawerLayout, binding.includeMain.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();
        binding.includeMain.toolbar.setNavigationIcon(R.drawable.icon_menu);
        binding.navMap.setOnClickListener(new MyMenuClickListener(this));
        binding.navSetting.setOnClickListener(new MyMenuClickListener(this));
        binding.navMy.setOnClickListener(new MyMenuClickListener(this));
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(map);
        mapFragment.getMapAsync(this);

        marker_root_view = LayoutInflater.from(this).inflate(marker, null);
       // init();
        BusProvider.getInstance().register(this);

        boolean autoLogin = SharedManager.getInstance().getBoolean(this,Common.AUTO_LOGIN);
        if(autoLogin){
            String ID = SharedManager.getInstance().getString(this, Common.ID);
            String PASS = SharedManager.getInstance().getString(this, Common.PASSWORD);
            String TYPE = SharedManager.getInstance().getString(this, Common.MEMBER_TYPE);
            doLogin(ID,PASS,TYPE);
        }


    }


    @Override
    protected void onResume() {
        super.onResume();
        if(KoreaTown.useLogin){
            getProfile();
        }else{
            setUINotLogin();
        }
    }

    private void setUINotLogin(){
       // String ID = SharedManager.getInstance().getString(this, Common.ID);
        CommonUtil.setGlideImage(MainAct.this,Common.BASEFILEURL + ""+".jpg",binding.profileImg);
        binding.memberId.setText("로그인이 필요합니다.");
        binding.profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtil.showTwoBtnDialog(MainAct.this, "로그인", "로그인이 필요합니다. 로그인페이지로 이동하시겠습니까?", new CommonUtil.onDialogClick() {
                    @Override
                    public void setonConfirm() {
                        Intent intent = new Intent(MainAct.this, LoginAct.class);
                        startActivity(intent);
                    }

                    @Override
                    public void setonCancel() {

                    }
                });
            }
        });


    }

    private void setUIMenu(MemberVO Item){
        binding.profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtil.showTwoBtnDialog(MainAct.this, "프로필변경", "프로필 이미지를 변경하시겠습니까?", new CommonUtil.onDialogClick() {
                    @Override
                    public void setonConfirm() {
                        TedBottomPicker bottomSheetDialogFragment = new TedBottomPicker.Builder(MainAct.this)
                                .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
                                    @Override
                                    public void onImageSelected(Uri uri) {
                                        // uri 활용
                                        String path = uri.getPath();
                                        updateImage(path);
                                    }
                                }).setPreviewMaxCount(100)
                                .create();
                        bottomSheetDialogFragment.show(getSupportFragmentManager());
                    }

                    @Override
                    public void setonCancel() {

                    }
                });
            }
        });

        String ID = SharedManager.getInstance().getString(this, Common.ID);
        CommonUtil.setGlideImage(MainAct.this,Common.BASEFILEURL + ID+".jpg",binding.profileImg);
        binding.memberId.setText(Item.getMEMBER_NICK());
    }

    private void getProfile(){
        String userID = SharedManager.getInstance().getString(this, Common.ID);
        Call<JsonElement> call = RetrofitAdapter.getInstance().getMyPage(userID);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                JsonElement json = response.body();
                if (json != null) {
                    Gson gson = new Gson();
                    String result = json.getAsJsonObject().get("result").getAsString();
                    if (result.equals("1")) {
                        MemberVO Item =  gson.fromJson(json.getAsJsonObject().get("data").getAsJsonObject(), MemberVO.class);
                        setUIMenu(Item);

                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

            }
        });
    }

    private void updateImage(String path){
        final String ID = SharedManager.getInstance().getString(this, Common.ID);

        File file = new File(path);
        MultipartBody.Part body = RetrofitUtil.toRequestMultoPartBody(Common.PIC,file);
        Call<MsgVO> call = RetrofitAdapter.getInstance().updateProfileImage(ID,body);
        call.enqueue(new Callback<MsgVO>() {
            @Override
            public void onResponse(Call<MsgVO> call, retrofit2.Response<MsgVO> response) {
                MsgVO item = response.body();
                if(item != null && item.getResult().equals("1")){
                    // BusProvider.getInstance().post(new RefreshViewEvent());
                   CommonUtil.setGlideImage(MainAct.this,Common.BASEFILEURL + ID+".jpg",binding.profileImg);
                }
            }

            @Override
            public void onFailure(Call<MsgVO> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Instance = null;
        BusProvider.getInstance().unregister(this);
    }

    private void setUpMap(){
       // gMap.setOnMarkerClickListener(markerClick);
        gMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {

                return false;
            }
        });
       gMap.setOnCameraMoveListener(cameraMoveCallback);
       gMap.setOnMapClickListener(mapClick);
       gMap.setOnCameraMoveCanceledListener(moveCanceledListener);
        gMap.setOnCameraMoveStartedListener(moveStartCallback);
        mClsterMa.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<ClusterVO>() {
            @Override
            public boolean onClusterClick(Cluster<ClusterVO> cluster) {
               // Toast.makeText(MainAct.this,"몇개 11" + cluster.getSize(),Toast.LENGTH_SHORT).show();
                Collection<ClusterVO> cluesters = cluster.getItems();
                Iterator<ClusterVO> iter = cluesters.iterator();
                ArrayList<FeedVO.Feed> arraylist = new ArrayList<FeedVO.Feed>();
                do{
                    ClusterVO item = iter.next();
                    arraylist.add(item.getFeed());
                   // Toast.makeText(MainAct.this,"item = " + item.getFeed().getGUBUN(),Toast.LENGTH_SHORT).show();
                }while(iter.hasNext());

                Intent intent = new Intent(MainAct.this,FeedList.class);
                intent.putExtra("arraylist",arraylist);
                //FeedList.setList(arraylist);
               // startActivityForResult(intent,FeedList.REQUEST_FEED);
                startActivity(intent);

                return true;
            }
        });
        mClsterMa.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<ClusterVO>() {
            @Override
            public boolean onClusterItemClick(ClusterVO clusterVO) {
                mClickMarker = clusterVO.getFeed();
                Intent intent = new Intent(MainAct.this, FeedDetailAct.class);
                intent.putExtra("feed",mClickMarker);
                startActivity(intent);

                //Toast.makeText(MainAct.this,"몇개 22" + clusterVO.getPosition(),Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        binding.includeMain.drawer.setOnDrawerScrollListener(new MultiDirectionSlidingDrawer.OnDrawerScrollListener() {
            @Override
            public void onScrollStarted() {

            }

            @Override
            public void onScrollEnded() {
                if(binding.includeMain.drawer.isOpened()){
                    binding.includeMain.drawer.setClickable(false);
                }else{
                    binding.includeMain.drawer.setClickable(true);
                }
            }
        });
       // gMap.setOnCameraIdleListener(idleListener);
        //gMap.setOnCameraMoveStartedListener(moveStartCallback);

    }

    @Subscribe
    public void LoginComple(LoginEvent event){
        MemberVO memberVO = event.getMemberVO();
        SharedManager.getInstance().setString(MainAct.this, Common.ID,memberVO.getMEMBER_ID());
        SharedManager.getInstance().setString(MainAct.this, Common.PASSWORD,memberVO.getPASSWORD());
        SharedManager.getInstance().setString(MainAct.this,Common.MEMBER_TYPE ,memberVO.getMEMBER_TYPE());
        SharedManager.getInstance().setBoolean(MainAct.this,Common.AUTO_LOGIN,true);
        KoreaTown.useLogin = true;
        onResume();
    }

    @Subscribe
    public void refreshView(RefreshViewEvent event){
        String LAT = String.valueOf(KoreaTown.myLocation.getLatitude());
        String LON = String.valueOf(KoreaTown.myLocation.getLongitude());
        getList(LAT,LON);
    }




    @Subscribe
    public void logout(LogoutEvent event){
        // 로그인정보 다삭제
        SharedManager.getInstance().setBoolean(this,Common.AUTO_LOGIN,false);
        SharedManager.getInstance().setString(this,Common.ID,"");
        SharedManager.getInstance().setString(this,Common.PASSWORD,"");
        SharedManager.getInstance().setString(this,Common.MEMBER_TYPE,"");
        KoreaTown.useLogin = false;
        Instance = null;
        Intent intent = new Intent(MainAct.this, SplashActivity.class);
        startActivity(intent);
        finish();
    }

    GoogleMap.OnCameraMoveCanceledListener moveCanceledListener = new GoogleMap.OnCameraMoveCanceledListener() {
        @Override
        public void onCameraMoveCanceled() {
        }
    };
    GoogleMap.OnCameraMoveStartedListener moveStartCallback = new GoogleMap.OnCameraMoveStartedListener() {
        @Override
        public void onCameraMoveStarted(int i) {
            if(!moveStart) {
                moveStart = true;
                CameraPosition position = gMap.getCameraPosition();
                LatLng center = position.target;
                String LAT = String.valueOf(center.latitude);
                String LON = String.valueOf(center.longitude);
                getList(LAT,LON);
            }

          /*  if(markerClickFlag){ //마커가 클릭되있는상태
                hideMarkerDetail();
                markerClickFlag =! markerClickFlag;
                setUI(Item);
                return;
            }
            moveStart = true;*/
        }
    };

    GoogleMap.OnCameraIdleListener idleListener = new GoogleMap.OnCameraIdleListener() {
        @Override
        public void onCameraIdle() {
            if(!markerClickFlag){
                moveStart = false;
                CameraPosition position = gMap.getCameraPosition();
                LatLng center = position.target;
                String LAT = String.valueOf(center.latitude);
                String LON = String.valueOf(center.longitude);
                getList(LAT,LON);
            }
          //  moveStart = false;
        }
    };


    GoogleMap.OnCameraMoveListener cameraMoveCallback = new GoogleMap.OnCameraMoveListener() {
        @Override
        public void onCameraMove() {
        /*   if(moveStart){
                moveStart = false;
                CameraPosition position = gMap.getCameraPosition();
                LatLng center = position.target;
                String LAT = String.valueOf(center.latitude);
                String LON = String.valueOf(center.longitude);
                getList(LAT,LON);
            }
            moveStart = false;*/
           /* if(!moveStart) {
                moveStart = true;
                CameraPosition position = gMap.getCameraPosition();
                LatLng center = position.target;
                String LAT = String.valueOf(center.latitude);
                String LON = String.valueOf(center.longitude);
                getList(LAT,LON);
            }*/

        }
    };

    GoogleMap.OnMapClickListener mapClick = new GoogleMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng latLng) {
            if(markerClickFlag){
                hideMarkerDetail();
                markerClickFlag =! markerClickFlag;
               // setUI(Item);
            }
        }
    };

    public void hideMarkerDetail(){
        menu.findItem(R.id.action_add).setVisible(true);
        menu.findItem(R.id.action_close).setVisible(false);
        binding.includeMain.detailView.setVisibility(View.GONE);
        Animation alphaAni = AnimationUtils.loadAnimation(MainAct.this, R.anim.slide_out_top);
        binding.includeMain.detailView.setAnimation(alphaAni);
        binding.includeMain.toolbar.setBackgroundColor(Utils.getColor(MainAct.this,R.color.colorPrimary));
        //overlay.remove();
        setUI(Item);
    }

    public void setMarkerClick(){
        if(markerClickFlag){ //마커가 클릭되있는상태
            hideMarkerDetail();
            //setUI(Item);
        }else{ // 마커 클릭상태가 아닐때
            getDetail(false);
        }
        markerClickFlag =! markerClickFlag;
    }

  /*  GoogleMap.OnMarkerClickListener markerClick = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            mClickMarker = marker;
            if(markerClickFlag){ //마커가 클릭되있는상태
               // markerClickFlag = false;
                hideMarkerDetail();
                setUI(Item);
            }else{ // 마커 클릭상태가 아닐때
               // markerClickFlag = true;
                getDetail(false);
            }
            markerClickFlag =! markerClickFlag;

            return true;
        }
    };*/

    public void getDetail(boolean flag){
        if(mClickMarker == null){
            return;
        }
        if(mClickMarker.getGUBUN().equals("R")){
            getRoomDetail(flag);
        }else if(mClickMarker.getGUBUN().equals("S")){
            markerClickFlag =! markerClickFlag;
        }
    }

    private void getRoomDetail(final boolean flag){
        Call<JsonElement> call = RetrofitAdapter.getInstance().getRoomDetail(mClickMarker.getSEQ());
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                JsonElement json = response.body();
                if (json != null) {
                    Gson gson = new Gson();
                    String result = json.getAsJsonObject().get("result").getAsString();
                    if (result.equals("1")) {
                        RoomVO item =  gson.fromJson(json, RoomVO.class);
                        setDetailUI(item);
                        if(flag) return;
                        setMapDetailUI();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.e("","");
            }
        });
    }

    public void setDetailUI(final RoomVO roomVO){

        binding.includeMain.detailDate.setText(Utils.CreateDataWithCheck(roomVO.getData().getCREATE_DATE()));
        binding.includeMain.detailTime.setText(getTimeLimit(roomVO.getData().getTIME_MINUTE()));
        binding.includeMain.detailRoomInfo.setText(String.format("%s room , %s bathroom",roomVO.getData().getRM_ROOM(),roomVO.getData().getRM_TOILET()));
        binding.includeMain.detailRoomAddr.setText(String.format("%s , %s",roomVO.getData().getRM_ADDR2(),roomVO.getData().getRM_ADDR1()));
        binding.includeMain.content.detailDesc.setText(roomVO.getData().getRM_DESC());
        CommonUtil.setGlideImage(MainAct.this,Common.BASEFILEURL + roomVO.getData().getMEMBER_ID()+".jpg", binding.includeMain.profilePhoto);

        if(roomVO.getData().getFILE_LIST() != null && roomVO.getData().getFILE_LIST().size() > 0){
            binding.includeMain.content.pager.setVisibility(View.VISIBLE);
            ImagePagerAdapter adapter = new ImagePagerAdapter(MainAct.this,getLayoutInflater(),roomVO.getData().getFILE_LIST());
            binding.includeMain.content.pager.setPageMargin(getResources().getDisplayMetrics().widthPixels / -5);
            binding.includeMain.content.pager.setOffscreenPageLimit(2);
            binding.includeMain.content.pager.setAdapter(adapter);
        }else{
            binding.includeMain.content.pager.setVisibility(View.GONE);
        }


        binding.includeMain.content.detailContentPrice.setText(String.format("-금액 : $%s (Mon)",roomVO.getData().getRM_PRICE()));
        binding.includeMain.content.detailContentRoom.setText(String.format("-방 : %s개",roomVO.getData().getRM_ROOM()));
        binding.includeMain.content.detailContentBath.setText(String.format("-욕실 : %s개",roomVO.getData().getRM_TOILET()));
        binding.includeMain.content.detailContentCnt.setText(String.format("Comment (%d)",roomVO.getData().getCOMMENT_LIST().size()));

        if(roomVO.getData().getCOMMENT_LIST() != null && roomVO.getData().getCOMMENT_LIST().size() > 0){
            binding.includeMain.content.detailContentRecyler.setVisibility(View.VISIBLE);
            LinearLayoutManager manager = new LinearLayoutManager(this);
            binding.includeMain.content.detailContentRecyler.setHasFixedSize(true);
            binding.includeMain.content.detailContentRecyler.setLayoutManager(manager);
            binding.includeMain.content.detailContentRecyler.setAdapter(new RecyclerAdapter(this, roomVO.getData().getCOMMENT_LIST(), R.layout.mainact));
        }else{
            binding.includeMain.content.detailContentRecyler.setVisibility(View.GONE);
        }

        binding.includeMain.content.detailContentCmtbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!KoreaTown.useLogin){
                    CommonUtil.showTwoBtnDialog(MainAct.this, "로그인", "로그인이 필요합니다. 로그인페이지로 이동하시겠습니까?", new CommonUtil.onDialogClick() {
                        @Override
                        public void setonConfirm() {
                            Intent intent = new Intent(MainAct.this, LoginAct.class);
                            startActivity(intent);
                        }

                        @Override
                        public void setonCancel() {

                        }
                    });
                    return;
                }
                Intent intent = new Intent(MainAct.this, CommentPopup.class);
                intent.putExtra(CommentPopup.GUBUN,"R");
                intent.putExtra(CommentPopup.SEQ,roomVO.getData().getRM_SEQ());
                startActivity(intent);
            }
        });

    }

    private void setMapDetailUI(){
        LatLng position = new LatLng(Double.valueOf(mClickMarker.getLAT()),Double.valueOf(mClickMarker.getLON()));
        CameraUpdate center = CameraUpdateFactory.newLatLng(position);
        gMap.clear();
        mClsterMa.clearItems();
        mClsterMa.setRenderer(mcr);
        binding.includeMain.detailView.setVisibility(View.VISIBLE);
        binding.includeMain.drawer.close();

        menu.findItem(R.id.action_add).setVisible(false);
        menu.findItem(R.id.action_close).setVisible(true);
        Animation alphaAni = AnimationUtils.loadAnimation(MainAct.this, R.anim.slide_in_top);
        binding.includeMain.detailView.setAnimation(alphaAni);
        binding.includeMain.toolbar.setBackgroundColor(Utils.getColor(MainAct.this,R.color.colorToolbar));
        addOverLayView();
        gMap.animateCamera(center);
    }
    public String getTimeLimit(String time){
        int limitTime = Integer.valueOf(time);
        if(limitTime > 60){
            int timeHour = limitTime / 60 ;
            int timeMinute = limitTime % 60;
            return String.format("%d시간 %d분 후에 사라집니다.",timeHour,timeMinute);
        } else {
            return String.format("%d분 후에 사라집니다.",limitTime);
        }

    }

    public void addOverLayView(){
        LatLng position = new LatLng(Double.valueOf(mClickMarker.getLAT()),Double.valueOf(mClickMarker.getLON()));
        Bitmap bitmap =  Bitmap.createBitmap(100,100,Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(Color.parseColor("#66000000"));

        ClusterVO items = new ClusterVO();
        items.setLocation(position);
        items.setFeed(mClickMarker);
        mClsterMa.addItem(items);
        mClsterMa.cluster();
        //gMap.addMarker(new MarkerOptions().position(position).icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, marker_root_view,mClickMarker.getGubun(),mClickMarker.getCnt()))));
        //overlay = gMap.addGroundOverlay(new GroundOverlayOptions().image(BitmapDescriptorFactory.fromBitmap(bitmap)).position(position,8600f, 6500f));
        CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(position, (float)18.0);
        gMap.moveCamera(cu);
    }

    MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {
        @Override
        public void gotLocation(Location location) {
           // String msg = "lon: "+location.getLongitude()+" -- lat: "+location.getLatitude();
           // Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            if(location != null){
                if( KoreaTown.myLocation == null){
                    KoreaTown.myLocation = location;
                    moveCamera();
                }else{
                    KoreaTown.myLocation = location;
                }
            }
           /* if(location == null){
                MyLocation location2 = new MyLocation();
                location2.getLocation(MainAct.this,locationResult);
            }else{
                KoreaTown.myLocation = location;
                String LAT = String.valueOf(KoreaTown.myLocation.getLatitude());
                String LON = String.valueOf(KoreaTown.myLocation.getLongitude());
                getList(LAT,LON);
            }*/
        }
    };

    private void moveCamera(){
        String LAT = String.valueOf(KoreaTown.myLocation.getLatitude());
        String LON = String.valueOf(KoreaTown.myLocation.getLongitude());
        LatLng latlng = new LatLng(KoreaTown.myLocation.getLatitude(),KoreaTown.myLocation.getLongitude());
        CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(latlng, (float)13.0);
        gMap.moveCamera(cu);
        getList(LAT,LON);
    }

    private void drawMarker(FeedVO.Feed item){
        double lat = Double.valueOf(item.getLAT());
        double lon = Double.valueOf(item.getLON());
        LatLng currentGeo = new LatLng(lat, lon);
        ClusterVO items = new ClusterVO();
        items.setLocation(currentGeo);
        items.setFeed(item);
        mClsterMa.addItem(items);
       /* gMap.addMarker(
                new MarkerOptions()
                        .position(currentGeo)
                        .title(getJsonObjectItem(item))
                        .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, marker_root_view,item.getGUBUN(),item.getCOMMENT_CNT())))
                        .flat(true)
        );*/
    }


    private String getJsonObjectItem(FeedVO.Feed item){
        JSONObject obj = new JSONObject();
        try {
            obj.put("SEQ",item.getSEQ());
            obj.put("GUBUN",item.getGUBUN());
            obj.put("MEMBER_ID",item.getMEMBER_ID());
            obj.put("LAT",item.getLAT());
            obj.put("LON",item.getLON());
            obj.put("COMMENT_CNT",item.getCOMMENT_CNT());
        }catch(Exception e){
            e.printStackTrace();
        }
        return obj.toString();
    }

    ClusterManager<ClusterVO> mClsterMa = null;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (gMap == null) {
            gMap = googleMap;
            mClsterMa = new ClusterManager<>(this,gMap);
            mcr = new MyClusterRenderer(this,gMap,mClsterMa);
            mcr.setClusterView(LayoutInflater.from(this).inflate(R.layout.cluster, null));
            gMap.setOnCameraIdleListener(mClsterMa);
            gMap.setOnMarkerClickListener(mClsterMa);
            gMap.setMyLocationEnabled(true);
            gMap.setIndoorEnabled(true);
            gMap.setBuildingsEnabled(true);
            gMap.setTrafficEnabled(true);
            UiSettings set = gMap.getUiSettings();
            set.setCompassEnabled(true);
            gMap.getUiSettings().setRotateGesturesEnabled(true);
            mClsterMa.setRenderer(mcr);
            setUpMap();
        }


      /*  if(KoreaTown.myLocation == null){
            MyLocation location = new MyLocation();
            location.getLocation(MainAct.this,locationResult);
        }else{*/
            //drawMarker(KoreaTown.myLocation);
            MyLocation location = new MyLocation();
            location.getLocation(MainAct.this,locationResult);
            String LAT = String.valueOf(KoreaTown.myLocation.getLatitude());
            String LON = String.valueOf(KoreaTown.myLocation.getLongitude());
            LatLng latlng = new LatLng(KoreaTown.myLocation.getLatitude(),KoreaTown.myLocation.getLongitude());
            CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(latlng, (float)13.0);
            gMap.moveCamera(cu);
            getList(LAT,LON);
      //  }
    }

    private synchronized void getList(String LAT,String LON){
       // progressOn(binding.includeMain.progressWheel);
        Call<JsonElement> call = RetrofitAdapter.getInstance().getMapList(LAT,LON);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
              //  progressOff(biRnding.includeMain.progressWheel);
                JsonElement json = response.body();
                if (json != null) {
                    Gson gson = new Gson();
                    String result = json.getAsJsonObject().get("result").getAsString();
                    if (result.equals("1")) {
                        Item =  gson.fromJson(json, FeedVO.class);
                        setUI(Item);
                    }
                }
                moveStart = false;
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.e("","");
               // progressOff(binding.includeMain.progressWheel);
                moveStart = false;
            }
        });
    }

    private void setUI(FeedVO item){

        if(item == null ) return;
        gMap.clear();
        mClsterMa.clearItems();
        ArrayList<FeedVO.Feed> list = item.getData();
        for(int i = 0 ; i < list.size(); i++){
            //ClusterVO vo = new ClusterVO();
            //vo.setLocation(new LatLng(Double.valueOf(list.get(i).getLAT()),Double.valueOf(list.get(i).getLON())));
          //  mClusterManage.addItem(list.get(i));
            drawMarker(list.get(i));
        }

        mClsterMa.cluster();

    }

    private Bitmap createDrawableFromView(Context context, View view,String gubun,String cnt) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        ImageView markerIcon = (ImageView)view.findViewById(R.id.img_marker);
        if(gubun.equals("R")){ // 방
            markerIcon.setImageResource(R.drawable.icon_house);
        }else if(gubun.equals("S")){ // 소식
            markerIcon.setImageResource(R.drawable.icon_news);
        }

        TextView cntView = (TextView)view.findViewById(R.id.cnt);
        if(cnt == null || cnt.length() == 0 || cnt.equals("0")){
            cntView.setVisibility(View.GONE);
        }else{
            cntView.setVisibility(View.VISIBLE);
            cntView.setText(cnt);
        }

        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)); view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels); view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels); view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.messenger, menu);
        //return true;
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(MainAct.this, SelectAct.class);
                startActivityForResult(intent,5502);
                break;
            case R.id.action_close:
                hideMarkerDetail();
                markerClickFlag =! markerClickFlag;
                break;
        }
        return super.onOptionsItemSelected(item);

    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case 5502:
                if(resultCode == RESULT_OK){
                    String result = data.getStringExtra("ADD_FLAG");
                    if(result.equals("1")){
                        Intent intent = new Intent(MainAct.this, RoomAct.class);
                        startActivity(intent);
                    }else{
                        Intent intent = new Intent(MainAct.this, StoryAct.class);
                        startActivity(intent);
                    }
                }
                break;
            case FeedList.REQUEST_FEED:
                if(resultCode == RESULT_OK){
                    mClickMarker = (FeedVO.Feed)data.getSerializableExtra("feed");
                    Intent intent = new Intent(MainAct.this, FeedDetailAct.class);
                    intent.putExtra("feed",mClickMarker);
                    startActivity(intent);
                    //setMarkerClick();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        }else{

            if(markerClickFlag){ //마커가 클릭되있는상태
                // markerClickFlag = false;
                hideMarkerDetail();
                //setUI(Item);
                markerClickFlag =! markerClickFlag;
            }else{
                CommonUtil.ShowExitToast(this);
            }
        }
    }


    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
        Context context;
        ArrayList<RoomVO.Room.Comment> items;
        int item_layout;
        public RecyclerAdapter(Context context, ArrayList<RoomVO.Room.Comment> items, int item_layout) {
            this.context=context;
            this.items=items;
            this.item_layout=item_layout;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, null);
            return new ViewHolder(v);
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onBindViewHolder(final ViewHolder holder,final int position) {
            RoomVO.Room.Comment item = items.get(position);
            holder.itemBinding.setComment(item);
            CommonUtil.setGlideImage(MainAct.this,Common.BASEFILEURL + item.getMEMBER_ID() +".jpg", holder.itemBinding.itemProfileImg);
        }



        @Override
        public int getItemCount() {
            return this.items.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {

            CommentItemBinding itemBinding = null;

            public ViewHolder(View itemView) {
                super(itemView);

                itemBinding = DataBindingUtil.bind(itemView);

            }
        }
    }

}
