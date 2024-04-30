//Author: David Tendai Gondo
//StudentID :s2111013

package com.example.gondo_david_tendai_s2111013.utils;

import android.content.res.Resources;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.net.ParseException;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;


import com.example.gondo_david_tendai_s2111013.R;
import com.example.gondo_david_tendai_s2111013.repository.WeatherRepository;
import com.example.gondo_david_tendai_s2111013.ui.WeatherViewModel;
import com.example.gondo_david_tendai_s2111013.ui.home.CustomSpinnerAdapter;
import com.example.gondo_david_tendai_s2111013.ui.map.MapsActivity;
import com.example.gondo_david_tendai_s2111013.ui.map.MapsViewModel;
import com.example.gondo_david_tendai_s2111013.ui.settings.SettingsActivity;

import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.android.gms.maps.model.LatLng;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HelperMethods {

    public static final LatLng GLASGOW = new LatLng(55.8651912,-4.2517849);
    public static final LatLng NEW_YORK = new LatLng(40.9164665,-73.9387039);
    public static  final String FORECAST = "forecast";


    //Extract  the date and time from a given text string.
    public static String getDateTime(String text){
        String[] values = text.split("\\s+");
        String date = values[0] + " "+ values[1] + "" + values[2] + " "+ values[2];
        return date;

    }

    //Extract  weather details such as temperature, wind direction, wind speed, humidity, pressure, and visibility from a given text string.
    public static String[] getWeatherDetails(String text) {
        String regexPattern = "Temperature: (.*?), Wind Direction: (.*?), Wind Speed: (.*?), Humidity: (.*?), Pressure: (.*?), Visibility: (.*?)$";

        // Compile the pattern
        Pattern pattern = Pattern.compile(regexPattern);

        // Create a Matcher object and match it against the input text
        Matcher matcher = pattern.matcher(text);

        // Check if the pattern matches the input text
        if (matcher.find()) {
            // Create an array to store the extracted values
            String[] weatherDetails = new String[6];



            // Extract and store each piece of information
            weatherDetails[0] = matcher.group(1); // Temperature
            weatherDetails[1] = matcher.group(2); // Wind Direction
            weatherDetails[2] = matcher.group(3); // Wind Speed
            weatherDetails[3] = matcher.group(4); // Humidity
            weatherDetails[4] = matcher.group(5); // Pressure
            weatherDetails[5] = matcher.group(6); // Visibility

            return weatherDetails;
        }

        // Return null if no match is found
        return null;
    }

    //Extract  forecast details such as maximum and minimum temperatures, wind direction, wind speed, visibility, pressure, humidity, UV risk, pollution, sunrise, and sunset from a given text string.
    public static  String[] getForecastDetails(String text){
        String[] weatherDetails = new String[9];
        Log.d("Input String", text);

        // Define the regular expression pattern
        String regexPattern = "Maximum Temperature: (.*?), Minimum Temperature: (.*?), Wind Direction: (.*?), Wind Speed: (.*?), Visibility: (.*?), Pressure: (.*?), Humidity: (.*?), UV Risk: (.*?), Pollution: (.*?), Sunrise: (.*?), Sunset: (.*?)$";

        // Compile the pattern
        Pattern pattern = Pattern.compile(regexPattern);

        // Create a Matcher object and match it against the input text
        Matcher matcher = pattern.matcher(text);


        // Check if the pattern matches the input text
        if (matcher.find()) {
            // Extract and store each piece of information in the array
            for (int i = 0; i < 9; i++) {
                weatherDetails[i] = matcher.group(i + 1);
            }
        }

        return weatherDetails;

    };


    //Split  a given text string into date and rain parts
    public static String[] splitDateRain(String text){
        String[] parts = text.split(",");
        String[] daterain = parts[0].split(":");
        return daterain;


    };


    //extra current date
    public static String extractDate(String dateTimeString) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
        try {
            Date date = inputFormat.parse(dateTimeString);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null; // Return null or handle the exception as needed
        } catch (java.text.ParseException e) {
            throw new RuntimeException(e);
        }
    }


    //get city id by  cityname
    public  static String getCityId(String city){
        String cityName = city.toLowerCase();
        switch (cityName){
            case "glasgow":
                return "2648579";
            case "london":
                return  "2643743";
            case "newyork":
                return "5128581";
            case "oman":
                return "287286";
            case "mauritius":
                return "934154";
            case "bangladesh":
                return "1185241";
        }

        return cityName;

    }


    //get cityname by id
    public static String getCityName(String cityId) {
        switch (cityId) {
            case "2648579":
                return "Glasgow";
            case "2643743":
                return "London";
            case "5128581":
                return "New York";
            case "287286":
                return "Oman";
            case "934154":
                return "Mauritius";
            case "1185241":
                return "Bangladesh";
            default:
                return "Unknown";
        }
    }



    //Change icon based on rain description
    public static void processRainDescription(String rainDescription, ImageView imageView) {
        String[] words = rainDescription.split("\\s+");

        if (words.length > 0) {
            String firstWord = words[1].toLowerCase();


            switch (firstWord) {
                case "light":
                    imageView.setImageResource(R.drawable.light_rain);
                    break;
                case "heavy":
                    imageView.setImageResource(R.drawable.rain);
                    break;
                case "sunny":
                    imageView.setImageResource(R.drawable.sun);
                    break;
                case "partly":
                    imageView.setImageResource(R.drawable.cloudy);
                    break;

                default:
                    imageView.setImageResource(R.drawable.rainyday);
                    break;
            }
        } else {
            imageView.setImageResource(R.drawable.cloudy);
        }
    }


    //Call  the Gemini API to generate weather tips based on the current weather conditions.
    public static void callGeminiApi(String weather, TextView tips) {
        // For text-only input
        GenerativeModel gm = new GenerativeModel("gemini-pro", "AIzaSyBjPYCxTeXake-2xFrqTteWw0fH4Tppq-E");
        GenerativeModelFutures model = GenerativeModelFutures.from(gm);

        Content content = new Content.Builder()
                .addText("Based on these weather conditions, give me recommendations on the type of clothes to wear or any tips for the day  on activities.Give me only 50 words.Give me in bullet point format : " + weather)
                .build();

        // Create a simple executor to run the callback on a background thread
        Executor executor = Runnable::run;

        ListenableFuture<GenerateContentResponse> response = model.generateContent(content);
        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                String generatedTips = result.getText();
                tips.setText(generatedTips);
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
                tips.setText("Failed to get activity tips.");
            }
        }, executor);
    }


    //Navigate  to a different activity based on the selected menu item.
    public static boolean goToActivity(@NonNull MenuItem item, Context context) {
        int id = item.getItemId();
        if (id == R.id.home) {
            // Add your home activity logic here
            return true;
        } else if (id == R.id.map) {
            Intent intent = new Intent(context, MapsActivity.class);
            context.startActivity(intent);
            return true;

        } else if (id == R.id.settings) {
            Intent intent = new Intent(context, SettingsActivity.class);

            context.startActivity(intent);
            return true;
        }
        return false; // return false if the item ID doesn't match any case
    }


   //Factory for creating ViewModel instances.
    public static class ViewModelFactory implements ViewModelProvider.Factory {
        private final WeatherRepository weatherRepository;

        public ViewModelFactory(WeatherRepository weatherRepository) {
            this.weatherRepository = weatherRepository;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(WeatherViewModel.class)) {
                return (T) new WeatherViewModel(weatherRepository);
            } else if (modelClass.isAssignableFrom(MapsViewModel.class)) {
                return (T) new MapsViewModel(weatherRepository);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }

    //Sets up a spinner adapter with a list of city names
    public static CustomSpinnerAdapter setupSpinnerAdapter(Context context, Spinner spinner) {
        Resources res = context.getResources();
        String[] data = res.getStringArray(R.array.campuses);
        List<String> dataList = Arrays.asList(data);

        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(context, dataList);
        spinner.setAdapter(adapter);

        return adapter;
    }


}
