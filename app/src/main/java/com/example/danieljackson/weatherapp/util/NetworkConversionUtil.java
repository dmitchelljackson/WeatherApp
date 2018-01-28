package com.example.danieljackson.weatherapp.util;

import com.example.danieljackson.weatherapp.data.network.model.WeatherApiResponse;
import com.example.danieljackson.weatherapp.ui.cities.presenter.model.City;

public class NetworkConversionUtil {

    public static City cityFrom(WeatherApiResponse weatherApiResponse) {
        return new City.Builder(weatherApiResponse.getId(), weatherApiResponse.getName())
                .setCurrentTemp(weatherApiResponse.getTempMeasurements().getTempKelvin())
                .setHighTemp(weatherApiResponse.getTempMeasurements().getMaxTemperatureKelvin())
                .setLowTemp(weatherApiResponse.getTempMeasurements().getMinimumTemperatureKelvin())
                .setHumidityPercentage((int) weatherApiResponse.getTempMeasurements().getHumidityPercentage())
                .setWindSpeed(weatherApiResponse.getWind().getSpeedKilometersPerHour())
                .setWindDirection(City.Direction.NONE)
                .setScale(City.Scale.KELVIN)
                .setDescription(weatherApiResponse.getWeatherDescription().getDescription())
                .build();
    }
}
