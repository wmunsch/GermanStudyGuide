package com.williammunsch.germanstudyguide;

public class CreateAccountResponse {
    String response;

    public CreateAccountResponse(String response){
        this.response = response;
    }

    public String toString(){
        return response;
    }

    public String getResponse(){
        return this.response;
    }
}
