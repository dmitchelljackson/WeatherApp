package com.example.danieljackson.weatherapp.dagger;

import com.example.danieljackson.weatherapp.ui.cities.CitiesFragment;
import com.example.danieljackson.weatherapp.ui.cities.CityCardView;

import dagger.Component;

@Component(modules = {SystemModule.class, PresenterModule.class})
public interface AppComponent {

    void inject(CitiesFragment fragment);
    void inject(CityCardView view);
}
