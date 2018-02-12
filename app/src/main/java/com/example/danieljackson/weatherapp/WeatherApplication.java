package com.example.danieljackson.weatherapp;

import android.app.Application;

import com.example.danieljackson.weatherapp.dagger.AppComponent;
import com.example.danieljackson.weatherapp.dagger.DaggerAppComponent;
import com.example.danieljackson.weatherapp.dagger.PresenterModule;
import com.example.danieljackson.weatherapp.dagger.SystemModule;

public class WeatherApplication extends Application {

    public static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        if (appComponent == null) {
            appComponent = DaggerAppComponent.builder()
                    .presenterModule(new PresenterModule())
                    .systemModule(new SystemModule(getApplicationContext()))
                    .build();
        }
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }
}
