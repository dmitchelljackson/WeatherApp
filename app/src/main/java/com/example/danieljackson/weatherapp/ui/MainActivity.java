package com.example.danieljackson.weatherapp.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.danieljackson.weatherapp.R;
import com.example.danieljackson.weatherapp.ui.cities.CitiesFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getFragmentManager().beginTransaction().replace(R.id.frame, new CitiesFragment(), CitiesFragment.class.getSimpleName()).commit();
    }
}
