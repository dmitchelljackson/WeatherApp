package com.example.danieljackson.weatherapp.ui.cities.presenter;

import com.example.danieljackson.weatherapp.data.persistence.SystemPersistence;
import com.example.danieljackson.weatherapp.data.strings.SystemMessaging;
import com.example.danieljackson.weatherapp.ui.cities.presenter.model.City;
import com.example.danieljackson.weatherapp.util.NetworkConversionUtil;
import com.jakewharton.rxrelay2.PublishRelay;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CitiesPresenterImpl implements CitiesPresenter {

    private static final long UPDATE_INTERVAL_MINUTES = 5;

    private CitiesWeatherApi citiesWeatherApi;

    private SystemMessaging systemMessaging;

    private SystemPersistence systemPersistence;

    private SortedSet<City> selectedCityCache;

    private PublishRelay<City> internalCityStream = PublishRelay.create();

    private PublishRelay<String> errorMessageStream = PublishRelay.create();

    private Map<City, Disposable> timerDisposableMap = new HashMap<>();

    public CitiesPresenterImpl(CitiesWeatherApi citiesWeatherApi, SystemMessaging systemMessaging,
                               SystemPersistence systemPersistence) {
        this.citiesWeatherApi = citiesWeatherApi;
        this.systemMessaging = systemMessaging;
        this.systemPersistence = systemPersistence;


        selectedCityCache = systemPersistence.getSavedCities();

        for(City city : selectedCityCache) {
            startUpdateTimerFor(city);
        }
    }

    @Override
    public Flowable<City> getCityUpdateStream() {
        return citiesWeatherApi.getWeatherForCity();
    }

    @Override
    public Flowable<String> errorMessageStream() {
        return null;
    }

    @Override
    public Single<City> searchForCityByZip(String zipCode) {
        long zipInt;
        try {
            zipInt = Long.parseLong(zipCode);
            return Single.fromObservable(getCityForZipCode(zipInt));
        } catch (NumberFormatException ex) {
            errorMessageStream.accept(systemMessaging.getThisIsNotAValidZipCodeMessage());
            return Single.error(ex);
        }

    }

    @Override
    public void addCity(City city) {


    }

    @Override
    public void deleteCity(City city) {

    }

    @Override
    public void swapCityListPosition(City position1, City position2) {

    }

    private Flowable<City> getCityForZipCode(long zipCode) {

    }

    private Flowable<City> startUpdateTimerFor(final City city) {
        Disposable disposable = Flowable.timer(UPDATE_INTERVAL_MINUTES, TimeUnit.MINUTES)
                .map(numb -> getCityForCityId(city.getCityId()))
                .doOnNext(city -> {
                    selectedCityCache.add(city);
                    systemPersistence.updateCityList(selectedCityCache);
                })
                .subscribeOn(Schedulers.io())
                .subscribe(internalCityStream);

        if (timerDisposableMap.get(city) != null) {
            systemMessaging.e("Time started for same city twice.  This should not happen. Stopping first timer.");
            timerDisposableMap.get(city).dispose();
        } else {
            timerDisposableMap.put(city, disposable);
        }
    }

    private Flowable<City> getCityForCityId(int cityId) {
        citiesWeatherApi.getWeatherForCity(cityId).map(apiResponse -> NetworkConversionUtil.cityFrom(apiResponse));
    }

}
