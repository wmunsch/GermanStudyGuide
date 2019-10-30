package com.williammunsch.germanstudyguide.viewmodels;

import android.text.Editable;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.williammunsch.germanstudyguide.LoginResponse;
import com.williammunsch.germanstudyguide.User;
import com.williammunsch.germanstudyguide.repositories.Repository;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivityViewModel extends ViewModel {
    private Repository mRepository;//injected

    private MutableLiveData<Integer> errorCode = new MutableLiveData<>();
    private LiveData<String> userName;
    //private MutableLiveData<String> userName = new MutableLiveData<>();
    private LoginResponse loginResponse;


    @Inject
    public MainActivityViewModel(Repository repository){
        this.mRepository = repository;
        userName=mRepository.getUserName();
       // userName.setValue("Log in");

    }


    /**
     * Calls the API login interface, returning user information to LoginResponse if login information is correct,
     * if not, the LoginResponse gathers the error of either non-existent email or incorrect password.
     * @param email The email typed into the edit text.
     * @param password The password typed into the edit text.
     */

    public void logIn(Editable email, Editable password){
        //Make a loading bar here?

        Call<LoginResponse> call = mRepository.apiService.logIn(email.toString().toLowerCase().trim(),password.toString().toLowerCase().trim());
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                System.out.println("Response to call: ");
                System.out.println(response);
                loginResponse = response.body();
                System.out.println(loginResponse);
                if(loginResponse != null) {
                    errorCode.setValue(loginResponse.checkError());
                    if (!loginResponse.getIsError()){
                        //add user to ROOM database
                        mRepository.insertUser(new User(loginResponse.getUsername(), loginResponse.getEmail(),loginResponse.getPassword()));

                        //TODO : reset the ROOM database scores and reload them with new values?
                        //mRepository.getScoresFromServer();
                    }

                }

                //End loading bar here?
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                System.out.println("Error on call" + t);
            }
        });
    }

    public LiveData<Integer> getA1Count() {
        return mRepository.getA1Count();
    }

    public LiveData<String> getUserName(){
        return userName;
    }


    public LiveData<Integer> getErrorCode(){
        return errorCode;
    }



}
