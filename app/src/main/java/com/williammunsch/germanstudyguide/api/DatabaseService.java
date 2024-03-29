package com.williammunsch.germanstudyguide.api;
import com.williammunsch.germanstudyguide.responses.CheckEmailResponse;
import com.williammunsch.germanstudyguide.responses.CheckUsernameResponse;
import com.williammunsch.germanstudyguide.responses.CreateAccountResponse;
import com.williammunsch.germanstudyguide.responses.CreateUploadDataResponse;
import com.williammunsch.germanstudyguide.responses.LoginResponse;
import com.williammunsch.germanstudyguide.responses.SaveDataResponse;
import com.williammunsch.germanstudyguide.datamodels.Hag_Sentences;
import com.williammunsch.germanstudyguide.datamodels.Hag_Words;
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

    @FormUrlEncoded
    @POST("api/login.php")
    Call<LoginResponse> logIn(
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("api/geta1data.php")
    Call<SaveDataResponse> getSaveData(
            @Field("username") String username
    );

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
    @POST("api/checkUsername.php")
    Call<CheckUsernameResponse> checkUsername(
            @Field("username") String username
    );

    @FormUrlEncoded
    @POST("api/register.php")
    Call<CreateUploadDataResponse> createAccount(
            @Field("username") String username,
            //@Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("api/sendActivationEmail.php")
    Call<CreateAccountResponse> sendActivationEmail(
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("api/createSaveData.php")
    Call<CreateUploadDataResponse> createSaveData(
            @Field("username") String username,
            @Field("table_name") String table_name1
         //   @Field("table_name2") String table_name2,
         //   @Field("table_name3") String table_name3,
         //   @Field("table_name4") String table_name4,
         //   @Field("table_name5") String table_name5,
         //   @Field("table_name6") String table_name6
    );


    //Used to upload flashcard save data to the remote database.
    @FormUrlEncoded
    @POST("api/uploaddata.php")
    Call<CreateUploadDataResponse> uploadData(
            @Field("username") String username,
            @Field("table_name") String table_name,
            @Field("score_list") String score_list,
            @Field("freq_list") String freq_list,
            @Field("studying_list") String studying_list,
            @Field("pid") String pid
    );

    @GET("api/downloadhagsentences.php")
    Call<List<Hag_Sentences>> downloadHagSentences();

    @GET("api/downloadhagwords.php")
    Call<List<Hag_Words>> downloadHagWords();

}
