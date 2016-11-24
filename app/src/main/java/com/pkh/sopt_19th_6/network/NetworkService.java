package com.pkh.sopt_19th_6.network;


import com.pkh.sopt_19th_6.main.MainResult;
import com.pkh.sopt_19th_6.register.RegisterResult;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by kh
 */
public interface NetworkService {


    @GET("/posts")
    Call<MainResult> getMainData();


    @GET("/posts/{notice_id}")
    Call<MainResult> getDetailData(@Path("notice_id") String notice_id);

    @Multipart
    @POST("/posts")
    Call<RegisterResult> registerImgNotice(@Part MultipartBody.Part file,
                                         @Part("subject") RequestBody title,
                                         @Part("contents") RequestBody content);

}
