package com.example.danieljackson.weatherapp.ui.cities.presenter.model;

import android.support.annotation.NonNull;

import com.example.danieljackson.weatherapp.dagger.SystemModule;

public class City implements Comparable {

    private int cityId;

    private String cityName;

    private int zipCode;

    private Double currentTemp;

    private Integer humidityPercentage;

    private Double windSpeed;

    private Direction windDirection = Direction.NONE;

    private Scale scale;

    private String iconUrl;

    private String description;

    private int listPosition;

    public City(int cityId, String cityName, int zipCode, double currentTemp, Integer humidityPercentage,
                double windSpeed, Direction windDirection, Scale scale, String iconUrl, String description, int listPosition) {
        this.cityId = cityId;
        this.cityName = cityName;
        this.zipCode = zipCode;
        this.currentTemp = currentTemp;
        this.humidityPercentage = humidityPercentage;
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
        if(scale == null) {
            this.scale = Scale.FARENHEIT;
        } else {
            this.scale = scale;
        }
        this.description = description;
        this.iconUrl = iconUrl;
        this.listPosition = listPosition;
    }

    public static class Builder {
        private int cityId;

        private String cityName;

        private int zipCode;

        private double currentTemp;

        private Integer humidityPercentage;

        private double windSpeed;

        private Direction windDirection;

        private Scale scale;

        private String description;

        private String iconUrl;

        private int listPosition;

        public Builder(int cityId, String cityName, int zipCode, int listPostion) {
            this.cityId = cityId;
            this.cityName = cityName;
            this.zipCode = zipCode;
            this.listPosition = listPostion;
        }

        public City build() {
            return new City(cityId, cityName, zipCode, currentTemp, humidityPercentage, windSpeed,
                    windDirection, scale, iconUrl, description, listPosition);
        }

        public Builder setCurrentTemp(double currentTemp) {
            this.currentTemp = currentTemp;
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

        public Builder setIconUrlEnd(String iconUrlEnd) {
            this.iconUrl = SystemModule.ICON_URL_BASE + "/" + iconUrlEnd + ".png";
            return Builder.this;
        }
    }

    public String getCityName() {
        return cityName;
    }

    public int getZipCode() {
        return zipCode;
    }

    public Double getCurrentTemp() {
        return currentTemp;
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

    public String getIconUrl() {
        return iconUrl;
    }

    public String getDescription() {
        return description;
    }

    public int getListPosition() {
        return listPosition;
    }

    public void setListPosition(int listPosition) {
        this.listPosition = listPosition;
    }

    public int getCityId() {
        return cityId;
    }

    public enum Scale {
        FARENHEIT,
        CELSIUS,
        KELVIN;

        public String getSpeedName() {
            if(this.equals(FARENHEIT)) {
                return "MPH";
            } else {
                return "KPH";
            }
        }
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
        NONE;

        @Override
        public String toString() {
            switch (this) {
                case NORTH:
                    return "N";
                case SOUTH:
                    return "S";
                case EAST:
                    return "E";
                case WEST:
                    return "W";
                case NORTHEAST:
                    return "NE";
                case SOUTHEAST:
                    return "SE";
                case SOUTHWEST:
                    return "SW";
                case NORTHWEST:
                    return "NW";
                case NONE:
                    return "";
            }
            return "";
        }
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

    //for search
    public City(int cityId) {
        this.cityId = cityId;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        if (this == o) return 0;
        if (o == null || getClass() != o.getClass()) return -1;

        City that = (City) o;

        if(getCityId() == that.getCityId()) {
            return 0;
        } else {
            return this.listPosition - that.listPosition;
        }
    }
}
