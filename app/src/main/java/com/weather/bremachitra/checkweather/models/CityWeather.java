package com.weather.bremachitra.checkweather.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;

public class CityWeather implements Serializable {
    @SerializedName("weather")
    public ArrayList<Weather> weather = new ArrayList<Weather>();
    public int checkedornot = 0;
    @SerializedName("main")
    public WeatherDetails main;
    @SerializedName("sys")
    public Sys sys;
    @SerializedName("name")
    public String name;
    @SerializedName("wind")
    public Wind wind;

    public Wind getWind() { return wind; }
    public void setWind(Wind wind) { this.wind = wind; }
    public Sys getSys() {
        return sys;
    }
    public void setSys(Sys sys) {
        this.sys = sys;
    }
    public WeatherDetails getMain() {
        return main;
    }
    public void setMain(WeatherDetails main) {
        this.main = main;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public ArrayList<Weather> getWeather() {
        return weather;
    }
    public void setWeather(ArrayList<Weather> weather) {
        this.weather = weather;
    }
}
