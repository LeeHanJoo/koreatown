package kr.nt.koreatown.setting;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.HashMap;
import java.util.Map;

import kr.nt.koreatown.Common;
import kr.nt.koreatown.R;
import kr.nt.koreatown.databinding.ProfileactBinding;
import kr.nt.koreatown.retrofit.RetrofitAdapter;
import kr.nt.koreatown.retrofit.RetrofitUtil;
import kr.nt.koreatown.util.SharedManager;
import kr.nt.koreatown.vo.MemberVO;
import kr.nt.koreatown.vo.MsgVO;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by user on 2017-05-10.
 */

public class MyProfileAct extends AppCompatActivity {

    private ProfileactBinding binding = null;
    public Menu menu;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.profileact);

        binding.toolbar.setTitle("내 정보");
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // ((LinearLayout.LayoutParams) binding.viewDetail.getLayoutParams()).topMargin = 1 + -633;
    //    binding.myinfoLayout.setOnClickListener(this);
    //    binding.logoutLayout.setOnClickListener(this);

        getProfile();
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
                        setUI(Item);
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

            }
        });
    }

    private void setUI(MemberVO Item ){
        binding.setUser(Item);
    }

    private void updateProfile(){
        String ID = SharedManager.getInstance().getString(this, Common.ID);
        Map<String, RequestBody> params = new HashMap<String, RequestBody>();
        params.put(Common.ID, RetrofitUtil.toRequestBody(ID));
        params.put("USER_NAME",RetrofitUtil.toRequestBody(binding.profileName.getText().toString().trim()));
        params.put(Common.EMAIL,RetrofitUtil.toRequestBody(binding.profileEmail.getText().toString().trim()));
        params.put(Common.BIRTHDAY,RetrofitUtil.toRequestBody(binding.profileBirth.getText().toString().trim()));
        params.put(Common.SEX,RetrofitUtil.toRequestBody(getSexConvert(binding.profileSex.getText().toString().trim())));
       // params.put(Common.LON,RetrofitUtil.toRequestBody(String.valueOf(KoreaTown.myLocation.getLongitude())));

        Call<MsgVO> call = RetrofitAdapter.getInstance().updateMyPage(params);
        call.enqueue(new Callback<MsgVO>() {
            @Override
            public void onResponse(Call<MsgVO> call, Response<MsgVO> response) {
                MsgVO item = response.body();
                if(item != null ) {
                    if (item.getResult().equals("1")) { // 성공
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<MsgVO> call, Throwable t) {

            }
        });
    }

    private String getSexConvert(String sex){

        if(sex.equals("남")){
            return "M";
        }else if(sex.equals("여")){
            return "W";
        }
        return "";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.update, menu);
        //return true;
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_update:
                updateProfile();
                break;
        }
        return super.onOptionsItemSelected(item);

    };
}
