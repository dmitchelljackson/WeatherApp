package com.example.danieljackson.weatherapp.data.persistence;


import com.example.danieljackson.weatherapp.ui.cities.presenter.model.City;

import java.util.List;
import java.util.Set;
import java.util.SortedSet;

public interface SystemPersistence {

    boolean updateCityList(Set<City> cities);

    SortedSet<City> getSavedCities();
}
