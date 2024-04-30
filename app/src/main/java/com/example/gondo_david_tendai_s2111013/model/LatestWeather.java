//Author: David Tendai Gondo
//StudentID :s2111013

package com.example.gondo_david_tendai_s2111013.model;


import java.io.Serializable;
import java.util.Objects;

public  class LatestWeather  implements Serializable {

    private String date;
    private String time;
    private String temp;
    private String wind_dir;
    private String wind_speed;
    private String pressure;
    private String humidity;
    private String location;

    private String visibility;

    public LatestWeather(){

    }

    public LatestWeather(String date, String time, String temp, String wind_dir, String wind_speed,
                         String pressure, String humidity, String location, String visibility) {
        this.date = date;
        this.time = time;
        this.temp = temp;
        this.wind_dir = wind_dir;
        this.wind_speed = wind_speed;
        this.pressure = pressure;
        this.humidity = humidity;
        this.location = location;
        this.visibility = visibility;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
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

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getVisibility() {
        return visibility;
    }



    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }




    @Override
    public int hashCode() {
        return Objects.hash(date, time, temp, wind_dir, wind_speed, pressure, humidity, location, visibility);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LatestWeather that = (LatestWeather) o;
        return Objects.equals(date, that.date) && Objects.equals(time, that.time) && Objects.equals(temp, that.temp) && Objects.equals(wind_dir, that.wind_dir) && Objects.equals(wind_speed, that.wind_speed) && Objects.equals(pressure, that.pressure) && Objects.equals(humidity, that.humidity) && Objects.equals(location, that.location) && Objects.equals(visibility, that.visibility);
    }

    @Override
    public String toString() {
        return "LatestWeather{" +
                "date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", temp=" + temp +
                ", wind_dir='" + wind_dir + '\'' +
                ", wind_speed=" + wind_speed +
                ", pressure='" + pressure + '\'' +
                ", humidity='" + humidity + '\'' +
                ", location='" + location + '\'' +
                ", visibility='" + visibility + '\'' +
                '}';
    }
}
