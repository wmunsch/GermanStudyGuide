package com.williammunsch.germanstudyguide.api;
import com.williammunsch.germanstudyguide.datamodels.VocabModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Retrofit interface for
 * REST API access points
 * Request method and URL specified in the annotation
 */
public interface DatabaseService {

    @GET("api/dbscript.php")
    Call<List<VocabModel>> vocabList();


}
