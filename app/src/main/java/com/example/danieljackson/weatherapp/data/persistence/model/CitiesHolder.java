package com.example.danieljackson.weatherapp.data.persistence.model;

import com.example.danieljackson.weatherapp.ui.cities.presenter.model.City;

import java.util.SortedSet;

public class CitiesHolder {

    private SortedSet<City> cities;

    public CitiesHolder(SortedSet<City> cities) {
        this.cities = cities;
    }

    public SortedSet<City> getCities() {
        return cities;
    }

    public void setCities(SortedSet<City> cities) {
        this.cities = cities;
    }
}
