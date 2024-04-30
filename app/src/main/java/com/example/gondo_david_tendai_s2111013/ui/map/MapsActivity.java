//Author: David Tendai Gondo
//StudentID :s2111013


package com.example.gondo_david_tendai_s2111013.ui.map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.example.gondo_david_tendai_s2111013.R;
import com.example.gondo_david_tendai_s2111013.repository.WeatherRepository;
import com.example.gondo_david_tendai_s2111013.utils.HelperMethods;
import com.example.gondo_david_tendai_s2111013.model.ForecastWeather;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapsViewModel viewModel;
    private GoogleMap maps;
    private FrameLayout windowOverlayContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Initialize the window overlay container
        windowOverlayContainer = findViewById(R.id.window_overlay_container);

//         Inflate and add the window.xml layout to the container
        LayoutInflater inflater = LayoutInflater.from(this);
        View windowOverlayView = inflater.inflate(R.layout.map_overlay, windowOverlayContainer, false);
        windowOverlayContainer.addView(windowOverlayView);

        // Create an instance of WeatherRepository
        WeatherRepository weatherRepository = new WeatherRepository(this);

        // Create an instance of the ViewModelFactory
        HelperMethods.ViewModelFactory factory = new HelperMethods.ViewModelFactory(weatherRepository);

        // Obtain the MapsViewModel instance using the ViewModelProvider
        viewModel = new ViewModelProvider(this, factory).get(MapsViewModel.class);

        // Observe the forecastWeather LiveData
        viewModel.getForecastWeather().observe(this, new Observer<List<ForecastWeather>>() {
            @Override
            public void onChanged( List<ForecastWeather> forecasts) {
                // Update the map with the new weather forecasts
                updateMapWithForecasts(forecasts);
            }
        });

        // Fetch weather data
        getData("2648579", "2643743", "5128581", "287286", "934154", "1185241");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        maps = googleMap;

        // Initial zoom over Europe/Africa region
        LatLng center = new LatLng(31.8176432, 13.9109963);
        maps.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 3));
    }

    private void getData(String... ids) {
        viewModel.fetchWeatherData(Arrays.asList(ids));
    }


    private void setCustomInfoWindowAdapter() {
        maps.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null; // Use default info window background
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Inflate custom layout for info window
                View view = getLayoutInflater().inflate(R.layout.info_window, null);

                // Find views in the custom layout
                TextView titleTextView = view.findViewById(R.id.title);
                ImageView tempImageView = view.findViewById(R.id.temp);
                TextView tempTextView = view.findViewById(R.id.temp_text);
                ImageView rainImageView = view.findViewById(R.id.rain);
                TextView rainTextView = view.findViewById(R.id.rain_value);
                ImageView uvImageView = view.findViewById(R.id.uv);
                TextView uvTextView = view.findViewById(R.id.uv_value);
                ImageView pollutionImageView = view.findViewById(R.id.pollution);
                TextView pollutionTextView = view.findViewById(R.id.pollution_value);

                // Get weather details from the marker's snippet
                String snippet = marker.getSnippet();
                String[] details = snippet.split(";");

                // Set title (city name)
                titleTextView.setText(marker.getTitle());

                // Set weather details (temperature, rain, uv, pollution)
                tempTextView.setText("Temperature: " + details[0]);
                rainTextView.setText("Rain: " + details[1]);
                uvTextView.setText("UV Index: " + details[2]);
                pollutionTextView.setText("Pollution: " + details[3]);

                marker.setInfoWindowAnchor(0.5f, 1.0f);



                return view;
            }
        });
    }

    private void updateMapWithForecasts(List<ForecastWeather> forecasts) {
        if (maps == null || forecasts == null) {
            return;
        }

        String[] cityNames = {"Glasgow", "London", "NewYork", "Oman", "Mauritius", "Bangladesh"};

        for (int i = 0; i < forecasts.size(); i++) {
            ForecastWeather forecast = forecasts.get(i);
            // Get the coordinates from the forecast
            double latitude = Double.parseDouble(forecast.getLatitude());
            double longitude = Double.parseDouble(forecast.getLongitude());
            LatLng coordinates = new LatLng(latitude, longitude);

            // Set the temperature and weather details as the marker's title and snippet
            String temperature = forecast.getMin_temp() != null ? forecast.getMin_temp()+ "°C" : "Unknown";
            String rain = forecast.getRain() != null ? forecast.getRain() : "Unknown";
            String uv = forecast.getUv_risk() != null ? forecast.getUv_risk() : "Unknown";
            String pollution = forecast.getPollution() != null ? forecast.getPollution() : "Unknown";
            String snippet = temperature + ";" + rain + ";" + uv + ";" + pollution;

            // Create a BitmapDescriptor from a drawable resource
            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.school);

            setCustomInfoWindowAdapter();

            // Add marker to the map with snippet containing weather details
            maps.addMarker(new MarkerOptions()
                    .position(coordinates)
                    .title("GCU " + cityNames[i])
                    .snippet(snippet)
                    .icon(icon));
        }
    }


}



