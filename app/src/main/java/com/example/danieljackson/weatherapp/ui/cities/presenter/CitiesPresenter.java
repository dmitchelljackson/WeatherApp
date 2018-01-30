package com.example.danieljackson.weatherapp.ui.cities.presenter;

import com.example.danieljackson.weatherapp.ui.cities.presenter.model.City;

import java.util.SortedSet;

import io.reactivex.Flowable;
import io.reactivex.Single;


public interface CitiesPresenter {

    Flowable<SortedSet<City>> getCityUpdateStream();

    Flowable<String> errorMessageStream();

    Single<City> searchForCityByZip(String zipCode);

    void addCity(City city);

    void deleteCity(City city);

    void swapCityListPosition(City position1, City position2);
}
