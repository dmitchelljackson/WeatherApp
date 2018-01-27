package com.example.danieljackson.weatherapp.data.strings;

import com.example.danieljackson.weatherapp.ui.cities.presenter.model.City;

public interface SystemMessaging {

    void d(String message);

    void e(String message);

    void i(String message);

    String getAlreadyAddedErrorMessage(City city);

    String getThisIsNotAValidZipCodeMessage();
 }
