package com.example.danieljackson.weatherapp.dagger;

import com.example.danieljackson.weatherapp.data.network.WeatherApi;
import com.example.danieljackson.weatherapp.data.persistence.SystemPersistence;
import com.example.danieljackson.weatherapp.data.strings.SystemMessaging;
import com.example.danieljackson.weatherapp.ui.cities.presenter.CitiesPresenter;
import com.example.danieljackson.weatherapp.ui.cities.presenter.CitiesPresenterImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Singleton
@Module
public class PresenterModule {

    @Provides
    static CitiesPresenter providesCitiesPresenter(WeatherApi weatherApi, SystemMessaging systemMessaging,
                                                   SystemPersistence systemPersistence) {
        return new CitiesPresenterImpl(weatherApi, systemMessaging, systemPersistence);
    }
}
