package com.example.danieljackson.weatherapp.ui.cities.presenter;


import com.example.danieljackson.weatherapp.data.network.model.WeatherApiResponse;

import io.reactivex.Observable;


public interface CitiesWeatherApi {
    Observable<WeatherApiResponse> getWeatherForCity(String cityName);
    Observable<WeatherApiResponse> getWeatherForCity(int cityCode);
}
