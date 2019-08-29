package com.williammunsch.germanstudyguide;

import android.app.Application;

import com.williammunsch.germanstudyguide.di.AppComponent;
import com.williammunsch.germanstudyguide.di.DaggerAppComponent;

public class GermanApp extends Application {

    private AppComponent component;

    @Override
    public void onCreate(){
        super.onCreate();
        component = DaggerAppComponent.builder().application(this).build();
    }

    public AppComponent getAppComponent(){
        return component;
    }
}
