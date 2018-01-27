package com.example.danieljackson.weatherapp.data.persistence;


import android.content.SharedPreferences;

import com.example.danieljackson.weatherapp.ui.cities.presenter.model.City;

import java.util.Set;

public class PreferencePersistence implements SystemPersistence {

    private SharedPreferences preferences;

    public PreferencePersistence(SharedPreferences preferences) {
        this.preferences = preferences;

    }


    @Override
    public boolean updateCityList(Set<City> cities) {
        return false;
    }

    @Override
    public Set<City> getSavedCities() {
        return null;
    }
}
