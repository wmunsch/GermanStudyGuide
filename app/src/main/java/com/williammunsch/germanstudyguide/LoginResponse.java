package com.williammunsch.germanstudyguide;

import android.util.Log;

import java.util.List;

public class LoginResponse {
    //The name of these values must match the values given by JSON in the PHP API
    private String error;
    private String username, email, password;
    private boolean isError = false;
    private int errorType = 0;

    public LoginResponse(String username, String email, String password){
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public LoginResponse(String error){
        this.error = error;
    }

    public String toString(){
        if (error != null && !error.isEmpty()){
            return error;
        }else{
            return username + " " + email + " " + password;
           // return null;//user.getUsername();
        }
    }

    public int checkError(){
        if (error != null && !error.isEmpty()){
            isError = true;
            if (error.contentEquals("Account not found.")){
                return 1;
            }else{
                return 2;
            }
        }else{
            isError = false;
            return 0;
        }
    }
/*
    public boolean checkError(){
        if (error != null && !error.isEmpty()){
            isError = true;
        }else{
            isError = false;
        }
        return isError;
    }

 */

    public String getError() {
        return error;
    }

}
