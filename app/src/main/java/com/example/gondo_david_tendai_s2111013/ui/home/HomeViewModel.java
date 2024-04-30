//Author: David Tendai Gondo
//StudentID :s2111013

package com.example.gondo_david_tendai_s2111013.ui.home;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gondo_david_tendai_s2111013.repository.WeatherRepository;
import com.example.gondo_david_tendai_s2111013.model.ForecastWeather;
import com.example.gondo_david_tendai_s2111013.model.LatestWeather;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;


import java.util.concurrent.CompletableFuture;

public class HomeViewModel extends ViewModel {

    private final WeatherRepository weatherRepository;
    private final MutableLiveData<LatestWeather> latestWeather = new MutableLiveData<>();
    private final MutableLiveData<List<ForecastWeather>> forecastWeather = new MutableLiveData<>();

    public HomeViewModel(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;

    }



    public LiveData<LatestWeather> getLatestWeather() {
        return latestWeather;
    }

    public LiveData<List<ForecastWeather>> getForecastWeather() {
        return forecastWeather;
    }

    public void fetchWeatherData(String locationId) {
        CompletableFuture.allOf(
                fetchLatestWeatherAsync(locationId),
                fetchForecastWeatherAsync(locationId)
        ).thenAccept(v -> handleFetchResults()); // Handle results here
    }

    private CompletableFuture<Void> fetchLatestWeatherAsync(String locationId){
        return CompletableFuture.supplyAsync(() -> {
            try {
                return weatherRepository.getLatestWeather(locationId).get();
            } catch (Exception e) {
                // Handle Error (e.g., log the error or post an error value)
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


    private void handleFetchResults() {
        if (latestWeather.getValue() == null || forecastWeather.getValue() == null) {
            // Handle the case where one or both fetches failed
            // Example: post an error value to a LiveData for the UI to display
        } else {
            // Both fetches succeeded
            // Example: hide loading indicator (if you have one)
        }
    }


    public static class HomeViewModelFactory implements ViewModelProvider.Factory {
        private final WeatherRepository weatherRepository;

        public HomeViewModelFactory(WeatherRepository weatherRepository) {
            this.weatherRepository = weatherRepository;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(HomeViewModel.class)) {
                return (T) new HomeViewModel(weatherRepository);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
