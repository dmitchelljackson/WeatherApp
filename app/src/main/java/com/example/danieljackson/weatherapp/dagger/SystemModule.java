package com.example.danieljackson.weatherapp.dagger;

import android.content.Context;

import com.example.danieljackson.weatherapp.data.network.WeatherApi;
import com.example.danieljackson.weatherapp.data.persistence.PreferencePersistence;
import com.example.danieljackson.weatherapp.data.persistence.SystemPersistence;
import com.example.danieljackson.weatherapp.data.strings.AndroidMessageRetriever;
import com.example.danieljackson.weatherapp.data.strings.SystemMessaging;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Singleton
@Module
public class SystemModule {

    //TODO if ever moved to production you would want to obfuscate the api key
    private static final String API_ID_KEY = "appid";
    private static final String API_ID_VALUE = "8564e740b6afc8dad424b57443758623";

    private Context context;

    public SystemModule(Context context) {
        this.context = context;
    }

    @Provides
    SystemPersistence providesSystemPersistence() {
        return new PreferencePersistence(context);
    }

    @Provides
    SystemMessaging providesSystemMessaging() {
        return new AndroidMessageRetriever(context);
    }

    @Provides
    WeatherApi providesWeatherApi() {

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(chain -> {
            HttpUrl url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter(API_ID_KEY, API_ID_VALUE).build();
            return chain.proceed(chain.request().newBuilder().url(url).build());
        }).addInterceptor(httpLoggingInterceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return retrofit.create(WeatherApi.class);
    }

}
