package com.example.danieljackson.weatherapp.util;

import com.example.danieljackson.weatherapp.data.network.model.WeatherApiResponse;
import com.example.danieljackson.weatherapp.ui.cities.presenter.model.City;

public class NetworkConversionUtil {

    public static City cityFrom(WeatherApiResponse weatherApiResponse, int zipCode) {
        return new City.Builder(weatherApiResponse.getId(), weatherApiResponse.getName(), zipCode)
                .setCurrentTemp(weatherApiResponse.getTempMeasurements().getTempKelvin())
                .setHumidityPercentage((int) weatherApiResponse.getTempMeasurements().getHumidityPercentage())
                .setWindSpeed(weatherApiResponse.getWind().getSpeedKilometersPerHour())
                .setWindDirection(MeasurementConversionUtil.convertBearing(weatherApiResponse.getWind().getDegree()))
                .setIconUrlEnd(weatherApiResponse.getWeatherDescription().getIcon())
                .setScale(City.Scale.FARENHEIT)
                .setDescription(weatherApiResponse.getWeatherDescription().getDescription())
                .build();
    }
}
