package com.example.danieljackson.weatherapp.data.persistence;


import android.content.Context;
import android.content.SharedPreferences;

import com.example.danieljackson.weatherapp.data.persistence.model.CitiesHolder;
import com.example.danieljackson.weatherapp.ui.cities.presenter.model.City;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.rxrelay2.BehaviorRelay;
import com.jakewharton.rxrelay2.Relay;

import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import io.reactivex.Flowable;
import io.reactivex.processors.BehaviorProcessor;

public class PreferencePersistence implements SystemPersistence {

    private static final String PREFS = "prefs";

    private static final String CITIES_PREF_KEY = "cities";

    private final String defaultCitiesHolder;

    private final SharedPreferences preferences;

    private Gson gson;

    private final BehaviorRelay<SortedSet<City>> cachedCities = BehaviorRelay.create();

    public PreferencePersistence(Context context) {
        this.preferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);

        gson = new GsonBuilder().create();
        defaultCitiesHolder = gson.toJson(new CitiesHolder(new TreeSet<>()));

        cachedCities.accept(loadCitiesFromPrefs());
    }


    @Override
    public void updateCityList(SortedSet<City> cities) {
        Iterator<City> iterator = cities.iterator();
        SortedSet<City> outCities = new TreeSet<>();
        int i = 0;
        while (iterator.hasNext()) {
            City city = iterator.next();
            city.setListPosition(i);
            outCities.add(new City.Builder(
                    city.getCityId(),
                    city.getCityName(),
                    city.getZipCode(),
                    city.getListPosition()).build());
            i++;
        }
        saveCitiesToPrefs(outCities);
        cachedCities.accept(cities);
    }

    @Override
    public Relay<SortedSet<City>> getSavedCities() {
        return cachedCities;
    }

    private void saveCitiesToPrefs(SortedSet<City> cities) {
        preferences.edit().putString(CITIES_PREF_KEY, gson.toJson(new CitiesHolder(cities))).commit();
    }

    private SortedSet<City> loadCitiesFromPrefs() {
        return gson.fromJson(
                preferences.getString(CITIES_PREF_KEY, defaultCitiesHolder), CitiesHolder.class).getCities();
    }
}
