package com.example.danieljackson.weatherapp.data.persistence;


import com.example.danieljackson.weatherapp.ui.cities.presenter.model.City;
import com.jakewharton.rxrelay2.Relay;

import java.util.List;
import java.util.Observable;
import java.util.Set;
import java.util.SortedSet;

import io.reactivex.Flowable;

public interface SystemPersistence {

    void updateCityList(SortedSet<City> cities);

    SortedSet<City> getSavedCities();

    Flowable<SortedSet<City>> getSavedCitiesStream();
}
