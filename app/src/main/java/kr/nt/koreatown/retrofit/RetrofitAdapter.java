package kr.nt.koreatown.retrofit;


import kr.nt.koreatown.Common;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitAdapter {
	public static final int CONNECT_TIMEOUT = 15;
    public static final int WRITE_TIMEOUT = 15;
    public static final int READ_TIMEOUT = 15;
    //private static final String SERVER_URL = "http://192.168.1.35:8080/"; //2부터 url뒤에 /를 입력해야 합니다.
    private static final String SERVER_URL = Common.BASEURL; //2부터 url뒤에 /를 입력해야 합니다.
    private static OkHttpClient client;

    private static RetrofitService apiService;
    
    
    public synchronized static RetrofitService getInstance() {
        if (apiService == null) {

            //Retrofit 설정
            apiService = new Retrofit.Builder()
                    .baseUrl(SERVER_URL)
                    .addConverterFactory(GsonConverterFactory.create()) //Json Parser 추가
                    .build().create(RetrofitService.class); //인터페이스 연결
        }
        return apiService;
    }
    

}
