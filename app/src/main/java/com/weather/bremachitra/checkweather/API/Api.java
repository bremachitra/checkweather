package com.weather.bremachitra.checkweather.API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {
     public static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";
     public static final String KEY = "4f227e20218c1bc33e0c1646c4db3716";
     private static Retrofit retrofit = null;
     public static Retrofit getApi(){
         if(retrofit == null)
         {
            retrofit =new Retrofit.Builder()
                       .baseUrl(BASE_URL)
                       .addConverterFactory(GsonConverterFactory.create())
                       .build();
        }
        return retrofit;
    }
}
