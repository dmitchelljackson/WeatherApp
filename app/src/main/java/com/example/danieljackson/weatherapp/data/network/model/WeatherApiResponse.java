package com.example.danieljackson.weatherapp.data.network.model;


import android.support.annotation.VisibleForTesting;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class WeatherApiResponse {

    private List<WeatherApiResponse> list;

    private String name;

    private int id;

    @SerializedName("weather")
    private List<DetailedDescription> weatherDescriptions;

    @SerializedName("main")
    private Measurements tempMeasurements;

    @SerializedName("coord")
    private Coordinates coordinates;

    private Wind wind;

    @VisibleForTesting
    public WeatherApiResponse(String name, List<DetailedDescription> weatherDescriptions, Measurements tempMeasurements, Wind wind) {
        this.name = name;
        this.weatherDescriptions = weatherDescriptions;
        this.tempMeasurements = tempMeasurements;
        this.wind = wind;
    }

    public String getName() {
        return name;
    }

    public DetailedDescription getWeatherDescription() {
        return weatherDescriptions.get(0);
    }

    public Measurements getTempMeasurements() {
        return tempMeasurements;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public List<WeatherApiResponse> getList() {
        if(list != null) {
            return list;
        } else {
            List<WeatherApiResponse> list = new ArrayList<>();
            list.add(this);
            return list;
        }
    }

    public Wind getWind() {
        return wind;
    }

    public int getId() {
        return id;
    }
}
