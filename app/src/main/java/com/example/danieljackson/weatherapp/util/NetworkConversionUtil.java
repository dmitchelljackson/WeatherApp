package com.example.danieljackson.weatherapp.util;

import com.example.danieljackson.weatherapp.data.network.model.WeatherApiResponse;
import com.example.danieljackson.weatherapp.data.strings.SystemMessaging;
import com.example.danieljackson.weatherapp.ui.cities.presenter.model.City;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class NetworkConversionUtil {

    private static final String TAG = NetworkConversionUtil.class.getSimpleName();

    public static City cityFrom(WeatherApiResponse weatherApiResponse, int zipCode) {
        return new City.Builder(weatherApiResponse.getId(), weatherApiResponse.getName(), zipCode, Integer.MAX_VALUE)
                .setCurrentTemp(weatherApiResponse.getTempMeasurements().getTempKelvin())
                .setHumidityPercentage((int) weatherApiResponse.getTempMeasurements().getHumidityPercentage())
                .setWindSpeed(weatherApiResponse.getWind().getSpeedKilometersPerHour())
                .setWindDirection(MeasurementConversionUtil.convertBearing(weatherApiResponse.getWind().getDegree()))
                .setIconUrlEnd(weatherApiResponse.getWeatherDescription().getIcon())
                .setScale(City.Scale.FARENHEIT)
                .setDescription(weatherApiResponse.getWeatherDescription().getDescription())
                .build();
    }


    public static SortedSet<City> mapApiResponsesToCities(SortedSet<City> cities, List<WeatherApiResponse> weatherApiResponseList, SystemMessaging systemMessaging) {
        List<City> citySearchList = new ArrayList<>(cities);
        Comparator<City> cityIdComparator = (city1, city2) -> city1.getCityId() - city2.getCityId();
        Collections.sort(citySearchList, cityIdComparator);

        SortedSet<City> resultSet = new TreeSet<>();
        for(WeatherApiResponse weatherApiResponse : weatherApiResponseList) {
            int cityIndex = Collections.binarySearch(citySearchList, new City(weatherApiResponse.getId()), cityIdComparator);
            if(cityIndex > -1) {
                City previousCity = citySearchList.get(cityIndex);
                City resultCity = cityFrom(weatherApiResponse, previousCity.getZipCode());
                resultCity.setListPosition(previousCity.getListPosition());
                resultSet.add(resultCity);
            } else {
                systemMessaging.e(TAG, "Invalid result " + weatherApiResponse.getName() + "returned from the api");
            }
        }

        return resultSet;
    }

}
