package com.example.danieljackson.weatherapp.data.network.model;

import android.support.annotation.VisibleForTesting;

public class DetailedDescription {

    private String description;


    @VisibleForTesting
    public DetailedDescription(String main, String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
