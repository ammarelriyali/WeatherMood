package com.example.weathermood.home.repository

import com.example.weathermood.model.OneCall
import retrofit2.Response

interface IRepository {
    suspend fun getCurrentLocation(log: String, lat: String, unit: String , lang: String ):Response<OneCall>
    suspend fun setWeather(body: OneCall)
}