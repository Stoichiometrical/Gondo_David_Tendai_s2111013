package com.example.gondo_david_tendai_s2111013.ui.home;



import static com.example.gondo_david_tendai_s2111013.utils.HelperMethods.extractDate;
import static com.example.gondo_david_tendai_s2111013.utils.PreferencesManager.PREF_NAME;
import static com.example.gondo_david_tendai_s2111013.utils.PreferencesManager.applyTheme;


import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;


import com.example.gondo_david_tendai_s2111013.R;
import com.example.gondo_david_tendai_s2111013.repository.WeatherRepository;
import com.example.gondo_david_tendai_s2111013.ui.detailed.DetailedForecastsActivity;
import com.example.gondo_david_tendai_s2111013.ui.settings.SettingsActivity;
import com.example.gondo_david_tendai_s2111013.utils.HelperMethods;
import com.example.gondo_david_tendai_s2111013.model.ForecastWeather;
import com.example.gondo_david_tendai_s2111013.model.LatestWeather;
import com.example.gondo_david_tendai_s2111013.ui.WeatherViewModel;
import com.example.gondo_david_tendai_s2111013.utils.PreferencesManager;
import com.example.gondo_david_tendai_s2111013.utils.UpdateUtils;

import com.google.android.material.bottomnavigation.BottomNavigationView;
//Author: David Tendai Gondo
//StudentID :s2111013

import java.io.Serializable;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private WeatherViewModel viewModel;



    String selectedCityId;
    private String cityId;

    static ForecastWeather forecast_today,tommorrow,future;

    private TextView temp ,wind,rain,uv,humidity,t_temp,f_temp,rain_dec,tips,pressure,dateToday;
    private Spinner spinner;
    private BottomNavigationView navigationView;
    private ImageView main_rain,t_rain,f_rain;

    private List<ForecastWeather> forecastWeather;

    PreferencesManager preferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize your PreferencesManager
        preferencesManager = new PreferencesManager(this);
        applyTheme(this);
        SharedPreferences themes = getSharedPreferences(PREF_NAME,MODE_PRIVATE);



        setContentView(R.layout.activity_home);
        initializeViews();




        // Extract LatestWeather object from the intent
        LatestWeather latestWeather = (LatestWeather) getIntent().getSerializableExtra("latestWeather");

        // Extract List<ForecastWeather> object from the intent
        List<ForecastWeather> forecastWeatherList = (List<ForecastWeather>) getIntent().getSerializableExtra(HelperMethods.FORECAST);

        // Initialize the views with the latest weather and forecast data
        if (latestWeather != null) {
            updateLatestWeatherUI(latestWeather);
        }
        if (forecastWeatherList != null) {
            updateForecastWeatherUI(forecastWeatherList);
        }


        // Create an instance of WeatherRepository
        WeatherRepository weatherRepository = new WeatherRepository(this);

        // Create an instance of the HomeViewModelFactory
        HelperMethods.ViewModelFactory factory = new HelperMethods.ViewModelFactory(weatherRepository);

        // Obtain the WeatherViewModel instance using the WeatherViewModelFactory
        viewModel = new ViewModelProvider(this, factory).get(WeatherViewModel.class);





        SettingsActivity.scheduleUpdateWork(getApplicationContext());


        //Set up spinner adapter
        HelperMethods.setupSpinnerAdapter(this,spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCityId = parent.getItemAtPosition(position).toString();
                cityId  = HelperMethods.getCityId(selectedCityId);
                fetchAndObserveData(cityId);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle optional case when nothing is selected
            }
        });

        navigationView.setOnItemSelectedListener(item -> HelperMethods.goToActivity(item, HomeActivity.this));

        UpdateUtils.createNotificationChannel(this);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        updateLatestWeatherUI(viewModel.getLatestWeather().getValue());
        updateForecastWeatherUI(viewModel.getForecastWeather().getValue());
    }


    private void initializeViews() {
        spinner = findViewById(R.id.spinners);
        dateToday = findViewById(R.id.date);

        temp = findViewById(R.id.main_temp);
        wind = findViewById(R.id.wind_value);
        rain = findViewById(R.id.rain_value);
        pressure = findViewById(R.id.uv_value);
        humidity = findViewById(R.id.hum_value);
        t_temp = findViewById(R.id.tom_temp);
        f_temp = findViewById(R.id.future_temp);
        main_rain = findViewById(R.id.weather_icon);
        t_rain = findViewById(R.id.tom_icon);
        f_rain = findViewById(R.id.future_icon);
        rain_dec = findViewById(R.id.weather_desc);
        tips = findViewById(R.id.tips);
        navigationView = findViewById(R.id.bottoms);
    }

    private void fetchAndObserveData(String locationId) {
        viewModel.fetchWeatherData(locationId,this);
        viewModel.getLatestWeather().observe(this, this::updateLatestWeatherUI);
        viewModel.getForecastWeather().observe(this, this::updateForecastWeatherUI);

    }

    private void updateLatestWeatherUI(LatestWeather latestWeather) {
        Log.d("HomeActivity", latestWeather.toString());
        UpdateUtils.sendWeatherNotification(this, latestWeather);
        if (latestWeather != null) {

           String todayDate = extractDate(latestWeather.getDate());
           dateToday.setText("Today : "+ todayDate);

            wind.setText(latestWeather.getWind_speed());

            pressure.setText(latestWeather.getPressure());
            humidity.setText(latestWeather.getHumidity());

            temp.setText(latestWeather.getTemp() + "°C");

            HelperMethods.callGeminiApi(latestWeather.toString(),tips);

        }
    }

    private void updateForecastWeatherUI(List<ForecastWeather> forecastWeather) {
        this.forecastWeather = forecastWeather;

        if (forecastWeather != null) {
            Log.d("OPRS",forecastWeather.get(0).toString());
            forecast_today= forecastWeather.get(0);
            tommorrow = forecastWeather.get(1);
            future=forecastWeather.get(2);

            rain.setText(forecast_today.getRain());
            rain_dec.setText(forecast_today.getRain());
            t_temp.setText(tommorrow.getMax_temp()+"°");
            f_temp.setText(future.getMax_temp()+"°");

            String rainDescription = forecast_today.getRain();
            HelperMethods.processRainDescription(rainDescription,main_rain);

            // For tomorrow's weather
            String tomorrowsRain = tommorrow.getRain();
            HelperMethods.processRainDescription(tomorrowsRain, t_rain);

            // For the future forecast
            String futureRain = future.getRain();
            HelperMethods.processRainDescription(futureRain, f_rain);

        }


    }



    public void  goToDetail(View view){
        Intent i = new Intent(this, DetailedForecastsActivity.class);
        i.putExtra(HelperMethods.FORECAST,(Serializable) forecastWeather);
        i.putExtra("location", selectedCityId);
        startActivity(i);

    }


}