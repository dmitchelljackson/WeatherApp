package com.example.danieljackson.weatherapp.data.network;


import com.example.danieljackson.weatherapp.data.network.model.WeatherApiResponse;
import com.example.danieljackson.weatherapp.ui.cities.presenter.CitiesWeatherApi;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi extends CitiesWeatherApi {

    @GET("/data/2.5/weather/")
    Observable<WeatherApiResponse> getWeatherForCity(@Query("q") String cityName);


    @GET("/data/2.5/weather")
    Observable<WeatherApiResponse> getWeatherForCity(@Query("id") long cityId);
}
