package com.example.danieljackson.weatherapp.ui.cities.presenter;

import com.example.danieljackson.weatherapp.data.network.WeatherApi;
import com.example.danieljackson.weatherapp.data.network.model.WeatherApiResponse;
import com.example.danieljackson.weatherapp.data.persistence.SystemPersistence;
import com.example.danieljackson.weatherapp.data.strings.SystemMessaging;
import com.example.danieljackson.weatherapp.ui.cities.presenter.model.City;
import com.example.danieljackson.weatherapp.util.NetworkConversionUtil;
import com.jakewharton.rxrelay2.BehaviorRelay;
import com.jakewharton.rxrelay2.PublishRelay;
import com.jakewharton.rxrelay2.Relay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CitiesPresenterImpl implements CitiesPresenter {

    private static final String TAG = CitiesPresenterImpl.class.getSimpleName();

    private static final long UPDATE_INTERVAL_MINUTES = 5;

    private static final String US_COUNTRY_CODE = "us";

    private WeatherApi weatherApi;

    private SystemMessaging systemMessaging;

    private SystemPersistence systemPersistence;

    private Relay<SortedSet<City>> citiesStream = BehaviorRelay.create();

    private Scheduler listUpdateScheduler = Schedulers.single();

    private PublishRelay<String> errorMessageStream = PublishRelay.create();

    private Disposable citiesStreamDisposable;

    public CitiesPresenterImpl(WeatherApi citiesWeatherApi, SystemMessaging systemMessaging,
                               SystemPersistence systemPersistence) {
        this.weatherApi = citiesWeatherApi;
        this.systemMessaging = systemMessaging;
        this.systemPersistence = systemPersistence;

        refreshCityStream();

        systemPersistence.getSavedCitiesStream().subscribe(cities -> {
            systemMessaging.i(TAG, "New cities from prefs");
            citiesStreamDisposable.dispose();
            refreshCityStream();
        });
    }

    @Override
    public Flowable<SortedSet<City>> getCityUpdateStream() {
        return citiesStream.toFlowable(BackpressureStrategy.BUFFER);
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
        systemPersistence.getSavedCitiesStream().take(1).map(cities -> {
            cities.add(city);
            return cities;
        }).subscribeOn(listUpdateScheduler).subscribe(cities -> systemPersistence.updateCityList(cities));
    }

    @Override
    public void deleteCity(City city) {
        systemPersistence.getSavedCitiesStream().take(1).map(cities -> {
            cities.remove(city);
            return cities;
        }).subscribeOn(listUpdateScheduler).subscribe(cities -> systemPersistence.updateCityList(cities));
    }

    @Override
    public void swapCityListPosition(City position1, City position2) {
        systemPersistence.getSavedCitiesStream().take(1).map(cities -> {
            List<City> cityList = new ArrayList<>();
            cityList.addAll(cities);
            Collections.swap(cityList, position1.getListPosition(), position2.getListPosition());
            return new TreeSet<>(cityList);
        }).subscribeOn(listUpdateScheduler).subscribe(cities -> systemPersistence.updateCityList(cities));
    }

    @Override
    public void refreshCities() {
        refreshCityStream();
    }

    private Flowable<City> getCityForZipCode(int zipCode) {
        return weatherApi.getCityWeatherForZip(zipCode + "," + US_COUNTRY_CODE)
                .map(WeatherApiResponse::getCoordinates)
                //TODO Leet hack due to issue with zip code endpoint not returning id properly
                .flatMap(coordinates -> weatherApi.getCityWeatherForCoordinates(coordinates.getLatitude(), coordinates.getLongitude()))
                .map((WeatherApiResponse weatherApiResponse) -> NetworkConversionUtil.cityFrom(weatherApiResponse, zipCode));
    }

    private Flowable<SortedSet<City>> getWeatherRefreshUpdateStream(long refreshTimeMinutes) {
        return Flowable.interval(refreshTimeMinutes, TimeUnit.MINUTES)
                .flatMap(i -> getRefreshCityStream())
                .startWith(getRefreshCityStream());
    }

    private Flowable<SortedSet<City>> getRefreshCityStream() {
        SortedSet<City> persistedCities = systemPersistence.getSavedCities();
        if (persistedCities.size() > 0) {
            systemMessaging.i(TAG, "Refreshing weather");

            String cityIdList = "";
            for (City city : persistedCities) {
                cityIdList = cityIdList + city.getCityId() + ",";
            }
            cityIdList = cityIdList.substring(0, cityIdList.length() - 1);

            return weatherApi.getWeatherForCities(cityIdList)
                    .map(weatherApiResponseList ->
                            NetworkConversionUtil.mapApiResponsesToCities(persistedCities, weatherApiResponseList.getList(), systemMessaging)
                    )
                    .onErrorResumeNext(Flowable.empty())
                    .subscribeOn(Schedulers.io());
        } else {
            systemMessaging.i(TAG, "No persisted cities, skipping refresh");
            return Flowable.empty();
        }
    }

    private void refreshCityStream() {
        citiesStreamDisposable = getWeatherRefreshUpdateStream(UPDATE_INTERVAL_MINUTES)
                .startWith(systemPersistence.getSavedCitiesStream().take(1).subscribeOn(listUpdateScheduler))
                .subscribe(citiesStream);
    }

}
