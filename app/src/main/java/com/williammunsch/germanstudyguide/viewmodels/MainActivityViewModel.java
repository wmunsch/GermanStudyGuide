package com.williammunsch.germanstudyguide.viewmodels;

import android.text.Editable;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import com.williammunsch.germanstudyguide.repositories.Repository;

import javax.inject.Inject;

public class MainActivityViewModel extends ViewModel {
    private Repository mRepository;//injected


    @Inject
    public MainActivityViewModel(Repository repository){
        this.mRepository = repository;


    }

    public void logIn(Editable email, Editable password){
        System.out.println("clicekd login");
        System.out.println("Email = " + email + "      password = " + password);

        if (email.toString() != null && !email.toString().isEmpty() && password.toString() != null && !password.toString().isEmpty()){
            mRepository.logIn(email.toString().toLowerCase().trim(), password.toString().toLowerCase().trim());
        }

        //TODO : Handle failed login (reset password)

        //TODO : Handle successful login (change fragment?)

    }


}
