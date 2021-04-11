package com.williammunsch.germanstudyguide.datamodels;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Handles saving data and uploading saved data to the server.
 */

@Entity(tableName = "users_table")
public class User {
    @PrimaryKey @NonNull
    //private String email;
    private String username;
    private String password;
    private String token;


    public User(String username, String password){
        this.username = username;
       // this.email = email;
        this.password = password;
    }

    public String toString(){return username + " " + password;}

    public String getUsername(){
        return username;
    }

   // public String getEmail(){
   //     return email;
    //}

    public String getPassword(){
        return password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
