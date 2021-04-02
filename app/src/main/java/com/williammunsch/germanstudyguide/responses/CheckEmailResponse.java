package com.williammunsch.germanstudyguide.responses;

/**
 * Class for verifying email address verification. *Not currently in use!
 */
public class CheckEmailResponse {

    //The name of these values must match the values given by JSON in the PHP API
    private int exists;

    public CheckEmailResponse(int exists){
        this.exists = exists;

    }

    public String toString(){
        if (exists==1){
            return "Email already exists.";
        }else{
            return "Email does not exist.";
        }
    }

    public int getExists() {
        return exists;
    }

    public void setExists(int exists) {
        this.exists = exists;
    }
}
