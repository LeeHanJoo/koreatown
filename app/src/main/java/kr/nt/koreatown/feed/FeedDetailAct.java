package kr.nt.koreatown.feed;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import kr.nt.koreatown.Common;
import kr.nt.koreatown.KoreaTown;
import kr.nt.koreatown.R;
import kr.nt.koreatown.bus.BusProvider;
import kr.nt.koreatown.bus.RefreshDetailViewEvent;
import kr.nt.koreatown.databinding.CommentItemBinding;
import kr.nt.koreatown.databinding.CommentItemStoryBinding;
import kr.nt.koreatown.databinding.FeeddetailBinding;
import kr.nt.koreatown.intro.LoginAct;
import kr.nt.koreatown.retrofit.RetrofitAdapter;
import kr.nt.koreatown.util.CommonUtil;
import kr.nt.koreatown.util.Utils;
import kr.nt.koreatown.view.ImagePagerAdapter;
import kr.nt.koreatown.vo.FeedVO;
import kr.nt.koreatown.vo.RoomVO;
import kr.nt.koreatown.vo.StoryVO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by user on 2017-06-07.
 */

public class FeedDetailAct extends AppCompatActivity {

    FeeddetailBinding binding = null;
    FeedVO.Feed item = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.feeddetail);

        binding.toolbar.setTitle("상세정보");
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        binding.toolbar.setBackgroundColor(Utils.getColor(FeedDetailAct.this,R.color.colorToolbar));
        item = (FeedVO.Feed)getIntent().getSerializableExtra("feed");
        BusProvider.getInstance().register(this);
        if(item.getGUBUN().equals("R")){
            getRoomDetail(false);
        }else{
            getStoryDetail();

        }

    }

    private void getStoryDetail(){
        Call<JsonElement> call = RetrofitAdapter.getInstance().getStoryDetail(item.getSEQ());
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                JsonElement json = response.body();
                if (json != null) {
                    Gson gson = new Gson();
                    String result = json.getAsJsonObject().get("result").getAsString();
                    if (result.equals("1")) {
                        StoryVO item =  gson.fromJson(json, StoryVO.class);
                        setDetailUIStory(item);

                        //setMapDetailUI();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.e("","");
            }
        });
    }


    private void getRoomDetail(final boolean flag){
        Call<JsonElement> call = RetrofitAdapter.getInstance().getRoomDetail(item.getSEQ());
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
                        //setMapDetailUI();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.e("","");
            }
        });
    }

    @Subscribe
    public void refreshDetailView(RefreshDetailViewEvent event){
        if(item.getGUBUN().equals("R")){
            getRoomDetail(false);
        }else{
            getStoryDetail();

        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
    }

    private void setSticker(String sticker){
        if(sticker.equals("1")){
            binding.detailSticker.setImageResource(R.drawable.emoticon_01_on);
        }else  if(sticker.equals("2")){
            binding.detailSticker.setImageResource(R.drawable.emoticon_02_on);
        } if(sticker.equals("3")){
            binding.detailSticker.setImageResource(R.drawable.emoticon_03_on);
        } if(sticker.equals("4")){
            binding.detailSticker.setImageResource(R.drawable.emoticon_04_on);
        }
    }

    public void setDetailUIStory(final StoryVO storyVO){
        binding.detailIcon.setImageResource(R.drawable.icon_news_wh);
        binding.detailDate.setText(Utils.CreateDataWithCheck(storyVO.getData().getCREATE_DATE()));
        binding.detailTime.setText(getTimeLimit(storyVO.getData().getTIME_MINUTE()));
        binding.detailRoomInfo.setText(storyVO.getData().getST_DESC());
        binding.detailRoomAddr.setText(String.format("%s , %s",storyVO.getData().getST_ADDR1(),storyVO.getData().getST_ADDR2()));
        binding.detailDesc.setText(storyVO.getData().getST_DESC());
        CommonUtil.setGlideImage(FeedDetailAct.this, Common.BASEFILEURL + storyVO.getData().getMEMBER_ID()+".jpg", binding.profilePhoto);

        setSticker(storyVO.getData().getST_STICKER());

        binding.pager.setVisibility(View.GONE);
        binding.detailContentPrice.setVisibility(View.GONE);
        binding.detailContentRoom.setVisibility(View.GONE);
        binding.detailContentBath.setVisibility(View.GONE);
        binding.detailContentCnt.setText(String.format("Comment (%d)",storyVO.getData().getCOMMENT_LIST().size()));

        if(storyVO.getData().getCOMMENT_LIST() != null && storyVO.getData().getCOMMENT_LIST().size() > 0){
            binding.detailContentRecyler.setVisibility(View.VISIBLE);
            LinearLayoutManager manager = new LinearLayoutManager(this);
            binding.detailContentRecyler.setHasFixedSize(true);
            binding.detailContentRecyler.setLayoutManager(manager);
            binding.detailContentRecyler.setAdapter(new RecyclerAdapter2(this, storyVO.getData().getCOMMENT_LIST(), R.layout.mainact));
        }else{
            binding.detailContentRecyler.setVisibility(View.GONE);
        }

        binding.detailContentCmtbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!KoreaTown.useLogin){
                    CommonUtil.showTwoBtnDialog(FeedDetailAct.this, "로그인", "로그인이 필요합니다. 로그인페이지로 이동하시겠습니까?", new CommonUtil.onDialogClick() {
                        @Override
                        public void setonConfirm() {
                            Intent intent = new Intent(FeedDetailAct.this, LoginAct.class);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void setonCancel() {

                        }
                    });
                    return;
                }

                Intent intent = new Intent(FeedDetailAct.this, CommentPopup.class);
                intent.putExtra(CommentPopup.GUBUN,"S");
                intent.putExtra(CommentPopup.SEQ,storyVO.getData().getST_SEQ());
                startActivity(intent);
            }
        });
    }

    public void setDetailUI(final RoomVO roomVO){
        binding.detailIcon.setImageResource(R.drawable.icon_house_wh);
        binding.detailDate.setText(Utils.CreateDataWithCheck(roomVO.getData().getCREATE_DATE()));
        binding.detailTime.setText(getTimeLimit(roomVO.getData().getTIME_MINUTE()));
        binding.detailRoomInfo.setText(String.format("%s room , %s bathroom",roomVO.getData().getRM_ROOM(),roomVO.getData().getRM_TOILET()));
        binding.detailRoomAddr.setText(String.format("%s , %s",roomVO.getData().getRM_ADDR2(),roomVO.getData().getRM_ADDR1()));
        binding.detailDesc.setText(roomVO.getData().getRM_DESC());
        CommonUtil.setGlideImage(FeedDetailAct.this, Common.BASEFILEURL + roomVO.getData().getMEMBER_ID()+".jpg", binding.profilePhoto);
        binding.detailSticker.setVisibility(View.GONE);
        if(roomVO.getData().getFILE_LIST() != null && roomVO.getData().getFILE_LIST().size() > 0){
            binding.pager.setVisibility(View.VISIBLE);
            ImagePagerAdapter adapter = new ImagePagerAdapter(FeedDetailAct.this,getLayoutInflater(),roomVO.getData().getFILE_LIST());
            binding.pager.setPageMargin(getResources().getDisplayMetrics().widthPixels / -5);
            binding.pager.setOffscreenPageLimit(2);
            binding.pager.setAdapter(adapter);
        }else{
            binding.pager.setVisibility(View.GONE);
        }


        binding.detailContentPrice.setText(String.format("-금액 : $%s (Mon)",roomVO.getData().getRM_PRICE()));
        binding.detailContentRoom.setText(String.format("-방 : %s개",roomVO.getData().getRM_ROOM()));
        binding.detailContentBath.setText(String.format("-욕실 : %s개",roomVO.getData().getRM_TOILET()));
        binding.detailContentCnt.setText(String.format("Comment (%d)",roomVO.getData().getCOMMENT_LIST().size()));

        if(roomVO.getData().getCOMMENT_LIST() != null && roomVO.getData().getCOMMENT_LIST().size() > 0){
            binding.detailContentRecyler.setVisibility(View.VISIBLE);
            LinearLayoutManager manager = new LinearLayoutManager(this);
            binding.detailContentRecyler.setHasFixedSize(true);
            binding.detailContentRecyler.setLayoutManager(manager);
            binding.detailContentRecyler.setAdapter(new RecyclerAdapter(this, roomVO.getData().getCOMMENT_LIST(), R.layout.mainact));
        }else{
            binding.detailContentRecyler.setVisibility(View.GONE);
        }

        binding.detailContentCmtbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!KoreaTown.useLogin){
                    CommonUtil.showTwoBtnDialog(FeedDetailAct.this, "로그인", "로그인이 필요합니다. 로그인페이지로 이동하시겠습니까?", new CommonUtil.onDialogClick() {
                        @Override
                        public void setonConfirm() {
                            Intent intent = new Intent(FeedDetailAct.this, LoginAct.class);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void setonCancel() {

                        }
                    });
                    return;
                }
                Intent intent = new Intent(FeedDetailAct.this, CommentPopup.class);
                intent.putExtra(CommentPopup.GUBUN,"R");
                intent.putExtra(CommentPopup.SEQ,roomVO.getData().getRM_SEQ());
                startActivity(intent);
            }
        });

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
            CommonUtil.setGlideImage(context,Common.BASEFILEURL + item.getMEMBER_ID() +".jpg", holder.itemBinding.itemProfileImg);
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


    public class RecyclerAdapter2 extends RecyclerView.Adapter<RecyclerAdapter2.ViewHolder> {
        Context context;
        ArrayList<StoryVO.Story.Comment> items;
        int item_layout;
        public RecyclerAdapter2(Context context, ArrayList<StoryVO.Story.Comment> items, int item_layout) {
            this.context=context;
            this.items=items;
            this.item_layout=item_layout;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item_story, null);
            return new ViewHolder(v);
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onBindViewHolder(final ViewHolder holder,final int position) {
            StoryVO.Story.Comment item = items.get(position);
            holder.itemBinding.setComment(item);
            CommonUtil.setGlideImage(context,Common.BASEFILEURL + item.getMEMBER_ID() +".jpg", holder.itemBinding.itemProfileImg);
        }
        @Override
        public int getItemCount() {
            return this.items.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {

            CommentItemStoryBinding itemBinding = null;

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
