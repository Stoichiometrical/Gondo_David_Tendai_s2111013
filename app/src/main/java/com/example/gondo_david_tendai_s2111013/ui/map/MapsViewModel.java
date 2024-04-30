//Author: David Tendai Gondo
//StudentID :s2111013

package com.example.gondo_david_tendai_s2111013.ui.map;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gondo_david_tendai_s2111013.model.ForecastWeather;
import com.example.gondo_david_tendai_s2111013.repository.WeatherRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

//public class MapsViewModel extends ViewModel {
//
//    private final WeatherRepository weatherRepository;
//    private final MutableLiveData<List<ForecastWeather>> forecastWeather = new MutableLiveData<>();
//
//
//    public MapsViewModel(WeatherRepository weatherRepository) {
//        this.weatherRepository = weatherRepository;
//    }
//
//
//    public LiveData<List<ForecastWeather>> getForecastWeather() {
//        return forecastWeather;
//    }
//
//
//    public void fetchWeatherData(String locationId) {
//        CompletableFuture.allOf(
//
//                fetchForecastWeatherAsync(locationId)
//        ); // Handle results here
//    }
//
//
//    private CompletableFuture<Void> fetchForecastWeatherAsync(String locationId) {
//        return CompletableFuture.supplyAsync(() -> {
//            try {
//                return weatherRepository.getForecastWeather(locationId).get();
//            } catch (Exception e) {
//                // Handle Error (e.g., log the error or post an error value)
//                return null;
//            }
//        }).thenAccept(forecastWeather -> this.forecastWeather.postValue((List<ForecastWeather>) forecastWeather));
//    }
//
//
//
//
//
//}


import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class MapsViewModel extends ViewModel {
    private final WeatherRepository weatherRepository;
    private final MutableLiveData<List<ForecastWeather>> forecastWeather = new MutableLiveData<>();

    public MapsViewModel(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    public LiveData<List<ForecastWeather>> getForecastWeather() {
        return forecastWeather;
    }

    public void fetchWeatherData(List<String> locationIds) {
        List<CompletableFuture<ForecastWeather>> futures = locationIds.stream()
                .map(this::fetchFirstForecastAsync)
                .collect(Collectors.toList());

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenAccept(v -> {
                    List<ForecastWeather> firstForecasts = new ArrayList<>();
                    for (CompletableFuture<ForecastWeather> future : futures) {
                        try {
                            firstForecasts.add(future.get());
                        } catch (InterruptedException | ExecutionException e) {
                            // Handle exceptions
                            e.printStackTrace();
                        }
                    }
                    forecastWeather.postValue(firstForecasts);
                });
    }

    private CompletableFuture<ForecastWeather> fetchFirstForecastAsync(String locationId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                List<ForecastWeather> forecasts = (List<ForecastWeather>) weatherRepository.getForecastWeather(locationId).get();
                Log.d("MapsViewModel", "Retrieved Forecast: " + forecasts.get(0));
                return forecasts.isEmpty() ? null : forecasts.get(0);
            } catch (Exception e) {
                // Handle Error (e.g., log the error or post an error value)
                return null;
            }
        });
    }
}
