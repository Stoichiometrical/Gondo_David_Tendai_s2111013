//Author: David Tendai Gondo
//StudentID :s2111013

package com.example.gondo_david_tendai_s2111013.repository;

import android.content.Context;
import android.util.Log;

import com.example.gondo_david_tendai_s2111013.utils.HelperMethods;
import com.example.gondo_david_tendai_s2111013.model.ForecastWeather;
import com.example.gondo_david_tendai_s2111013.model.LatestWeather;
import com.example.gondo_david_tendai_s2111013.utils.PreferencesManager;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class WeatherRepository {
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public WeatherRepository(Context context) {
        this.context = context;
        preferencesManager = new PreferencesManager(context); // Initialize PreferenceManager
    }
    private Context context;
    private static PreferencesManager preferencesManager;

    public Future<Object> getLatestWeather(String id) {
        return executorService.submit(new GetWeatherTask(id, "latest"));
    }

    public Future<Object> getForecastWeather(String id) {
        return executorService.submit(new GetWeatherTask(id, "forecast"));
    }

    public void shutdown() {
        executorService.shutdown();
    }

    // Inner Task Class - Handles both latest and forecast
    private static class GetWeatherTask implements Callable<Object> {
        private final String locationId;
        private final String dataType;

        public GetWeatherTask(String locationId, String dataType) {
            this.locationId = locationId;
            this.dataType = dataType;
        }

        @Override
        public Object call() throws WeatherDataException {
            InputStream inputStream = null;
            try {
                String rssUrl = (dataType.equals("latest"))
                        ? String.format("https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/%s", locationId)
                        : String.format("https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/%s", locationId);

                URL url = new URL(rssUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Log.d("fgh", String.valueOf(connection.getResponseCode()));
                    throw new IOException("HTTP Error: " + connection.getResponseCode());

                }

                inputStream = connection.getInputStream();


                if (dataType.equals("latest")) {
                    return parseLatestWeather(inputStream);
                } else {
                    return parseForecastWeather(inputStream);
                }


            }catch (IOException e) {

                // Attempt to load from cache if network fails
                try {
                    if (dataType.equals("latest")) {

                        return preferencesManager.getCachedLatestWeather();
                    } else {
                        return preferencesManager.getCachedForecast();
                    }
                } catch (Exception cacheException) {
                    throw new WeatherDataException("Network & Cache Error", e); // Or rethrow a custom exception
                }
            } catch (XmlPullParserException e) {
                throw new WeatherDataException("Parsing Error", e);
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        // Handle error closing stream
                    }
                }
            }
        }

        private LatestWeather parseLatestWeather(InputStream inputStream) throws XmlPullParserException, IOException {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(inputStream, null);

            LatestWeather weather = new LatestWeather();
            boolean foundItem = false;
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    String tagName = parser.getName();
                    if ("item".equals(tagName)) {
                        foundItem = true;
                    } else if (foundItem && "pubDate".equals(tagName)) {
                        weather.setDate(parser.nextText());
                    } else if (foundItem && "description".equals(tagName)) {
                        String description = parser.nextText();

                        String[] desc = HelperMethods.getWeatherDetails(description);
                        if (desc != null && desc.length >= 6) {
                            String[] temp = desc[0].split("\\s+");
                            String[] xtemp = temp[0].split("°");
                            weather.setTemp(xtemp[0]);
                            weather.setWind_dir(desc[1]);
                            weather.setWind_speed(desc[2]);
                            weather.setHumidity(desc[3]);

                            String[] pressureDet = desc[4].split(",");
                            weather.setPressure(pressureDet[0]);
                            weather.setVisibility(desc[5]);
                        }
                    } else if (foundItem && "georss:point".equals(tagName)) {
                        weather.setLocation(parser.nextText());
                    }
                } else if (eventType == XmlPullParser.END_TAG && "item".equals(parser.getName())) {
                    break;
                }
                eventType = parser.next();
            }

            inputStream.close();

            preferencesManager.updateLatestCache(weather);


            return weather;
        }

        private List<ForecastWeather> parseForecastWeather(InputStream inputStream) throws XmlPullParserException, IOException {
            // Initialize XML parser
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(inputStream, null);

            // Variables to store weather information
            List<ForecastWeather> forecasts = new ArrayList<>();
            ForecastWeather forecast = null;

            // Parse XML
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        String tagName = parser.getName();
                        if ("item".equals(tagName)) {
                            forecast = new ForecastWeather();
                        } else if (forecast != null) {
                            switch (tagName) {
                                case "title":
                                    String title = parser.nextText();
                                    // Extract date and rain information from the title
                                    String[] titleParts = title.split(", ");
                                    String[] titles =titleParts[0].split(":");
                                    forecast.setDate(titles[0]);
                                    forecast.setRain(titles[1]);
                                    break;
                                case "description":
                                    // Extract weather details from the description
                                    String description = parser.nextText();
                                    String[] details = description.split(", ");
                                    for (String detail : details) {
                                        String[] keyValue = detail.split(": ");
                                        switch (keyValue[0]) {
                                            case "Minimum Temperature":
                                                String[] det = keyValue[1].split("°");
//
                                                forecast.setMin_temp(det[0]);
                                                break;
                                            case "Maximum Temperature":
                                                String[] dets = keyValue[1].split("°");
                                                forecast.setMax_temp(dets[0]);
                                                break;
                                            case "Wind Direction":
                                                forecast.setWind_dir(keyValue[1]);
                                                break;
                                            case "Wind Speed":
                                                forecast.setWind_speed(keyValue[1]);
                                                break;
                                            case "Visibility":
                                                forecast.setVisibility(keyValue[1]);
                                                break;
                                            case "Pressure":
                                                forecast.setPressure(keyValue[1]);
                                                break;
                                            case "UV Risk":
                                                forecast.setUv_risk(keyValue[1]);
                                                break;
                                            case "Pollution":
                                                forecast.setPollution(keyValue[1]);
                                                break;
                                            case "Sunrise":
                                                forecast.setSunrise(keyValue[1]);
                                                break;
                                            case "Sunset":
                                                forecast.setSunset(keyValue[1]);
                                                break;

                                            case "Humidity":
                                                forecast.setHumidity(keyValue[1]);
                                                break;
                                        }
                                    }
                                    break;
                                case "georss:point":
                                    // Extract location
                                    String coordinates = parser.nextText();
                                    String[] loc = coordinates.split(" ");
                                    if (loc.length == 2) {
                                        forecast.setLatitude(loc[0]);
                                        forecast.setLongitude(loc[1]);
                                        forecast.setLocation(coordinates);
                                    } else {
                                        // Handle unexpected format
                                        Log.e("GSAE", "Unexpected format for coordinates: " + coordinates);
                                    }
                                    break;
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("item".equals(parser.getName()) && forecast != null) {
                            forecasts.add(forecast);
                            forecast = null;
                        }
                        break;
                }
                eventType = parser.next();
            }

            inputStream.close();

            preferencesManager.updateForecastCache(forecasts);
            return forecasts;
        }
    }

    // Custom Exception for Weather Data Errors
    public static class WeatherDataException extends Exception {
        public WeatherDataException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
