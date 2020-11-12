package com.williammunsch.germanstudyguide.viewmodels;

import android.text.Editable;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.williammunsch.germanstudyguide.CheckEmailResponse;
import com.williammunsch.germanstudyguide.CheckUsernameResponse;
import com.williammunsch.germanstudyguide.CreateAccountResponse;
import com.williammunsch.germanstudyguide.CreateUploadDataResponse;
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
    //private LiveData<Integer> showLoadingBar;
    //private LiveData<Integer> showViewPager;
    //private LiveData<Integer> couldNotConnectVisibility;

    //private MutableLiveData<Integer> profileVisibility = new MutableLiveData<>();
    //private MutableLiveData<Integer> loginVisibility = new MutableLiveData<>();

    @Inject
    public MainActivityViewModel(Repository repository){
        this.mRepository = repository;
        userName=mRepository.getUserName();
        userEmail=mRepository.getUserEmail();

        //showLoadingBar = mRepository.getShowLoadingBar();
        //showViewPager = mRepository.getShowViewPager();
       // userName.setValue("Log in");


       // loginVisibility.setValue(View.VISIBLE);
       // profileVisibility.setValue(View.GONE);


    }



    /**
     * Calls the API login interface, returning user information to LoginResponse if login information is correct,
     * if not, the LoginResponse gathers the error of either non-existent email or incorrect password.
     * @param username The username typed into the edit text.
     * @param password The password typed into the edit text.
     */

    public void logIn(Editable username, Editable password){
        //Make a loading bar here?

        Call<LoginResponse> call = mRepository.apiService.logIn(username.toString().toLowerCase().trim(),password.toString().toLowerCase().trim());
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
                        mRepository.insertUser(new User(loginResponse.getUsername(),loginResponse.getPassword()));

                        //Change view from login to profile
                       // loginVisibility.setValue(View.GONE);
                        //profileVisibility.setValue((View.VISIBLE));
                        //TODO : reset the ROOM database scores and reload them with new values?
                        //mRepository.getScoresFromServer();
                        mRepository.downloadSaveData(loginResponse.getUsername());
                    }

                }

                //End loading bar here?
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                System.out.println("Error on call" + t);
                errorCode.setValue(5);
               // mRepository.setCouldNotConnectVisibility(View.VISIBLE);
            }
        });
    }

    public void checkEmailAndPassword(Editable password, Editable username){
        System.out.println("pressed checkemailandpassword");
        boolean passGood = false;
        //boolean emailGood = false;
        //boolean emailExists = false;
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
        /*
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
*/

        //If both the password and email are syntactically correct, check if email exists,
        // which causes a chain reaction if does not exist then account is created.
        if (passGood){// && emailGood){
            //TODO : hide ui and show loading bar thing while the background threads work
            registerAccount(username.toString().toLowerCase().trim(),password.toString().toLowerCase().trim());//,email.toString().toLowerCase().trim(),password.toString().toLowerCase().trim());
            //errorCode.setValue(4);
        }

    }

    /*
      Method to register an account only using the username and password.
      Utilizes google's reCAPTCHA v2 for android to create accounts without an email validation.
     */
    public void registerAccount(String username, String pw){
        //First check if the username already exists in the database
        Call<CheckUsernameResponse> call = mRepository.apiService.checkUsername(username);

        //Based on the response from the call, either respond with username is taken or create the account if not
        call.enqueue(new Callback<CheckUsernameResponse>() {
            @Override
            public void onResponse(Call<CheckUsernameResponse> call, Response<CheckUsernameResponse> response) {
                System.out.println("Response to checkUsername call: ");
                System.out.println(response);
                CheckUsernameResponse checkResponse = response.body();
                System.out.println(checkResponse);

                if (checkResponse!=null && checkResponse.getExists()==1){ //Username is taken
                    mRepository.setEmailTakenVisibility(View.VISIBLE);
                }else{ //Username not taken, create another call to register the account
                    mRepository.setEmailTakenVisibility(View.GONE);

                    //Call within a call?***********************************************************
                    Call<CreateUploadDataResponse> call2 = mRepository.apiService.createAccount(username,pw);
                    call2.enqueue(new Callback<CreateUploadDataResponse>() {
                        @Override
                        public void onResponse(Call<CreateUploadDataResponse> call2, Response<CreateUploadDataResponse> response2) {
                            System.out.println("Response to create account call: ");
                            System.out.println(response2);
                            CreateUploadDataResponse lr = response2.body();
                            System.out.println(lr);

                            mRepository.insertUser(new User(username, pw));
                            errorCode.setValue(4);
                            setLoginAndRegistrationVisibilityGone();
                        }
                        @Override
                        public void onFailure(Call<CreateUploadDataResponse> call2, Throwable t) {
                            System.out.println("Error on call" + t);
                        }
                    });

                    //Create another call for inserting all of the save_data rows so when updating after an activity you don't have to check if the row exists or not.
                    /*
                    Call<CreateUploadDataResponse> call3 = mRepository.apiService.createSaveData(username,"A1","A2","B1","B2","C1","C2");
                    call3.enqueue(new Callback<CreateUploadDataResponse>() {
                        @Override
                        public void onResponse(Call<CreateUploadDataResponse> call3, Response<CreateUploadDataResponse> response3) {
                            System.out.println("Response to creating save data");
                            System.out.println(response3);
                            CreateUploadDataResponse lr = response3.body();
                            System.out.println(lr);

                        }

                        @Override
                        public void onFailure(Call<CreateUploadDataResponse> call3, Throwable t) {
                            System.out.println("Error on call when creating save data" + t);
                        }
                    });
*/
                }
            }

            @Override
            public void onFailure(Call<CheckUsernameResponse> call, Throwable t) {
                System.out.println("Error on call" + t);
            }
        });
    }

    /**
     * Method to retry downloading the base database data, such as A1 vocab
     * when the initial download failed. Called by the linear layout containing
     * the error message in the middle of the screen in activity_main.xml
     */
    public void retryDownloads(){
        mRepository.checkA1();
    }


/*
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
                }else{
                    mRepository.setEmailTakenVisibility(View.GONE);

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


                            mRepository.insertUser(new User(username, email,pw));
                            errorCode.setValue(4);
                            setLoginAndRegistrationVisibilityGone();
                        }
                        @Override
                        public void onFailure(Call<CreateAccountResponse> call2, Throwable t) {
                            System.out.println("Error on call" + t);
                        }
                    });

                }
            }
            @Override
            public void onFailure(Call<CheckEmailResponse> call, Throwable t) {
                System.out.println("Error on call" + t);
            }
        });
    }
*/
    public void logOut(){
        mRepository.deleteAllUsers();
        mRepository.resetAllScores();
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
    public void setLoginAndRegistrationVisibilityGone(){mRepository.setLoginAndRegistrationVisibilityGone();}

    public LiveData<Integer> getShowLoadingBar(){return mRepository.getShowLoadingBar();}
    public LiveData<Integer> getShowViewPager(){return mRepository.getShowViewPager();}

    public LiveData<Integer> getCouldNotConnectVisibility(){return mRepository.getCouldNotConnectVisibility();}

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
