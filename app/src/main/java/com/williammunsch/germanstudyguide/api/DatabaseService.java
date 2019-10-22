package com.williammunsch.germanstudyguide.api;
import com.williammunsch.germanstudyguide.LoginResponse;
import com.williammunsch.germanstudyguide.User;
import com.williammunsch.germanstudyguide.datamodels.VocabModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Retrofit interface for
 * REST API access points
 * Request method and URL specified in the annotation
 */
public interface DatabaseService {

    @GET("api/dbscript.php")
    Call<List<VocabModel>> vocabList();

    @FormUrlEncoded
    @POST("api/login.php")
    Call<LoginResponse> logIn(
            @Field("email") String email,
            @Field("password") String password
    );


}
