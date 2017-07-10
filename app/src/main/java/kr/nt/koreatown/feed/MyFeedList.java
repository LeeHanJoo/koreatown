package kr.nt.koreatown.feed;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.ArrayList;

import kr.nt.koreatown.Common;
import kr.nt.koreatown.R;
import kr.nt.koreatown.databinding.FeedItemBinding;
import kr.nt.koreatown.databinding.MyfeedlistBinding;
import kr.nt.koreatown.main.BaseActivity;
import kr.nt.koreatown.retrofit.RetrofitAdapter;
import kr.nt.koreatown.util.CommonUtil;
import kr.nt.koreatown.util.SharedManager;
import kr.nt.koreatown.util.Utils;
import kr.nt.koreatown.vo.FeedVO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by user on 2017-06-07.
 */

public class MyFeedList extends BaseActivity {

    MyfeedlistBinding binding = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.myfeedlist);

        binding.toolbar.setTitle("내가 작성한글");
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        binding.toolbar.setBackgroundColor(Utils.getColor(MyFeedList.this,R.color.colorToolbar));

        //getList();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getList();
    }

    public void setUI(FeedVO item){
        LinearLayoutManager manager = new LinearLayoutManager(this);
        binding.recylerview.setHasFixedSize(true);
        binding.recylerview.setLayoutManager(manager);
        binding.recylerview.setAdapter(new RecyclerAdapter(this, item.getData(), R.layout.myfeedlist));
        binding.itemCnt.setText(String.valueOf(item.getData().size()));
    }

    public void getList(){
        progressOn(binding.progressWheel);

        String ID = SharedManager.getInstance().getString(this, Common.ID);

        Call<JsonElement> call = RetrofitAdapter.getInstance().getMyList(ID);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                progressOff(binding.progressWheel);
                JsonElement json = response.body();
                if (json != null) {
                    Gson gson = new Gson();
                    String result = json.getAsJsonObject().get("result").getAsString();
                    if (result.equals("1")) {
                        FeedVO item =  gson.fromJson(json, FeedVO.class);
                        setUI(item);
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressOff(binding.progressWheel);
                Log.e("","");
            }
        });
    }


    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
        Context context;
        ArrayList<FeedVO.Feed> items;
        int item_layout;
        public RecyclerAdapter(Context context, ArrayList<FeedVO.Feed> items, int item_layout) {
            this.context=context;
            this.items=items;
            this.item_layout=item_layout;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_item, null);
            return new ViewHolder(v);
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onBindViewHolder(final ViewHolder holder,final int position) {
            final FeedVO.Feed item = items.get(position);
            if(item.getGUBUN().equals("R")){
                holder.itemBinding.detailIcon.setImageResource(R.drawable.icon_house_wh);
            }else{
                holder.itemBinding.detailIcon.setImageResource(R.drawable.icon_news_wh);
            }
            holder.itemBinding.detailDate.setText(Utils.CreateDataWithCheck(item.getCREATE_DATE()));
            holder.itemBinding.detailTime.setText(getTimeLimit(item.getTIME_MINUTE()));
            holder.itemBinding.detailRoomAddr.setText(String.format("%s , %s",item.getADDR2(),item.getADDR1()));


            if(item.getGUBUN().equals("R")){
                String[] info = item.getDESCS().split("-");
                holder.itemBinding.detailRoomInfo.setText(String.format("%s room , %s bathroom",info[0],info[1]));
            }else{
                holder.itemBinding.detailRoomInfo.setText(item.getDESCS());
            }

            CommonUtil.setGlideImage(MyFeedList.this, Common.BASEFILEURL + item.getMEMBER_ID()+".jpg", holder.itemBinding.profilePhoto);

         /*   if(item.getCOMMENT_CNT() == null || item.getCOMMENT_CNT().length() == 0 || item.getCOMMENT_CNT().equals("0")){
                holder.itemBinding.cnt.setVisibility(View.GONE);
            }else{
                holder.itemBinding.cnt.setVisibility(View.VISIBLE);
                holder.itemBinding.cnt.setText(item.getCOMMENT_CNT());
            }*/

           // holder.itemBinding.itemWriter.setText(item.getMEMBER_ID());

            holder.itemBinding.clickDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MyFeedDetailAct.class);
                    intent.putExtra("feed",item);
                    startActivity(intent);
                   // Intent intent = new Intent();
                   // intent.putExtra("feed",item);
                   // setResult(RESULT_OK,intent);
                   // finish();
                }
            });
            //RoomVO.Room.Comment item = items.get(position);
           // holder.itemBinding.setComment(item);
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

        @Override
        public int getItemCount() {
            return this.items.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {

            FeedItemBinding itemBinding = null;

            public ViewHolder(View itemView) {
                super(itemView);

                itemBinding = DataBindingUtil.bind(itemView);

            }
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
