package com.example.weather;

import com.example.weather.Instances.WeatherApiService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.weatherapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    WeatherApiService weatherApiServiceImp = retrofit.create(WeatherApiService.class);
}
