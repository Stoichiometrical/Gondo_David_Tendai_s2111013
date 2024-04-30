//Author: David Tendai Gondo
//StudentID :s2111013


package com.example.gondo_david_tendai_s2111013.model;

import java.io.Serializable;
import java.util.Objects;

public class ForecastWeather implements Serializable {

    private String date;

    private String rain;

    private String max_temp;
    private String min_temp;
    private String wind_dir;

    private String wind_speed;
    private String visibility;
    private String pressure;

    private String uv_risk;
    private String humidity;
    private String pollution;
    private String location;

    private String latitude;



    private String longitude;
    private String sunrise;
    private String sunset;

    public ForecastWeather(){

    }
    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRain() {
        return rain;
    }

    public void setRain(String rain) {
        this.rain = rain;
    }

    public String getMax_temp() {
        return max_temp;
    }

    public void setMax_temp(String max_temp) {
        this.max_temp = max_temp;
    }

    public String getMin_temp() {
        return min_temp;
    }

    public void setMin_temp(String min_temp) {
        this.min_temp = min_temp;
    }

    public String getWind_dir() {
        return wind_dir;
    }

    public void setWind_dir(String wind_dir) {
        this.wind_dir = wind_dir;
    }

    public String getWind_speed() {
        return wind_speed;
    }

    public void setWind_speed(String wind_speed) {
        this.wind_speed = wind_speed;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getUv_risk() {
        return uv_risk;
    }

    public void setUv_risk(String uv_risk) {
        this.uv_risk = uv_risk;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getPollution() {
        return pollution;
    }

    public void setPollution(String pollution) {
        this.pollution = pollution;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ForecastWeather that = (ForecastWeather) o;
        return Objects.equals(date, that.date) && Objects.equals(rain, that.rain) && Objects.equals(max_temp, that.max_temp) && Objects.equals(min_temp, that.min_temp) && Objects.equals(wind_dir, that.wind_dir) && Objects.equals(wind_speed, that.wind_speed) && Objects.equals(visibility, that.visibility) && Objects.equals(pressure, that.pressure) && Objects.equals(uv_risk, that.uv_risk) && Objects.equals(humidity, that.humidity) && Objects.equals(pollution, that.pollution) && Objects.equals(location, that.location) && Objects.equals(sunrise, that.sunrise) && Objects.equals(sunset, that.sunset);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, rain, max_temp, min_temp, wind_dir, wind_speed, visibility, pressure, uv_risk, humidity, pollution, location, sunrise, sunset);
    }

    @Override
    public String toString() {
        return "ForecastWeather{" +
                "date='" + date + '\'' +
                ", rain='" + rain + '\'' +
                ", max_temp='" + max_temp + '\'' +
                ", min_temp='" + min_temp + '\'' +
                ", wind_dir='" + wind_dir + '\'' +
                ", wind_speed='" + wind_speed + '\'' +
                ", visibility='" + visibility + '\'' +
                ", pressure='" + pressure + '\'' +
                ", uv_risk='" + uv_risk + '\'' +
                ", humidity='" + humidity + '\'' +
                ", pollution='" + pollution + '\'' +
                ", location='" + location + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", sunrise='" + sunrise + '\'' +
                ", sunset='" + sunset + '\'' +
                '}';
    }
}


