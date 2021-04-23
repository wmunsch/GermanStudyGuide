package com.williammunsch.germanstudyguide;

import android.app.Application;

import com.williammunsch.germanstudyguide.di.AppComponent;
import com.williammunsch.germanstudyguide.di.DaggerAppComponent;

/**
 * Base class for maintaining the global application state
 * Instantiated before anything else and builds the dagger appcomponent
 *
 * Remember to set this in the android manifest
 */
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
