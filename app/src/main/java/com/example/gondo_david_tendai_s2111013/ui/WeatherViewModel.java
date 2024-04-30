//Author: David Tendai Gondo
//StudentID :s2111013
package com.example.gondo_david_tendai_s2111013.ui;



import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.gondo_david_tendai_s2111013.model.LatestWeather;
import com.example.gondo_david_tendai_s2111013.repository.WeatherRepository;
import com.example.gondo_david_tendai_s2111013.model.ForecastWeather;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class WeatherViewModel extends ViewModel {

    private final WeatherRepository weatherRepository;
    private final MutableLiveData<LatestWeather> latestWeather = new MutableLiveData<>();
    private final MutableLiveData<List<ForecastWeather>> forecastWeather = new MutableLiveData<>();



    public WeatherViewModel(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;

    }

    public LiveData<LatestWeather> getLatestWeather() {
        return latestWeather;
    }

    public LiveData<List<ForecastWeather>> getForecastWeather() {
        return forecastWeather;
    }

    private final MutableLiveData<String> toastMessage = new MutableLiveData<>();

    public LiveData<String> getToastMessage() {
        return toastMessage;
    }


    public boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }




    public void fetchWeatherData(String locationId,Context context) {
        if (isNetworkConnected(context)) {
            CompletableFuture.allOf(
                    fetchLatestWeatherAsync(locationId),
                    fetchForecastWeatherAsync(locationId)
            ) ;
        } else {

            toastMessage.postValue("No internet connection. Using last update data.");
            Log.d("DFT","No internet connection. Using last update data.");
            CompletableFuture.allOf(
                    fetchLatestWeatherAsync(locationId),
                    fetchForecastWeatherAsync(locationId));

        }
    }



    private CompletableFuture<Void> fetchLatestWeatherAsync(String locationId){
        return CompletableFuture.supplyAsync(() -> {
            try {
                return weatherRepository.getLatestWeather(locationId).get();
            } catch (Exception e) {

                return null;
            }
        }).thenAccept(latestWeather -> this.latestWeather.postValue((LatestWeather) latestWeather));
    }

    private CompletableFuture<Void> fetchForecastWeatherAsync(String locationId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return weatherRepository.getForecastWeather(locationId).get();
            } catch (Exception e) {
                // Handle Error (e.g., log the error or post an error value)
                return null;
            }
        }).thenAccept(forecastWeather -> this.forecastWeather.postValue((List<ForecastWeather>) forecastWeather));
    }










    public static class WeatherViewModelFactory implements ViewModelProvider.Factory {
        private final WeatherRepository weatherRepository;

        public WeatherViewModelFactory(WeatherRepository weatherRepository) {
            this.weatherRepository = weatherRepository;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(WeatherViewModel.class)) {
                return (T) new WeatherViewModel(weatherRepository);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}

