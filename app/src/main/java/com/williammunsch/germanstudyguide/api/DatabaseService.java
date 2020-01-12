package com.williammunsch.germanstudyguide.api;
import com.williammunsch.germanstudyguide.CheckEmailResponse;
import com.williammunsch.germanstudyguide.CreateAccountResponse;
import com.williammunsch.germanstudyguide.LoginResponse;
import com.williammunsch.germanstudyguide.SaveDataResponse;
import com.williammunsch.germanstudyguide.datamodels.VocabModelA1;

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
    Call<List<VocabModelA1>> vocabList();
    //Call<VocabModelA1[]> vocabList();

    @FormUrlEncoded
    @POST("api/login.php")
    Call<LoginResponse> logIn(
            @Field("email") String email,
            @Field("password") String password
    );

    @GET("api/getdata.php")
    Call<SaveDataResponse> getSaveData();

    @FormUrlEncoded
    @POST("api/savedata.php")
    Call<Integer> pushSaveData(
            @Field("freq") String freq,
            @Field("score") String score,
            @Field("studying") String studying
    );

    @FormUrlEncoded
    @POST("api/checkEmail.php")
    Call<CheckEmailResponse> checkEmail(
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("api/register.php")
    Call<CreateAccountResponse> createAccount(
            @Field("username") String username,
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("api/sendActivationEmail.php")
    Call<CreateAccountResponse> sendActivationEmail(
            @Field("email") String email
    );


}
