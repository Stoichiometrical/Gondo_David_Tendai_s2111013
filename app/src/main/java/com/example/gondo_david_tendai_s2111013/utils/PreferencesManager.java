//Author: David Tendai Gondo
//StudentID :s2111013

package com.example.gondo_david_tendai_s2111013.utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.gondo_david_tendai_s2111013.model.LatestWeather;
import com.example.gondo_david_tendai_s2111013.model.ForecastWeather;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class PreferencesManager {

    private static Context context;
    public static final String PREF_NAME = "cache";
    public static final String KEY_DEFAULT_CITY = "city";
    public static final String KEY_THEME = "theme";
    public static final String KEY_UPDATE_TIME1 = "updateTime1";
    public static final String KEY_UPDATE_TIME2 = "updateTime2";

    public static final String KEY_LATEST_WEATHER = "latest_weather";
    public static final String KEY_FORECAST_WEATHER = "forecast_weather";
    private Gson gson = new Gson();

    private static SharedPreferences sharedPreferences;

    // Constructor for PreferencesManager. Initializes the SharedPreferences instance.
    public PreferencesManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
    }


    // Updates a single preference in SharedPreferences with the given key and value.
    public void updateSharedPreferences(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void initializeDefaultValues(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        boolean shouldApplyChanges = false; // Flag to track if changes should be applied

        // Check if the update times are empty and set default values
        if (preferences.getString(KEY_UPDATE_TIME1, "").isEmpty()) {
            editor.putString(KEY_UPDATE_TIME1, "08:00");
            shouldApplyChanges = true; // Set the flag to true since a change was made
        }
        if (preferences.getString(KEY_UPDATE_TIME2, "").isEmpty()) {
            editor.putString(KEY_UPDATE_TIME2, "20:00");
            shouldApplyChanges = true; // Set the flag to true since a change was made
        }

        // Check if the theme is empty and set default value
        if (preferences.getString(KEY_THEME, "").isEmpty()) {
            editor.putString(KEY_THEME, "light");
            shouldApplyChanges = true; // Set the flag to true since a change was made
        }

        // Check if the selected campus is empty and set default value
        if (preferences.getString(KEY_DEFAULT_CITY, "").isEmpty()) {
            editor.putString(KEY_DEFAULT_CITY, "Glasgow");
            shouldApplyChanges = true; // Set the flag to true since a change was made
        }

        // Only apply the changes if at least one condition was true
        if (shouldApplyChanges) {
            editor.apply();
        }
    }



    // Updates multiple settings in SharedPreferences, including default city, theme, and update times.
    public void updateSettings(String defaultCity, String theme, String updateTime1, String updateTime2) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_DEFAULT_CITY, defaultCity);
        editor.putString(KEY_THEME, theme);

        // Check if updateTime1 and updateTime2 are null or empty, if so, set default values
        if (updateTime1 == null || updateTime1.isEmpty()) {
            updateTime1 = "08:00"; // Default update time 1: 8 AM
        }
        if (updateTime2 == null || updateTime2.isEmpty()) {
            updateTime2 = "20:00"; // Default update time 2: 8 PM
        }

        editor.putString(KEY_UPDATE_TIME1, updateTime1);
        editor.putString(KEY_UPDATE_TIME2, updateTime2);
        editor.apply();
    }


    // Updates the latest weather data in SharedPreferences by converting the LatestWeather object to JSON
    public void updateLatestCache(LatestWeather latest) {
        String jsonString = gson.toJson(latest);
        sharedPreferences.edit().putString(KEY_LATEST_WEATHER, jsonString).apply();
    }


    // Updates the forecast weather data in SharedPreferences by converting the List of ForecastWeather objects to JSON
    public void updateForecastCache(List<ForecastWeather> forecast) {
        String jsonString = gson.toJson(forecast);
        sharedPreferences.edit().putString(KEY_FORECAST_WEATHER, jsonString).apply();
    }

    // Retrieves the latest weather data from SharedPreferences and converts it from JSON to a LatestWeather object.
    public LatestWeather getCachedLatestWeather() {
        String jsonString = sharedPreferences.getString(KEY_LATEST_WEATHER, "");
        return gson.fromJson(jsonString, LatestWeather.class);
    }

    // Retrieves the forecast weather data from SharedPreferences and converts it from JSON to a List of ForecastWeather objects.
    public List<ForecastWeather> getCachedForecast() {
        String jsonString = sharedPreferences.getString(KEY_FORECAST_WEATHER, "");
        Type listType = new TypeToken<List<ForecastWeather>>(){}.getType();
        return gson.fromJson(jsonString, listType);
    }

    // Retrieves a single cached value from SharedPreferences using the provided key.
    public static String getCachedValue(String key) {
        return sharedPreferences.getString(key, "");
    }

    // Saves the selected theme preference (dark or light) in SharedPreferences.
    public static void saveTheme(AppCompatActivity activity, boolean isDarkTheme) {
        SharedPreferences.Editor editor = activity.getSharedPreferences(PreferencesManager.PREF_NAME, MODE_PRIVATE).edit();
        editor.putString(KEY_THEME, isDarkTheme ? "dark" : "light");
        editor.apply();
    }


    // Applies the saved theme preference to the activity.
    public static void applyTheme(AppCompatActivity activity) {
        SharedPreferences prefs = activity.getSharedPreferences(PreferencesManager.PREF_NAME, MODE_PRIVATE);
        String themeSetting = prefs.getString(KEY_THEME , "light");
        Log.d("XPXG",themeSetting);
        if (themeSetting.equals("dark")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }





}
