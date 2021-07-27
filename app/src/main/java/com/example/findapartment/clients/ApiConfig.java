package com.example.findapartment.clients;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiConfig {

    @Multipart
    @PUT("update/{id}")
    Call < ServerResponse > updateApartment(@Header("Authorization") String authToken, @Path("id" ) String id, @Part MultipartBody.Part[] uploads, @Part("upload") RequestBody name);

    @Multipart
    @POST("upload")
    Call < ServerResponse > uploadMulFile(@Header("Authorization") String authToken, @Part MultipartBody.Part[] uploads, @Part("upload") RequestBody name);

}
