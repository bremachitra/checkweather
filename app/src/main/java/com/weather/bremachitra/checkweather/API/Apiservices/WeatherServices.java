package com.weather.bremachitra.checkweather.API.Apiservices;

import com.weather.bremachitra.checkweather.models.CityWeather;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherServices {
    @GET("weather")
    Call<CityWeather> getWeatherCity (@Query("q") String city, @Query("APPID")String key);

}
