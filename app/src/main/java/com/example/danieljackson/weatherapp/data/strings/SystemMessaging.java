package com.example.danieljackson.weatherapp.data.strings;

import com.example.danieljackson.weatherapp.ui.cities.presenter.model.City;

public interface SystemMessaging {

    void d(String tag, String message);

    void e(String tag, String message);

    void e(String tag,String message, Throwable throwable);

    void i(String tag, String message);

    String getAlreadyAddedErrorMessage(City city);

    String getThisIsNotAValidZipCodeMessage();
 }
