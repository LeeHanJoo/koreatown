package kr.nt.koreatown.retrofit;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by user on 2017-05-04.
 */

public class RetrofitUtil {

    public static RequestBody toRequestBody (String value) {
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), value);
        return body ;
    }


    public static RequestBody toRequestBodyMultiPart (File value) {
        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), value);
        return fileBody ;
    }

    public static RequestBody toRequestBodyMultiPart2 (File value) {
        RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), value);
        return fileBody ;
    }

    public static MultipartBody.Part toRequestMultoPartBody (String keyName , File value){
        MultipartBody.Part body =
                MultipartBody.Part.createFormData(keyName, value.getName(), toRequestBodyMultiPart(value));
        return body;
    }


}
