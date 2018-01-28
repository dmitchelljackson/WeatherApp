package com.example.danieljackson.weatherapp.ui.cities.presenter;

import com.example.danieljackson.weatherapp.data.network.WeatherApi;
import com.example.danieljackson.weatherapp.data.persistence.SystemPersistence;
import com.example.danieljackson.weatherapp.data.strings.SystemMessaging;
import com.example.danieljackson.weatherapp.ui.cities.presenter.model.City;
import com.example.danieljackson.weatherapp.util.NetworkConversionUtil;
import com.jakewharton.rxrelay2.PublishRelay;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CitiesPresenterImpl implements CitiesPresenter {

    private static final long UPDATE_INTERVAL_MINUTES = 5;

    private static final String US_COUNTRY_CODE = "us";

    private WeatherApi weatherApi;

    private SystemMessaging systemMessaging;

    private SystemPersistence systemPersistence;

    private SortedSet<City> selectedCityCache;

    private PublishRelay<City> internalCityStream = PublishRelay.create();

    private PublishRelay<String> errorMessageStream = PublishRelay.create();

    private Map<City, Disposable> timerDisposableMap = new HashMap<>();

    public CitiesPresenterImpl(WeatherApi citiesWeatherApi, SystemMessaging systemMessaging,
                               SystemPersistence systemPersistence) {
        this.weatherApi = citiesWeatherApi;
        this.systemMessaging = systemMessaging;
        this.systemPersistence = systemPersistence;


//        selectedCityCache = systemPersistence.getSavedCities();
//
//        for(City city : selectedCityCache) {
//            startUpdateTimerFor(city);
//        }
    }

    @Override
    public Flowable<City> getCityUpdateStream() {
//        return weatherApi.getWeatherForCity();
        return null;
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
            return Single.fromObservable(getCityForZipCode(zipInt).toObservable()).subscribeOn(Schedulers.io());
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
        return weatherApi.getCityWeatherForZip(zipCode + "," + US_COUNTRY_CODE)
                .map(NetworkConversionUtil::cityFrom);
    }

    private Flowable<City> startUpdateTimerFor(final City city) {
//        Disposable disposable = Flowable.timer(UPDATE_INTERVAL_MINUTES, TimeUnit.MINUTES)
//                .map(numb -> getCityForCityId(city.getCityId()))
//                .doOnNext(resultCity -> {
//                    selectedCityCache.add(resultCity);
//                    systemPersistence.updateCityList(selectedCityCache);
//                })
//                .subscribeOn(Schedulers.io())
//                .subscribe(internalCityStream);
//
//        if (timerDisposableMap.get(city) != null) {
//            systemMessaging.e("Time started for same city twice.  This should not happen. Stopping first timer.");
//            timerDisposableMap.get(city).dispose();
//        } else {
//            timerDisposableMap.put(city, disposable);
//        }
        return null;
    }

    private Flowable<City> getCityForCityId(int cityId) {
//        return weatherApi.getWeatherForCity(cityId).map(NetworkConversionUtil::cityFrom);
        return null;
    }

}
