package com.williammunsch.germanstudyguide.activitiesviewmodels;

import android.text.Editable;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.williammunsch.germanstudyguide.responses.CheckUsernameResponse;
import com.williammunsch.germanstudyguide.responses.CreateUploadDataResponse;
import com.williammunsch.germanstudyguide.responses.LoginResponse;
import com.williammunsch.germanstudyguide.datamodels.User;
import com.williammunsch.germanstudyguide.repositories.Repository;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/**
 * Handles all of the livedata objects and logic for the main screen activity.
 */
public class MainActivityViewModel extends ViewModel {
    private final Repository mRepository;//injected
    private final MutableLiveData<Integer> errorCode = new MutableLiveData<>();
    private LoginResponse loginResponse;

    private final MutableLiveData<Integer> registrationVisibility = new MutableLiveData<>(View.GONE);
    private final MutableLiveData<Integer> passwordErrorVisibility = new MutableLiveData<>(View.GONE);
    private final MutableLiveData<Integer> emailTakenVisibility = new MutableLiveData<>(View.GONE);
    private final MutableLiveData<Integer> emailValidVisibility = new MutableLiveData<>(View.GONE);
    private final MutableLiveData<Integer> couldNotConnectVisibility = new MutableLiveData<>(View.GONE);

    @Inject
    public MainActivityViewModel(Repository repository){
        this.mRepository = repository;
    }




    /**
     * Calls the API login interface, returning user information to LoginResponse if login information is correct,
     * if not, the LoginResponse gathers the error of either non-existent email or incorrect password.
     * @param username The username typed into the edit text.
     * @param password The password typed into the edit text.
     */
    public void logIn(Editable username, Editable password){

        Call<LoginResponse> call = mRepository.apiService.logIn(username.toString().toLowerCase().trim(),password.toString().toLowerCase().trim());
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                loginResponse = response.body();
                if(loginResponse != null) {
                    errorCode.setValue(loginResponse.checkError());
                    if (!loginResponse.getIsError()){
                        //add user to ROOM database, downloadSaveData() is called via the transformation map in the repository
                        mRepository.insertUser(new User(loginResponse.getUsername(),loginResponse.getPassword()));
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

    /**
     * Checks to see if the username and password are using correct characters/length, then registers if good
     */
    public void checkEmailAndPassword(Editable password, Editable username){
        boolean passGood = false;
        char[] pwa = password.toString().toCharArray();
        if (password.length()<8 || password.length()>20){
            //error
            setPasswordErrorVisibility(View.VISIBLE);
            passGood = false;
        }else{
            setPasswordErrorVisibility(View.GONE);
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

    /**
     * Function to register an account only using the username and password.
     */
    //TODO : Fix this so its not a call within a call
    public void registerAccount(String username, String pw){
        //First check if the username already exists in the database
        Call<CheckUsernameResponse> call = mRepository.apiService.checkUsername(username);

        //Based on the response from the call, either respond with username is taken or create the account if not
        call.enqueue(new Callback<CheckUsernameResponse>() {
            @Override
            public void onResponse(Call<CheckUsernameResponse> call, Response<CheckUsernameResponse> response) {
                CheckUsernameResponse checkResponse = response.body();

                if (checkResponse!=null && checkResponse.getExists()==1){ //Username is taken
                    setEmailTakenVisibility(View.VISIBLE);
                }else{ //Username not taken, create another call to register the account
                    setEmailTakenVisibility(View.GONE);

                    Call<CreateUploadDataResponse> call2 = mRepository.apiService.createAccount(username,pw);
                    call2.enqueue(new Callback<CreateUploadDataResponse>() {
                        @Override
                        public void onResponse(Call<CreateUploadDataResponse> call2, Response<CreateUploadDataResponse> response2) {
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
     * Called when logging out, deletes the user, scores, and resets flashcard activity data.
     */
    public void logOut(){
        mRepository.deleteAllUsers();
        mRepository.resetAllScores();
    }

    public LiveData<Integer> getShowLoadingBar(){return mRepository.getShowLoadingBar();}
    public LiveData<Integer> getShowViewPager(){return mRepository.getShowViewPager();}

    public LiveData<Integer> getA1Count() {
        return mRepository.getA1Count();
    }

    public LiveData<String> getUserName(){
        return mRepository.getUserName();
    }


    public LiveData<Integer> getErrorCode(){
        return errorCode;
    }
    public LiveData<Integer> getProfileVisibility() {
        return mRepository.getProfileVisibility();
    }


    public LiveData<Integer> getLoginVisibility() {return mRepository.getLoginVisibility();}


    //View Visibility setters
    public void setUpRegistration(){
        mRepository.setLoginVisibility(View.GONE);
        mRepository.setProfileVisibility(View.GONE);
        registrationVisibility.setValue(View.VISIBLE);
        passwordErrorVisibility.setValue(View.INVISIBLE);
    }
    public void setUpRegistrationF(){
        mRepository.setLoginVisibility(View.VISIBLE);
        mRepository.setProfileVisibility(View.GONE);
        registrationVisibility.setValue(View.GONE);
        passwordErrorVisibility.setValue(View.GONE);
    }
    public void setLoginAndRegistrationVisibilityGone(){
        mRepository.setLoginVisibility(View.GONE);
        mRepository.setProfileVisibility(View.GONE);
        registrationVisibility.setValue(View.GONE);
        passwordErrorVisibility.setValue(View.GONE);
    }
    public void setPasswordErrorVisibility(int i){
        passwordErrorVisibility.setValue(i);
    }
    public LiveData<Integer> getPasswordErrorVisibility() {
        return passwordErrorVisibility;
    }
    public LiveData<Integer> getRegistrationVisibility() {
        return registrationVisibility;
    }
    public MutableLiveData<Integer> getEmailTakenVisibility() {
        return emailTakenVisibility;
    }
    public void setEmailTakenVisibility(int i) {
        this.emailTakenVisibility.setValue(i);
    }
    public LiveData<Integer> getCouldNotConnectVisibility(){return couldNotConnectVisibility;}
    public void setCouldNotConnectVisibility(int i){couldNotConnectVisibility.setValue(i);}
    public LiveData<Integer> getEmailValidVisibility() {
        return emailValidVisibility;
    }

    public void setEmailValidVisibility(int i) {
        this.emailValidVisibility.setValue(i);
    }
}
