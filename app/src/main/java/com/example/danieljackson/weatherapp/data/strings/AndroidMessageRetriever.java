package com.example.danieljackson.weatherapp.data.strings;

import com.example.danieljackson.weatherapp.ui.cities.presenter.model.City;

public class AndroidMessageRetriever implements SystemMessaging {
    @Override
    public String getAlreadyAddedErrorMessage(City city) {
        return null;
    }
}
