package kr.nt.koreatown.feed;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.HashMap;
import java.util.Map;

import kr.nt.koreatown.Common;
import kr.nt.koreatown.R;
import kr.nt.koreatown.bus.BusProvider;
import kr.nt.koreatown.bus.RefreshDetailViewEvent;
import kr.nt.koreatown.databinding.CommentactBinding;
import kr.nt.koreatown.retrofit.RetrofitAdapter;
import kr.nt.koreatown.retrofit.RetrofitUtil;
import kr.nt.koreatown.util.CommonUtil;
import kr.nt.koreatown.util.SharedManager;
import kr.nt.koreatown.vo.MsgVO;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by user on 2017-05-17.
 */

public class CommentPopup extends Activity {

    public final static String GUBUN = "reqGUBUN";
    public final static String SEQ = "reqSeq";
    CommentactBinding binding = null;
    public String mGubun = "";
    public String mSeq = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.7f;

        // layoutParams.softInputMode =
        // WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
        // |WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
        getWindow().setAttributes(layoutParams);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // 다이얼로그
        //getWindow().setBackgroundDrawable(new ColorDrawable(Color.YELLOW)); // 다이얼로그
        binding =  DataBindingUtil.setContentView(this, R.layout.commentact);

        init();
    }

    private void init(){

        mGubun = getIntent().getStringExtra(GUBUN);
        mSeq = getIntent().getStringExtra(SEQ);

        binding.commSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendComm();
            }
        });
    }

    private void sendComm(){
        String comment = binding.commEdit.getText().toString().trim();
        if(comment.isEmpty() || comment.length() == 0){
            CommonUtil.showOnBtnDialog(CommentPopup.this, "코멘트입력", "코멘트를 입력해주세요", null);
            return;
        }

        String ID = SharedManager.getInstance().getString(this, Common.ID);
        Map<String, RequestBody> params = new HashMap<String, RequestBody>();
        params.put(Common.ID, RetrofitUtil.toRequestBody(ID));
        params.put(Common.TYPE, RetrofitUtil.toRequestBody(mGubun));
        params.put(Common.SEQ, RetrofitUtil.toRequestBody(String.valueOf(mSeq)));
        params.put(Common.COMMENT, RetrofitUtil.toRequestBody(String.valueOf(comment)));

        Call<MsgVO> call2 = RetrofitAdapter.getInstance().postComment(params);
        call2.enqueue(new Callback<MsgVO>() {
            @Override
            public void onResponse(Call<MsgVO> call, retrofit2.Response<MsgVO> response) {
                MsgVO item = response.body();
                if(item != null && item.getResult().equals("1")){
                    // BusProvider.getInstance().post(new RefreshViewEvent());
                    BusProvider.getInstance().post(new RefreshDetailViewEvent());
                    finish();

                }
            }

            @Override
            public void onFailure(Call<MsgVO> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
