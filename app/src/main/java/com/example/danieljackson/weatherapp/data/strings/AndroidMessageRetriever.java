package com.example.danieljackson.weatherapp.data.strings;

import android.content.Context;
import android.util.Log;

import com.example.danieljackson.weatherapp.ui.cities.presenter.model.City;

public class AndroidMessageRetriever implements SystemMessaging {

    private final Context context;

    public AndroidMessageRetriever(Context context) {
        this.context = context;
    }

    @Override
    public String getAlreadyAddedErrorMessage(City city) {
        return null;
    }

    @Override
    public void d(String tag, String message) {
        Log.d(tag, message);
    }

    @Override
    public void e(String tag, String message) {
        Log.e(tag, message);
    }

    @Override
    public void e(String tag, String message, Throwable throwable) {
        Log.e(tag, message, throwable);
    }

    @Override
    public void i(String tag, String message) {
        Log.i(tag, message);
    }

    @Override
    public String getThisIsNotAValidZipCodeMessage() {
        return null;
    }
}
