package com.example.findapartment.clients;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiConfig {

//    @Multipart
//    @POST("apartments")
//    Call uploadFile(@Part MultipartBody.Part file, @Part("file") RequestBody name);
    @Multipart
    @POST("upload")
    Call < ServerResponse > uploadMulFile(@Header("Authorization") String authToken, @Part MultipartBody.Part[] uploads, @Part("upload") RequestBody name);
//    Call < ServerResponse > uploadMulFile(@Part MultipartBody.Part file1, @Part("upload") RequestBody name);


}
