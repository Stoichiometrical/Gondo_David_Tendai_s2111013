//Author: David Tendai Gondo
//StudentID :s2111013

package com.example.gondo_david_tendai_s2111013.ui.detailed;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.Toast;


import com.example.gondo_david_tendai_s2111013.R;
import com.example.gondo_david_tendai_s2111013.model.ForecastWeather;


import java.util.List;

public class DetailedForecastsActivity extends AppCompatActivity {

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_forecasts);

        viewPager = findViewById(R.id.viewPager);
        // Displaying a toast
        Toast.makeText(this, "Swipe left to see more days", Toast.LENGTH_SHORT).show();

        // Get the list of forecast weather objects
        List<ForecastWeather> forecastWeatherList = (List<ForecastWeather>) getIntent().getSerializableExtra("forecast");

        // Create adapter for ViewPager
        WeatherPagerAdapter adapter = new WeatherPagerAdapter(getSupportFragmentManager(), forecastWeatherList);
        viewPager.setAdapter(adapter);
    }

    // Inner class for the FragmentPagerAdapter
    private class WeatherPagerAdapter extends FragmentPagerAdapter {
        private List<ForecastWeather> forecastWeatherList;

        public WeatherPagerAdapter(FragmentManager fm, List<ForecastWeather> forecastWeatherList) {
            super(fm);
            this.forecastWeatherList = forecastWeatherList;
        }



        @Override
        public Fragment getItem(int position) {
            String location = getIntent().getStringExtra("location"); // Get the location

            Bundle args = new Bundle();
            args.putSerializable("forecast", forecastWeatherList.get(position));
            args.putString("location", location);
            ForecastFragment fragment = new ForecastFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return forecastWeatherList.size();
        }
    }
}
