package com.aditya.bakingapp.application;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by ASUS A456U on 07/08/2017.
 */

public class BaseApplication extends Application {

    @Override
    public void onCreate(){
        super.onCreate();
        Realm.init(this);
    }
}
