package com.example.danieljackson.weatherapp.data.network;


import com.example.danieljackson.weatherapp.data.network.model.WeatherApiResponse;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {

    @GET("/data/2.5/weather")
    Flowable<WeatherApiResponse> getCityWeatherForZip(@Query("zip") String zipCode);

    @GET("/data/2.5/weather")
    Flowable<WeatherApiResponse> getCityWeatherForCoordinates(@Query("lat") double latitude, @Query("lon") double longitude);

    //TODO it would be better for this method to take a list of cityId, unfortunately the won't support this
    @GET("/data/2.5/group")
    Flowable<WeatherApiResponse> getWeatherForCities(@Query("id") String cityIdList);

}
