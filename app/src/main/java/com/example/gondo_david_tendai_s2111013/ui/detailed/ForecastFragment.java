//Author: David Tendai Gondo
//StudentID :s2111013

package com.example.gondo_david_tendai_s2111013.ui.detailed;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


import com.example.gondo_david_tendai_s2111013.R;
import com.example.gondo_david_tendai_s2111013.model.ForecastWeather;


public class ForecastFragment extends Fragment {

    private static final String ARG_FORECAST_WEATHER = "forecast";

    private ForecastWeather forecastWeather;
    private TextView countryTextView;
    private TextView tempTextView,date,pollution,rains,country;
    private LinearLayout mainLinearLayout;
    private TextView minTempTextView;
    private TextView windDirTextView;
    private TextView windSpeedTextView;
    private TextView pressureTextView;
    private TextView sunsetTextView;
    private TextView sunriseTextView;
    String location;

    public static ForecastFragment newInstance(ForecastWeather forecastWeather) {
        ForecastFragment  fragment = new ForecastFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_FORECAST_WEATHER, forecastWeather);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            forecastWeather = (ForecastWeather) getArguments().getSerializable(ARG_FORECAST_WEATHER);
            location = getArguments().getString("location"); // Get the location
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.forecast_fragment, container, false);
        date = rootView.findViewById(R.id.date);
        pollution = rootView.findViewById(R.id.pollution);
        rains = rootView.findViewById(R.id.rain);
        countryTextView = rootView.findViewById(R.id.country);
        tempTextView = rootView.findViewById(R.id.temp);
        mainLinearLayout = rootView.findViewById(R.id.main_linear);
        minTempTextView = rootView.findViewById(R.id.min_temp);
        windDirTextView = rootView.findViewById(R.id.wind_dir);
        windSpeedTextView = rootView.findViewById(R.id.wind_speed);
        pressureTextView = rootView.findViewById(R.id.pressure);
        sunsetTextView = rootView.findViewById(R.id.sunset);
        sunriseTextView = rootView.findViewById(R.id.sunrise);

        displayWeatherData();

        return rootView;
    }

    private void displayWeatherData() {
        if (forecastWeather != null) {
            countryTextView.setText(location);
            date.setText(forecastWeather.getDate()+ "'s Weather");
            minTempTextView.setText(forecastWeather.getMin_temp()+ "°C");

            String temp;
            if(forecastWeather.getMax_temp() ==null){
                temp =forecastWeather.getMin_temp();
            }else {
                temp = forecastWeather.getMax_temp();
            }


            tempTextView.setText(temp+ "°");
            rains.setText(forecastWeather.getRain());
            windDirTextView.setText(forecastWeather.getWind_dir());
            windSpeedTextView.setText(String.valueOf(forecastWeather.getWind_speed()));
            pressureTextView.setText(String.valueOf(forecastWeather.getPressure()));
            sunsetTextView.setText(forecastWeather.getSunset());
            sunriseTextView.setText(forecastWeather.getSunrise());
            pollution.setText("Air Quality :" + forecastWeather.getPollution());
        }
    }
}

