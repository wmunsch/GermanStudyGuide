package com.williammunsch.germanstudyguide.viewmodels;

import android.text.Editable;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.williammunsch.germanstudyguide.CheckEmailResponse;
import com.williammunsch.germanstudyguide.CreateAccountResponse;
import com.williammunsch.germanstudyguide.LoginResponse;
import com.williammunsch.germanstudyguide.User;
import com.williammunsch.germanstudyguide.repositories.Repository;

import java.util.regex.Pattern;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivityViewModel extends ViewModel {
    private Repository mRepository;//injected

    private MutableLiveData<Integer> errorCode = new MutableLiveData<>();
    private LiveData<String> userName;
    private LiveData<String> userEmail;
    //private MutableLiveData<String> userName = new MutableLiveData<>();
    private LoginResponse loginResponse;

    //private MutableLiveData<Integer> profileVisibility = new MutableLiveData<>();
    //private MutableLiveData<Integer> loginVisibility = new MutableLiveData<>();

    @Inject
    public MainActivityViewModel(Repository repository){
        this.mRepository = repository;
        userName=mRepository.getUserName();
        userEmail=mRepository.getUserEmail();
       // userName.setValue("Log in");


       // loginVisibility.setValue(View.VISIBLE);
       // profileVisibility.setValue(View.GONE);


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

                        //Change view from login to profile
                       // loginVisibility.setValue(View.GONE);
                        //profileVisibility.setValue((View.VISIBLE));
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

    public void checkEmailAndPassword(Editable email, Editable password, Editable username){
        System.out.println("pressed checkemailandpassword");
        boolean passGood = false;
        boolean emailGood = false;
        boolean emailExists = false;
        char[] pwa = password.toString().toCharArray();
        if (password.length()<8 || password.length()>20){
            //error
            mRepository.setPasswordErrorVisibility(View.VISIBLE);
            passGood = false;
        }else{
            mRepository.setPasswordErrorVisibility(View.GONE);
            passGood = true;
        }

        //Check if email is valid
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        if (email == null || !pat.matcher(email.toString().toLowerCase().trim()).matches()){
            //input for email is invalid
            mRepository.setEmailValidVisibility(View.VISIBLE);
            emailGood = false;
        }else{
            mRepository.setEmailValidVisibility(View.GONE);
            emailGood = true;
        }

        //If both the password and email are syntactically correct, check if email exists,
        // which causes a chain reaction if does not exist then account is created.
        if (passGood && emailGood){
            //TODO : hide ui and show loading bar thing while the background threads work
            registerAccount(username.toString().toLowerCase().trim(),email.toString().toLowerCase().trim(),password.toString().toLowerCase().trim());
            //errorCode.setValue(4);
        }

    }

    public void registerAccount(String username, String email, String pw){
        //check if email is already in database
        Call<CheckEmailResponse> call = mRepository.apiService.checkEmail(email);
        call.enqueue(new Callback<CheckEmailResponse>() {
            @Override
            public void onResponse(Call<CheckEmailResponse> call, Response<CheckEmailResponse> response) {
                System.out.println("Response to checkEmail call: ");
                System.out.println(response);
                CheckEmailResponse checkResponse= response.body();
                System.out.println(checkResponse);

                if (checkResponse!=null && checkResponse.getExists()==1){
                    mRepository.setEmailTakenVisibility(View.VISIBLE);
                   // mRepository.setEmailTakenBoolean(false);
                }else{
                    mRepository.setEmailTakenVisibility(View.GONE);

              /*
                    Call<CreateAccountResponse> call3 = mRepository.apiService.sendActivationEmail(email);
                    call3.enqueue(new Callback<CreateAccountResponse>() {
                        @Override
                        public void onResponse(Call<CreateAccountResponse> call3, Response<CreateAccountResponse> response3) {
                            System.out.println("Response to checkEmail call: ");
                            System.out.println(response3);
                            CreateAccountResponse checkResponse= response3.body();
                            System.out.println(checkResponse);
                        }

                        @Override
                        public void onFailure(Call<CreateAccountResponse> call3, Throwable t) {
                            System.out.println("Error on call" + t);
                        }
                    });
*/
                    //Call within a call?***********************************************************


                    Call<CreateAccountResponse> call2 = mRepository.apiService.createAccount(username,email,pw);
                    call2.enqueue(new Callback<CreateAccountResponse>() {
                        @Override
                        public void onResponse(Call<CreateAccountResponse> call2, Response<CreateAccountResponse> response2) {
                            System.out.println("Response to create account call: ");
                            System.out.println(response2);
                            CreateAccountResponse lr = response2.body();
                            System.out.println(lr);
                            //CreateAccountResponse checkResponse2= response2.body();
                            // System.out.println(checkResponse2);

                        }
                        @Override
                        public void onFailure(Call<CreateAccountResponse> call2, Throwable t) {
                            System.out.println("Error on call" + t);
                        }
                    });


                    //**************************************************************************
                }
            }
            @Override
            public void onFailure(Call<CheckEmailResponse> call, Throwable t) {
                System.out.println("Error on call" + t);
            }
        });


    }

    public void logOut(){
        mRepository.deleteAllUsers();
    }

    public LiveData<Integer> getEmailValidVisibility() {
        return mRepository.getEmailValidVisibility();
    }
    public void setUpRegistration(){
        mRepository.setRegistrationVisibility();
    }
    public void setUpRegistrationF(){
        mRepository.setRegistrationVisibilityF();
    }

    public LiveData<Integer> getA1Count() {
        return mRepository.getA1Count();
    }

    public LiveData<String> getUserName(){
        return userName;
    }

    public LiveData<String> getUserEmail() {
        return userEmail;
    }

    public LiveData<Integer> getErrorCode(){
        return errorCode;
    }
    public LiveData<Integer> getPasswordErrorVisibility() {
        return mRepository.getPasswordErrorVisibility();
    }
    public LiveData<Integer> getProfileVisibility() {
        return mRepository.getProfileVisibility();
    }

    public LiveData<Integer> getEmailTakenVisibility() {
        return mRepository.getEmailTakenVisibility();
    }


    public LiveData<Integer> getLoginVisibility() {return mRepository.getLoginVisibility();}

    public LiveData<Integer> getRegistrationVisibility() {return mRepository.getRegistrationVisibility();}
}
