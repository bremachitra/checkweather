package com.weather.bremachitra.checkweather.utils;

import com.weather.bremachitra.checkweather.R;

public class Weathericon {
    public static int getIcon(String weatherDescription) {
        int weatherIcon;
        switch (weatherDescription) {
            case "Thunderstorm":
                weatherIcon = R.mipmap.ic_thunderstorm;
                break;
            case "Drizzle":
                weatherIcon = R.mipmap.ic_rain;
                break;
            case "Rain":
                weatherIcon = R.mipmap.ic_rain;
                break;
            case "Snow":
                weatherIcon = R.mipmap.ic_snow;
                break;
            case "Clear":
                weatherIcon = R.mipmap.ic_clear;
                break;
            case "Clouds":
                weatherIcon = R.mipmap.ic_cloud;
                break;
            case "Fog":
                weatherIcon = R.mipmap.ic_fog;
                break;
            default:
                weatherIcon = R.mipmap.ic_clear;
        }
        return weatherIcon;
    }
}