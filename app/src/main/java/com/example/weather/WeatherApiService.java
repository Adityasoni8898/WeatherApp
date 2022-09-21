package com.example.weather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApiService {
    @GET("v1/current.json?key="+BuildConfig.weather_api)
    Call<WeatherData> getData(
            @Query("q") String place
    );
}
