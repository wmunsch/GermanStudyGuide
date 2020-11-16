package com.williammunsch.germanstudyguide;

public class CheckUsernameResponse {

    //The name of these values must match the values given by JSON in the PHP API
    private int exists;

    public CheckUsernameResponse(int exists){
        this.exists = exists;

    }

    public String toString(){
        if (exists==1){
            return "Username already exists.";
        }else{
            return "Username does not exist.";
        }
    }

    public int getExists() {
        return exists;
    }

    public void setExists(int exists) {
        this.exists = exists;
    }
}
