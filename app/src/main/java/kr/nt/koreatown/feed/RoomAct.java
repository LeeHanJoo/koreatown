package kr.nt.koreatown.feed;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gun0912.tedbottompicker.TedBottomPicker;
import kr.nt.koreatown.Common;
import kr.nt.koreatown.KoreaTown;
import kr.nt.koreatown.R;
import kr.nt.koreatown.bus.BusProvider;
import kr.nt.koreatown.bus.RefreshViewEvent;
import kr.nt.koreatown.databinding.RoomactBinding;
import kr.nt.koreatown.retrofit.RetrofitAdapter;
import kr.nt.koreatown.retrofit.RetrofitUtil;
import kr.nt.koreatown.util.CommonUtil;
import kr.nt.koreatown.util.SharedManager;
import kr.nt.koreatown.util.Utils;
import kr.nt.koreatown.vo.MsgVO;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by user on 2017-04-27.
 */

public class RoomAct extends AppCompatActivity implements View.OnClickListener{

    private RoomactBinding binding = null;
    private int mRoom = 0 , mBath = 0;
    private ArrayList<String> fileArr = new ArrayList<>();
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
        initListener();
    }

    private void initListener(){
        binding.visibleDetailBtn.setOnClickListener(this);
        binding.addimage.setOnClickListener(this);
        binding.btnSend.setOnClickListener(this);
        binding.radioRoom.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch(checkedId){
                    case R.id.room_1:
                        mRoom = 1;
                        break;
                    case R.id.room_2:
                        mRoom = 2;
                        break;
                    case R.id.room_3:
                        mRoom = 3;
                        break;
                    case R.id.room_4:
                        mRoom = 4;
                        break;
                    case R.id.room_5:
                        mRoom = 5;
                        break;
                    case R.id.room_6:
                        mRoom = 6;
                        break;
                }
            }
        });
        binding.radioBath.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch(checkedId){
                    case R.id.bath_1:
                        mBath = 1;
                        break;
                    case R.id.bath_2:
                        mBath = 2;
                        break;
                    case R.id.bath_3:
                        mBath = 3;
                        break;
                }
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
       // BusProvider.getInstance().post(new RefreshViewEvent());
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.visible_detail_btn:
                if(binding.viewDetail.getVisibility() == View.VISIBLE){
                    binding.viewDetail.setVisibility(View.GONE);
                    binding.visibleDetailBtn.setImageResource(R.drawable.bar_advance_up);
                }else{
                    binding.visibleDetailBtn.setImageResource(R.drawable.bar_advance_down);
                    binding.viewDetail.setVisibility(View.VISIBLE);
                }
                scrollToEnd();
                break;
            case R.id.addimage:
                AddImage();
                break;
            case R.id.btn_send:
                validation();
                break;
        }
    }

    private void validation(){
            if(mRoom == 0){
                Toast.makeText(this,"방 갯수를 선택해주세요.",Toast.LENGTH_SHORT).show();
                return;
            }
            if(mBath == 0){
                Toast.makeText(this,"욕실 갯수를 선택해주세요.",Toast.LENGTH_SHORT).show();
                return;
            }

            String price = binding.inputPrice.getText().toString().trim();
            if(price.isEmpty() || price.length() == 0){
                binding.inputPrice.hasFocus();
                Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
                binding.inputPrice.startAnimation(shake);
                Toast.makeText(this,"금액을 입력해주세요.",Toast.LENGTH_SHORT).show();
                return;
           }

            String desc = binding.inputDesc.getText().toString().trim();
            if(desc.isEmpty() || desc.length() == 0){
                binding.inputDesc.hasFocus();
                Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
                binding.inputDesc.startAnimation(shake);
                Toast.makeText(this,"상세설명을 입력해주세요.",Toast.LENGTH_SHORT).show();
                return;
            }


        new SendRoomTask(price,desc).execute();
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
                        params.rightMargin = Utils.toPixel(RoomAct.this,5);
                        imageview.setLayoutParams(params);
                        String path = uri.getPath();
                        fileArr.add(path);
                        Glide.with(RoomAct.this).load("file://" + uri).into(imageview);
                        binding.addimagelayout.addView(imageview,0);
                        //binding.addimagelayout.addView(imageview);

                    }
                }).setMaxCount(1000)
                .create();

        bottomSheetDialogFragment.show(getSupportFragmentManager());

    }

    private void SendStory(String price, String desc,String addr1,String addr2){
        String ID = SharedManager.getInstance().getString(this, Common.ID);
        Map<String, RequestBody> params = new HashMap<String, RequestBody>();
        params.put(Common.ID, RetrofitUtil.toRequestBody(ID));
        params.put(Common.PRICE, RetrofitUtil.toRequestBody(price));
        params.put(Common.ROOM, RetrofitUtil.toRequestBody(String.valueOf(mRoom)));
        params.put(Common.TOILET, RetrofitUtil.toRequestBody(String.valueOf(mBath)));
        params.put(Common.DESC, RetrofitUtil.toRequestBody(desc));

        params.put(Common.LAT, RetrofitUtil.toRequestBody(String.valueOf(KoreaTown.myLocation.getLatitude())));
        params.put(Common.LON, RetrofitUtil.toRequestBody(String.valueOf(KoreaTown.myLocation.getLongitude())));
        params.put(Common.ADDR1, RetrofitUtil.toRequestBody(addr1));
        params.put(Common.ADDR2, RetrofitUtil.toRequestBody(addr2));
        params.put(Common.COSTDAY, RetrofitUtil.toRequestBody("24"));

        List<MultipartBody.Part> photos = new ArrayList<>();
        if(fileArr.size() > 0){
            for(int i = 0 ; i < fileArr.size(); i++){
                File file = new File(fileArr.get(i));
                MultipartBody.Part body = RetrofitUtil.toRequestMultoPartBody(Common.PIC_ARR,file);
                photos.add(body);
            }
        }


        Call<MsgVO> call2 = RetrofitAdapter.getInstance().postRoom(params,photos);
        call2.enqueue(new Callback<MsgVO>() {
            @Override
            public void onResponse(Call<MsgVO> call, retrofit2.Response<MsgVO> response) {
                MsgVO item = response.body();
                if(item != null && item.getResult().equals("1")){
                   // BusProvider.getInstance().post(new RefreshViewEvent());
                    CommonUtil.showOnBtnDialog(RoomAct.this, "방등록", "방등록이 완료되었습니다.", new CommonUtil.onDialogClick() {
                        @Override
                        public void setonConfirm() {
                            BusProvider.getInstance().post(new RefreshViewEvent());
                            finish();
                        }

                        @Override
                        public void setonCancel() {

                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<MsgVO> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }



    private class SendRoomTask extends AsyncTask<Void,Void,Void> {

        private String price ;
        private String desc;
        public SendRoomTask(String price,String desc){
            this.price = price;
            this.desc = desc;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            String addr1 = "";
            String addr2 = "";
            String addrURL = String.format("http://maps.googleapis.com/maps/api/geocode/json?latlng=%s,%s&language=ko&sensor=false",String.valueOf(KoreaTown.myLocation.getLatitude()),String.valueOf(KoreaTown.myLocation.getLongitude()));

            Request request = new Request.Builder()
                    .url(addrURL)
                    .build();
            try {
                Response response = new OkHttpClient().newCall(request).execute();
                String respon =  response.body().string();

                JSONObject obj = new JSONObject(respon);
                JSONArray array = obj.getJSONArray("results").getJSONObject(0).getJSONArray("address_components");

                for(int i = 0 ; i < 5 ;i++){
                    String addr = array.getJSONObject(i).getString("long_name");
                    if(i < 2){
                        addr2 = addr +" " +  addr2;
                    }else{
                        addr1 = addr +" " + addr1;
                    }
                }

                SendStory(price,desc,addr1,addr2);
            }catch(Exception e){
                e.printStackTrace();
            }

            return null;
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
