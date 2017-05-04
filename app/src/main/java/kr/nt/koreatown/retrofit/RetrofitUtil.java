package kr.nt.koreatown.retrofit;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.kakao.auth.StringSet.file;

/**
 * Created by user on 2017-05-04.
 */

public class RetrofitUtil {

    public static RequestBody toRequestBody (String value) {
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), value);
        return body ;
    }

    public static RequestBody toRequestBodyMultiPart (File value) {
        RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), value);
        return fileBody ;
    }


}
