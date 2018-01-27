package com.example.danieljackson.weatherapp.ui.cities.presenter;

import com.example.danieljackson.weatherapp.ui.cities.presenter.model.City;

import io.reactivex.Observable;
import io.reactivex.Single;


public interface CitiesPresenter {

    Observable<City> getCityUpdateStream();

    Observable<String> errorMessageStream();

    Single<City> searchForCityByZip(String zipCode);

    void addCity(City city);

    void deleteCity(City city);

    void swapCityListPosition(City position1, City position2);
}
