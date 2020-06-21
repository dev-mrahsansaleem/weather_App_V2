package com.example.myapplication.Retrofit;

import com.example.myapplication.Modal.WeatherResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherMapAPI {
    @GET("weather")
    Observable<WeatherResponse> getWeather(
            @Query("lat") int lat,
            @Query("lon") int lon,
            @Query("appid") String App_ID,
            @Query("units") String units
    );
}
