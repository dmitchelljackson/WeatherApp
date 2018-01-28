package com.example.danieljackson.weatherapp.ui.cities.presenter.model;

import android.support.annotation.NonNull;

public class City implements Comparable{

    private int cityId;

    private String cityName;

    private Double currentTemp;

    private Double highTemp;

    private Double lowTemp;

    private Integer humidityPercentage;

    private Double windSpeed;

    private Direction windDirection = Direction.NONE;

    private Scale scale = Scale.FARENHEIT;

    private String description;

    private int listPosition;

    public City(int cityId, String cityName, double currentTemp, double highTemp, double lowTemp, Integer humidityPercentage,
                double windSpeed, Direction windDirection, Scale scale, String description) {
        this.cityId = cityId;
        this.cityName = cityName;
        this.currentTemp = currentTemp;
        this.highTemp = highTemp;
        this.lowTemp = lowTemp;
        this.humidityPercentage = humidityPercentage;
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
        this.scale = scale;
        this.description = description;
    }

    public static class Builder {
        private int cityId;

        private String cityName;

        private double currentTemp;

        private double highTemp;

        private double lowTemp;

        private Integer humidityPercentage;

        private double windSpeed;

        private Direction windDirection;

        private Scale scale;

        private String description;

        public Builder(int cityId, String cityName) {
            this.cityId = cityId;
            this.cityName = cityName;
        }

        public City build() {
            return new City(cityId, cityName, currentTemp, highTemp, lowTemp, humidityPercentage, windSpeed,
                    windDirection, scale, description);
        }

        public Builder setCurrentTemp(double currentTemp) {
            this.currentTemp = currentTemp;
            return Builder.this;
        }

        public Builder setHighTemp(double highTemp) {
            this.highTemp = highTemp;
            return Builder.this;
        }

        public Builder setLowTemp(double lowTemp) {
            this.lowTemp = lowTemp;
            return Builder.this;
        }

        public Builder setHumidityPercentage(Integer humidityPercentage) {
            this.humidityPercentage = humidityPercentage;
            return Builder.this;
        }

        public Builder setWindSpeed(double windSpeed) {
            this.windSpeed = windSpeed;
            return Builder.this;
        }

        public Builder setWindDirection(Direction windDirection) {
            this.windDirection = windDirection;
            return Builder.this;
        }

        public Builder setScale(Scale scale) {
            this.scale = scale;
            return Builder.this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return Builder.this;
        }
    }

    public String getCityName() {
        return cityName;
    }

    public Double getCurrentTemp() {
        return currentTemp;
    }

    public Double getHighTemp() {
        return highTemp;
    }

    public Double getLowTemp() {
        return lowTemp;
    }

    public Integer getHumidityPercentage() {
        return humidityPercentage;
    }

    public Double getWindSpeed() {
        return windSpeed;
    }

    public Direction getWindDirection() {
        return windDirection;
    }

    public Scale getScale() {
        return scale;
    }

    public int getListPosition() {
        return listPosition;
    }

    void setListPosition(int listPosition) {
        this.listPosition = listPosition;
    }

    public int getCityId() {
        return cityId;
    }

    public enum Scale {
        FARENHEIT,
        CELSIUS,
        KELVIN
    }

    public enum Direction {
        NORTH,
        SOUTH,
        EAST,
        WEST,
        NORTHWEST,
        SOUTHWEST,
        NORTHEAST,
        SOUTHEAST,
        NONE
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        City city = (City) o;

        return city.getCityId() == getCityId();

    }

    @Override
    public int hashCode() {
        return cityId;
    }


    @Override
    public int compareTo(@NonNull Object o) {
        if (this == o) return 0;
        if (o == null || getClass() != o.getClass()) return -1;

        City that = (City) o;

        return this.listPosition - that.listPosition;
    }
}
