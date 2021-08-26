package com.weather.bremachitra.checkweather.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Sys implements Serializable {
    @SerializedName("country")
    public String country;
    @SerializedName("sunrise")
    public int sunrise;
    @SerializedName("sunset")
    public int sunset;
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public int getSunrise() {
        return sunrise;
    }
    public void setSunrise(int sunrise) {
        this.sunrise = sunrise;
    }
    public int getSunset() {
        return sunset;
    }
    public void setSunset(int sunset) {
        this.sunset = sunset;
    }
}
