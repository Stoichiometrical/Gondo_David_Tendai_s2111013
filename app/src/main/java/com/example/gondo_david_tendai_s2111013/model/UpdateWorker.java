//Author: David Tendai Gondo
//StudentID :s2111013

package com.example.gondo_david_tendai_s2111013.model;

import static com.example.gondo_david_tendai_s2111013.utils.PreferencesManager.KEY_DEFAULT_CITY;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.gondo_david_tendai_s2111013.repository.WeatherRepository;
import com.example.gondo_david_tendai_s2111013.utils.AppStateTracker;
import com.example.gondo_david_tendai_s2111013.utils.UpdateUtils;

import android.util.Log;

import com.example.gondo_david_tendai_s2111013.utils.PreferencesManager;

import java.util.List;

public class UpdateWorker extends Worker {

    private final WeatherRepository weatherRepository;
    private final PreferencesManager preferencesManager;

    public UpdateWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.weatherRepository = new WeatherRepository(context);
        this.preferencesManager = new PreferencesManager(context);
    }

    @NonNull
    @Override
    public Result doWork() {

        // Fetch default city from preferences
        String defaultCity = PreferencesManager.getCachedValue(KEY_DEFAULT_CITY);

        // Fetch latest weather data
        LatestWeather latestWeather;
        List<ForecastWeather> forecastUpdate;
        try {
            latestWeather = (LatestWeather) weatherRepository.getLatestWeather(defaultCity).get();
            forecastUpdate  = (List<ForecastWeather>) weatherRepository.getForecastWeather(defaultCity).get();

            UpdateUtils.sendWeatherNotification(this.getApplicationContext(), latestWeather);

        } catch (Exception e) {
            Log.e("WeatherFetchWorker", "Error fetching weather data", e);
            return Result.failure();
        }

        // Send notification (if app is in background)
        if (!AppStateTracker.isAppInForeground()) {
           UpdateUtils.sendWeatherNotification(getApplicationContext(), latestWeather);
        }

        return Result.success();
    }
}
