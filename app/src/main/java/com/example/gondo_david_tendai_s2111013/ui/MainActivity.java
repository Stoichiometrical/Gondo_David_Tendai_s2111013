package com.example.gondo_david_tendai_s2111013.ui;

import static com.example.gondo_david_tendai_s2111013.utils.PreferencesManager.initializeDefaultValues;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import com.example.gondo_david_tendai_s2111013.R;
import com.example.gondo_david_tendai_s2111013.utils.HelperMethods;
import com.example.gondo_david_tendai_s2111013.model.ForecastWeather;
import com.example.gondo_david_tendai_s2111013.repository.WeatherRepository;
import com.example.gondo_david_tendai_s2111013.ui.home.HomeActivity;
import com.example.gondo_david_tendai_s2111013.utils.PreferencesManager;


import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;



public class MainActivity extends AppCompatActivity {

    private WeatherViewModel viewModel;

    PreferencesManager preferencesManager;

    String locationId;
    String defaultCityId;
    private TextView fetchingMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fetchingMessage = findViewById(R.id.fetchingMessage);

        // Initialize PreferencesManager
        preferencesManager = new PreferencesManager(this);
        initializeDefaultValues(this);

        // Create an instance of WeatherRepository
        WeatherRepository weatherRepository = new WeatherRepository(this);

        // Create an instance of the HomeViewModelFactory
        HelperMethods.ViewModelFactory factory = new HelperMethods.ViewModelFactory(weatherRepository);

        // Obtain the WeatherViewModel instance using the WeatherViewModelFactory
        viewModel = new ViewModelProvider(this, factory).get(WeatherViewModel.class);

        // Start the throb animation on the ImageView
        ImageView icon = findViewById(R.id.icon);
        Animation throbAnimation = AnimationUtils.loadAnimation(this, R.anim.throb);
        icon.startAnimation(throbAnimation);

        // Fetch the default city ID from SharedPreferences
        defaultCityId = preferencesManager.getCachedValue(PreferencesManager.KEY_DEFAULT_CITY);

        if(defaultCityId == null || defaultCityId.isEmpty()){
            defaultCityId = "Glasgow";
        }
        locationId = HelperMethods.getCityId(defaultCityId);
        fetchAndObserveData(locationId);

        viewModel.getToastMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String message) {
                if (message != null) {
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void fetchAndObserveData(String locationId) {
        viewModel.fetchWeatherData(locationId,this);
        viewModel.getLatestWeather().observe(this, latestWeather -> {
            if (latestWeather != null) {

                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                intent.putExtra("latestWeather", latestWeather);
                startActivity(intent);
            } else {
                Log.d("xvt", "LatestWeather is null");
                fetchingMessage.setText("Failed to load  latest weather data");
            }
        });

        viewModel.getForecastWeather().observe(this, forecastWeatherList -> {
            if (forecastWeatherList != null) {

                for (ForecastWeather forecast : forecastWeatherList) {
                    Log.d("xvt", "ForecastWeather: " + forecast.toString());
                }
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                intent.putExtra(HelperMethods.FORECAST, (Serializable) forecastWeatherList);
                startActivity(intent);
            } else {
                Log.d("xvt", "ForecastWeatherList is null");
                fetchingMessage.setText("Failed to load  forecast weather data");
            }
        });
    }
}