package com.example.danieljackson.weatherapp.data.network;


import com.example.danieljackson.weatherapp.data.network.model.WeatherApiResponse;
import com.example.danieljackson.weatherapp.ui.cities.presenter.CitiesWeatherApi;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {

    @GET("/data/2.5/weather/")
    Flowable<WeatherApiResponse> getWeatherForCity(@Query("q") String cityName);


    @GET("/data/2.5/weather")
    Flowable<WeatherApiResponse> getWeatherForCity(@Query("id") long cityId);

    @GET("/data/2.5/weather")
    Flowable<WeatherApiResponse> getCityWeatherForZip(@Query("zip") String zipCode);

}
