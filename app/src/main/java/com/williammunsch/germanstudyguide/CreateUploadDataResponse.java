package com.williammunsch.germanstudyguide;

import java.util.List;

public class CreateUploadDataResponse {
    List<String> response;

    public CreateUploadDataResponse(List<String> response){
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
