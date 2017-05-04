package kr.nt.koreatown.retrofit;

import java.util.Map;

import kr.nt.koreatown.vo.CommonVO;
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
    public Call<CommonVO> postSign(@PartMap Map<String, RequestBody> params);


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

    @GET("/mobile/m_mypage")
    public Call<CommonVO> getMyPage(@Query("ID")  String ID);

    @POST("/mobile/m_mypage_upd")
    public Call<CommonVO> updateMyPage(@Query("ID") String ID, @Query("USER_NAME") String USER_NAME
            , @Query("EMAIL") String EMAIL , @Query("BIRTHDAY") String BIRTHDAY, @Query("SEX") String SEX);

    @Multipart
    @POST("/mobile/m_story_ins")
    public Call<CommonVO> insertStory(@Query("ID") String ID, @Query("STICKER") String STICKER
            , @Query("DESC") String DESC , @Query("LAT") String LAT, @Query("LON") String LON
            , @Query("ADDR1") String ADDR1, @Query("ADDR2") String ADDR2,@Part  MultipartBody.Part  FOOD_PIC);


}
