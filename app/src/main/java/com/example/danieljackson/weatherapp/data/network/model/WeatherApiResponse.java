package com.example.danieljackson.weatherapp.data.network.model;


import android.support.annotation.VisibleForTesting;

import com.google.gson.annotations.SerializedName;

public class WeatherApiResponse {

    private String name;

    @SerializedName("weather")
    private DetailedDescription weatherDescription;

    @SerializedName("main")
    private Measurements tempMeasurements;

    private Wind wind;

    @VisibleForTesting
    public WeatherApiResponse(String name, DetailedDescription weatherDescription, Measurements tempMeasurements, Wind wind) {
        this.name = name;
        this.weatherDescription = weatherDescription;
        this.tempMeasurements = tempMeasurements;
        this.wind = wind;
    }

    public String getName() {
        return name;
    }

    public DetailedDescription getWeatherDescription() {
        return weatherDescription;
    }

    public Measurements getTempMeasurements() {
        return tempMeasurements;
    }

    public Wind getWind() {
        return wind;
    }
}
