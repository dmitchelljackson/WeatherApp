package com.example.danieljackson.weatherapp.data.network.model;

import android.support.annotation.VisibleForTesting;

import com.google.gson.annotations.SerializedName;

public class Wind {

    @SerializedName("speed")
    private Double speedKilometersPerHour;

    @SerializedName("deg")
    private Integer degree;

    @VisibleForTesting
    public Wind(Double speedKilometersPerHour, Integer degree) {
        this.speedKilometersPerHour = speedKilometersPerHour;
        this.degree = degree;
    }

    public Double getSpeedKilometersPerHour() {
        return speedKilometersPerHour;
    }

    public Integer getDegree() {
        return degree;
    }
}
