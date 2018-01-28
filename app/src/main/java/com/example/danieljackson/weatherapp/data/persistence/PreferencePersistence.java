package com.example.danieljackson.weatherapp.data.persistence;


import android.content.Context;
import android.content.SharedPreferences;

import com.example.danieljackson.weatherapp.ui.cities.presenter.model.City;

import java.util.Set;
import java.util.SortedSet;

public class PreferencePersistence implements SystemPersistence {

    private static final String PREFS = "prefs";

    private final SharedPreferences preferences;

    public PreferencePersistence(Context context) {
        this.preferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }


    @Override
    public boolean updateCityList(Set<City> cities) {
        return false;
    }

    @Override
    public SortedSet<City> getSavedCities() {
        return null;
    }
}
