package com.example.danieljackson.weatherapp.util;


import com.example.danieljackson.weatherapp.ui.cities.presenter.model.City;

public class MeasurementConversionUtil {

    public static Double convertFromKelvin(City.Scale scale, double kelvinTemp) {
        if(scale.equals(City.Scale.CELSIUS)) {
            return kelvinTemp - 273.15d;
        } else if(scale.equals(City.Scale.FARENHEIT)) {
            return (((kelvinTemp - 273d) * 9d/5d) + 32d);
        } else {
            return kelvinTemp;
        }
    }

    public static Double convertSpeed(City.Scale scale, Double speed) {
        if(scale.equals(City.Scale.CELSIUS)) {
            return speed;
        } else if(scale.equals(City.Scale.FARENHEIT)) {
            return speed * 0.621371;
        } else {
            return speed;
        }
    }

    public static City.Direction convertBearing(double bearing) {
        if (bearing < 0 && bearing > -180) {
            // Normalize to [0,360]
            bearing = 360.0 + bearing;
        }
        if (bearing > 360 || bearing < -180) {
            return City.Direction.NONE;
        }

        City.Direction directions[] = {
                City.Direction.NORTH, City.Direction.NORTHEAST, City.Direction.EAST, City.Direction.SOUTHEAST, City.Direction.SOUTH,
                City.Direction.SOUTHWEST, City.Direction.WEST, City.Direction.NORTHWEST,};
        return directions[(int) Math.floor(((bearing + 11.25) % 360) / 45)];
    }
}
