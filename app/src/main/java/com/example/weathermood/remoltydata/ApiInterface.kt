package com.example.createrecwithkotlin.retroit

import com.example.weathermood.model.OneCall
import retrofit2.Response
import retrofit2.http.GET

import retrofit2.http.Query

interface ApiInterface {
    @GET("onecall")
    suspend fun getCurrentWeather(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("units") unit: String = "default",
        @Query("lang") lang: String = "en",
        @Query("appid") api: String = "6cd7f954ec06ef046ce2fefb293967ed",
        @Query("exclude") exclude: String = "minutely,alerts"
    ): Response<OneCall>

    @GET("onecall")
    suspend fun getAlerts(
        @Query("lat") lat: String,
        @Query("log") log: String,
        @Query("lang") lang: String = "en",
        @Query("appid") api: String = "6cd7f954ec06ef046ce2fefb293967ed",
        @Query("exclude") exclude: String = "minutely,hourly,daily,current"
    ): Response<OneCall>


}