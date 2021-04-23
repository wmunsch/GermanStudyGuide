package com.williammunsch.germanstudyguide.responses;


/**
 * Used to get a response from JSON when uploading data
 */
public class CreateUploadDataResponse {
    String response;

    public CreateUploadDataResponse(String response){
        this.response = response;
    }

    public String toString(){
        if (response==null){
            return "The response was null";
        }
        return response.toString();

    }

    public String getResponse(){
        return this.response.toString();
    }
}
