package com.example.danieljackson.weatherapp.ui.cities.presenter;

import com.example.danieljackson.weatherapp.data.network.WeatherApi;
import com.example.danieljackson.weatherapp.data.network.model.WeatherApiResponse;
import com.example.danieljackson.weatherapp.data.persistence.SystemPersistence;
import com.example.danieljackson.weatherapp.data.strings.SystemMessaging;
import com.example.danieljackson.weatherapp.ui.cities.presenter.model.City;
import com.example.danieljackson.weatherapp.util.NetworkConversionUtil;
import com.jakewharton.rxrelay2.PublishRelay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class CitiesPresenterImpl implements CitiesPresenter {

    private static final String TAG = CitiesPresenterImpl.class.getSimpleName();

    private static final long UPDATE_INTERVAL_MINUTES = 5;

    private static final String US_COUNTRY_CODE = "us";

    private WeatherApi weatherApi;

    private SystemMessaging systemMessaging;

    private SystemPersistence systemPersistence;

    private PublishRelay<SortedSet<City>> citiesStream = PublishRelay.create();

    private Scheduler listUpdateScheduler = Schedulers.single();

    private PublishRelay<String> errorMessageStream = PublishRelay.create();

    public CitiesPresenterImpl(WeatherApi citiesWeatherApi, SystemMessaging systemMessaging,
                               SystemPersistence systemPersistence) {
        this.weatherApi = citiesWeatherApi;
        this.systemMessaging = systemMessaging;
        this.systemPersistence = systemPersistence;
    }

    @Override
    public Flowable<SortedSet<City>> getCityUpdateStream() {
        return systemPersistence.getSavedCities().toFlowable(BackpressureStrategy.BUFFER);
    }

    @Override
    public Flowable<String> errorMessageStream() {
        return errorMessageStream.toFlowable(BackpressureStrategy.BUFFER);
    }

    @Override
    public Single<City> searchForCityByZip(String zipCode) {
        int zipInt;
        try {
            zipInt = Integer.parseInt(zipCode);
            return Single.fromObservable(getCityForZipCode(zipInt).toObservable()).subscribeOn(Schedulers.io());
        } catch (NumberFormatException ex) {
            errorMessageStream.accept(systemMessaging.getThisIsNotAValidZipCodeMessage());
            return Single.error(ex);
        }

    }

    @Override
    public void addCity(City city) {
        systemPersistence.getSavedCities().take(1).map(cities -> {
            cities.add(city);
            return cities;
        }).subscribeOn(listUpdateScheduler).subscribe(cities -> systemPersistence.updateCityList(cities));
    }

    @Override
    public void deleteCity(City city) {
        systemPersistence.getSavedCities().take(1).map(cities -> {
            cities.remove(city);
            return cities;
        }).subscribeOn(listUpdateScheduler).subscribe(cities -> systemPersistence.updateCityList(cities));
    }

    @Override
    public void swapCityListPosition(City position1, City position2) {
        systemPersistence.getSavedCities().take(1).map(cities -> {
            List<City> cityList = new ArrayList<>();
            cityList.addAll(cities);
            Collections.swap(cityList, position1.getListPosition(), position2.getListPosition());
            return new TreeSet<>(cityList);
        }).subscribeOn(listUpdateScheduler).subscribe(cities -> systemPersistence.updateCityList(cities));
    }

    private Flowable<City> getCityForZipCode(int zipCode) {
        return weatherApi.getCityWeatherForZip(zipCode + "," + US_COUNTRY_CODE)
                .map((WeatherApiResponse weatherApiResponse) -> NetworkConversionUtil.cityFrom(weatherApiResponse, zipCode));
    }

}
