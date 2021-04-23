package com.williammunsch.germanstudyguide.responses;

/**
 * Used when returning info from JSON when creating an account.
 */
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
