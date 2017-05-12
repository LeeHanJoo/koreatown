package kr.nt.koreatown.feed;

import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import kr.nt.koreatown.Common;
import kr.nt.koreatown.KoreaTown;
import kr.nt.koreatown.R;
import kr.nt.koreatown.bus.BusProvider;
import kr.nt.koreatown.bus.RefreshViewEvent;
import kr.nt.koreatown.databinding.StoryactBinding;
import kr.nt.koreatown.retrofit.RetrofitAdapter;
import kr.nt.koreatown.retrofit.RetrofitUtil;
import kr.nt.koreatown.util.CommonUtil;
import kr.nt.koreatown.util.SharedManager;
import kr.nt.koreatown.vo.MsgVO;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by user on 2017-04-27.
 */

public class StoryAct extends AppCompatActivity {

    private StoryactBinding binding = null;
    private int mSticker = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.storyact);

        binding.toolbar.setTitle("소식등록");
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initListener();
    }

    private void initListener(){
        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validation();
            }
        });
        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch(checkedId){
                    case R.id.sticker_1:
                        mSticker = 1;
                        break;
                    case R.id.sticker_2:
                        mSticker = 2;
                        break;
                    case R.id.sticker_3:
                        mSticker = 3;
                        break;
                    case R.id.sticker_4:
                        mSticker = 4;
                        break;

                }
            }
        });
    }

    private void validation(){
        String story = binding.storyEdit.getText().toString().trim();

        if(story.isEmpty() || story.length() == 0){
            binding.storyEdit.hasFocus();
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            binding.storyEdit.startAnimation(shake);
            Toast.makeText(this,"내용을 입력해주세요.",Toast.LENGTH_SHORT).show();
            return;
        }

        if(mSticker == 0){
            Toast.makeText(this,"스티커를 선택해주세요.",Toast.LENGTH_SHORT).show();
            return;
        }

        /*MyLocation location = new MyLocation();
        location.getLocation(MainAct.this,locationResult);*/

        new SendStoryTask(story).execute();

       // RetrofitAdapter.getInstance().postStory()

    }

    private void SendStory(String story,String addr1,String addr2){
        String ID = SharedManager.getInstance().getString(this, Common.ID);
        Map<String, RequestBody> params = new HashMap<String, RequestBody>();
        params.put(Common.ID, RetrofitUtil.toRequestBody(ID));
        params.put(Common.STICKER, RetrofitUtil.toRequestBody(String.valueOf(mSticker)));
        params.put(Common.DESC, RetrofitUtil.toRequestBody(story));

        params.put(Common.LAT, RetrofitUtil.toRequestBody(String.valueOf(KoreaTown.myLocation.getLatitude())));
        params.put(Common.LON, RetrofitUtil.toRequestBody(String.valueOf(KoreaTown.myLocation.getLongitude())));
        params.put(Common.ADDR1, RetrofitUtil.toRequestBody(addr1));
        params.put(Common.ADDR2, RetrofitUtil.toRequestBody(addr2));

        Call<MsgVO> call = RetrofitAdapter.getInstance().postStory(params);
        call.enqueue(new Callback<MsgVO>() {
            @Override
            public void onResponse(Call<MsgVO> call, retrofit2.Response<MsgVO> response) {
                MsgVO item = response.body();
                if(item != null && item.getResult().equals("1")){
                    BusProvider.getInstance().post(new RefreshViewEvent());
                    CommonUtil.showOnBtnDialog(StoryAct.this, "소식등록", "소식등록이 완료되었습니다.", new CommonUtil.onDialogClick() {
                        @Override
                        public void setonConfirm() {
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

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private class SendStoryTask extends AsyncTask<Void,Void,Void>{

        private String story ;
        public SendStoryTask(String story){
            this.story = story;
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

                SendStory(story,addr1,addr2);
            }catch(Exception e){
                e.printStackTrace();
            }

            return null;
        }
    }

}
