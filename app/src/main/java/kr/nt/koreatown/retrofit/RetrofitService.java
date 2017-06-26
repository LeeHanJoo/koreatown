package kr.nt.koreatown.retrofit;

import com.google.gson.JsonElement;

import java.util.List;
import java.util.Map;

import kr.nt.koreatown.vo.CommonVO;
import kr.nt.koreatown.vo.MsgVO;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

/**
 * Created by user on 2017-04-25.
 */

public interface RetrofitService {

    @GET("/mobile/m_ver_check")
    public Call<CommonVO> getVersion(@Query("OS_TYPE") String OS_TYPE);

    @Multipart
    @POST("/mobile/m_join")
    public Call<MsgVO> postSign(@PartMap Map<String, RequestBody> params);


    @Multipart
    @POST("/mobile/m_join")
    public Call<CommonVO> postSign(@Query("ID")  String ID, @Query("EMAIL") String EMAIL
            , @Query("PASSWORD") String PASSWORD, @Query("BIRTHDAY") String BIRTHDAY
            , @Query("NICK_NAME") String NICK_NAME , @Query("SEX") String SEX
            , @Part MultipartBody.Part PIC , @Query("PUSH_KEY") String PUSH_KEY
            , @Query("OS_VER") String OS_VER, @Query("DEVICE_ID") String DEVICE_ID, @Query("OS_TYPE") String OS_TYPE, @Query("MEMBER_TYPE") String MEMBER_TYPE);

    @GET("/mobile/m_login")
    public Call<CommonVO> doLogin(@Query("ID")  String ID,@Query("OS_TYPE") String OS_TYPE,@Query("PASSWORD") String PASSWORD
            ,@Query("PUSH_KEY") String PUSH_KEY,@Query("DEVICE_ID") String DEVICE_ID,@Query("OS_VER") String OS_VER
            , @Query("LAT") String LAT ,@Query("LON") String LON  );

    @Multipart
    @POST("/mobile/m_login")
    public Call<MsgVO> doLogin(@PartMap Map<String, RequestBody> params);

    @Multipart
    @POST("/mobile/m_login_upd")
    public Call<MsgVO> doOnlyLogin(@PartMap Map<String, RequestBody> params);


    @GET("/mobile/m_mypage")
    public Call<JsonElement> getMyPage(@Query("ID")  String ID);

    @GET("/mobile/m_map_list")
    public Call<JsonElement> getMapList(@Query("LAT")  String LAT, @Query("LON")  String LON);

    @Multipart
    @POST("/mobile/m_mypage_upd")
    public Call<MsgVO> updateMyPage(@PartMap Map<String, RequestBody> params);


    @Multipart
    @POST("/mobile/m_story_ins")
    public Call<MsgVO> postStory(@PartMap Map<String, RequestBody> params);

    @Multipart
    @POST("/mobile/m_room_ins")
    public Call<MsgVO> postRoom(@PartMap Map<String, RequestBody> params,@Part List<MultipartBody.Part> PIC);

    @GET("/mobile/m_room_detail")
    public Call<JsonElement> getRoomDetail(@Query("SEQ")  String SEQ);

    @GET("/mobile/m_story_detail")
    public Call<JsonElement> getStoryDetail(@Query("SEQ")  String SEQ);

    @Multipart
    @POST("/mobile/m_comment_ins")
    public Call<MsgVO> postComment(@PartMap Map<String, RequestBody> params);

    @Multipart
    @POST("/mobile/m_profile_upd ")
    public Call<MsgVO> updateProfileImage(@Query("ID") String ID,@Part MultipartBody.Part PIC);

    @Multipart
    @POST("/mobile/m_room_ins")
    public Call<MsgVO> postRoom(@PartMap Map<String, RequestBody> params);


    @POST("/mobile/m_mypage_upd")
    public Call<CommonVO> updateMyPage(@Query("ID") String ID, @Query("USER_NAME") String USER_NAME
            , @Query("EMAIL") String EMAIL , @Query("BIRTHDAY") String BIRTHDAY, @Query("SEX") String SEX);

    @Multipart
    @POST("/mobile/m_story_ins")
    public Call<CommonVO> insertStory(@Query("ID") String ID, @Query("STICKER") String STICKER
            , @Query("DESC") String DESC , @Query("LAT") String LAT, @Query("LON") String LON
            , @Query("ADDR1") String ADDR1, @Query("ADDR2") String ADDR2,@Part  MultipartBody.Part  FOOD_PIC);


}
