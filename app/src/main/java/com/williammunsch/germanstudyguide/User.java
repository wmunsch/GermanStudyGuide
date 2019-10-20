package com.williammunsch.germanstudyguide;

/**
 * Handles saving data and uploading saved data to the server.
 */
public class User {
    private String username, email, password;

    public User(String username, String email, String password){
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUsername(){
        return username;
    }

    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }
}
