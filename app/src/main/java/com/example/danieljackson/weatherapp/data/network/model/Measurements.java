package com.example.danieljackson.weatherapp.data.network.model;

import android.support.annotation.VisibleForTesting;

import com.google.gson.annotations.SerializedName;

public class Measurements {

    @SerializedName("temp")
    private double tempKelvin;

    @SerializedName("humidity")
    private double humidityPercentage;

    @SerializedName("temp_min")
    private double minimumTemperatureKelvin;

    @SerializedName("temp_max")
    private double maxTemperatureKelvin;

    @VisibleForTesting
    public Measurements(double tempKelvin, double humidityPercentage, double minimumTemperatureKelvin, double maxTemperatureKelvin) {
        this.tempKelvin = tempKelvin;
        this.humidityPercentage = humidityPercentage;
        this.minimumTemperatureKelvin = minimumTemperatureKelvin;
        this.maxTemperatureKelvin = maxTemperatureKelvin;
    }

    public double getTempKelvin() {
        return tempKelvin;
    }

    public double getHumidityPercentage() {
        return humidityPercentage;
    }

    public double getMinimumTemperatureKelvin() {
        return minimumTemperatureKelvin;
    }

    public double getMaxTemperatureKelvin() {
        return maxTemperatureKelvin;
    }
}
