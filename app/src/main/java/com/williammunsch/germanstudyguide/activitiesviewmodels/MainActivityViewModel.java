package com.williammunsch.germanstudyguide.activitiesviewmodels;

import android.text.Editable;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.williammunsch.germanstudyguide.responses.CheckUsernameResponse;
import com.williammunsch.germanstudyguide.responses.CreateUploadDataResponse;
import com.williammunsch.germanstudyguide.responses.LoginResponse;
import com.williammunsch.germanstudyguide.User;
import com.williammunsch.germanstudyguide.repositories.FlashcardRepository;
import com.williammunsch.germanstudyguide.repositories.Repository;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivityViewModel extends ViewModel {
    private Repository mRepository;//injected
    private FlashcardRepository mFlashcardRepository;
    private MutableLiveData<Integer> errorCode = new MutableLiveData<>();
    private LiveData<String> userName;
    private LiveData<String> userEmail;
    private LoginResponse loginResponse;


    @Inject
    public MainActivityViewModel(Repository repository, FlashcardRepository flashcardRepository){
        this.mRepository = repository;
        this.mFlashcardRepository = flashcardRepository;
        userName=mRepository.getUserName();
        userEmail=mRepository.getUserEmail();
    }



    /**
     * Calls the API login interface, returning user information to LoginResponse if login information is correct,
     * if not, the LoginResponse gathers the error of either non-existent email or incorrect password.
     * @param username The username typed into the edit text.
     * @param password The password typed into the edit text.
     */

    public void logIn(Editable username, Editable password){
        //Reset the flashcard activity data so nothing gets messed up when changing accounts.
        mFlashcardRepository.resetEverything();

        Call<LoginResponse> call = mRepository.apiService.logIn(username.toString().toLowerCase().trim(),password.toString().toLowerCase().trim());
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                loginResponse = response.body();
                if(loginResponse != null) {
                    errorCode.setValue(loginResponse.checkError());
                    if (!loginResponse.getIsError()){
                        //add user to ROOM database
                        mRepository.insertUser(new User(loginResponse.getUsername(),loginResponse.getPassword()));

                        //TODO : reset the ROOM database scores and reload them with new values
                        mRepository.downloadSaveData(loginResponse.getUsername());
                    }

                }
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                errorCode.setValue(5);
            }
        });
    }

    public void checkEmailAndPassword(Editable password, Editable username){
        boolean passGood = false;
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
        // if does not exist then account is created.
        if (passGood){// && emailGood){
            //TODO : hide ui and show loading bar thing while the background threads work
            registerAccount(username.toString().toLowerCase().trim(),password.toString().toLowerCase().trim());
        }

    }

    /*
      Function to register an account only using the username and password.
     */
    //TODO : Fix this so its not a call within a call
    public void registerAccount(String username, String pw){
        //First check if the username already exists in the database
        Call<CheckUsernameResponse> call = mRepository.apiService.checkUsername(username);

        //Based on the response from the call, either respond with username is taken or create the account if not
        call.enqueue(new Callback<CheckUsernameResponse>() {
            @Override
            public void onResponse(Call<CheckUsernameResponse> call, Response<CheckUsernameResponse> response) {
             //   System.out.println("Response to checkUsername call: ");
              //  System.out.println(response);
                CheckUsernameResponse checkResponse = response.body();
              //  System.out.println(checkResponse);

                if (checkResponse!=null && checkResponse.getExists()==1){ //Username is taken
                    mRepository.setEmailTakenVisibility(View.VISIBLE);
                }else{ //Username not taken, create another call to register the account
                    mRepository.setEmailTakenVisibility(View.GONE);

                    //Call within a call?***********************************************************
                    Call<CreateUploadDataResponse> call2 = mRepository.apiService.createAccount(username,pw);
                    call2.enqueue(new Callback<CreateUploadDataResponse>() {
                        @Override
                        public void onResponse(Call<CreateUploadDataResponse> call2, Response<CreateUploadDataResponse> response2) {
                            CreateUploadDataResponse lr = response2.body();

                            mRepository.insertUser(new User(username, pw));
                            errorCode.setValue(4);
                            setLoginAndRegistrationVisibilityGone();
                        }
                        @Override
                        public void onFailure(Call<CreateUploadDataResponse> call2, Throwable t) {
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<CheckUsernameResponse> call, Throwable t) {

            }
        });
    }

    /**
     * Function to retry downloading the base database data, such as A1 vocab
     * when the initial download failed. Called by the linear layout containing
     * the error message in the middle of the screen in activity_main.xml
     */
    public void retryDownloads(){
        mRepository.checkA1();
        //mRepository.checkStories();
    }





    public void logOut(){
        mRepository.deleteAllUsers();
        mRepository.resetAllScores();
        mFlashcardRepository.resetEverything();
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
