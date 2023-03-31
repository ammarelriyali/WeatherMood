package com.example.weathermood.home.repository

import com.example.weathermood.model.OneCall
import com.example.weathermood.model.OneCallHome
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface IRepository {
     fun getCurrentLocation(log: String, lat: String, unit: String , lang: String ):Flow<Response<OneCall>>
    fun getWeather(): Flow<List<OneCallHome>>
     suspend fun setWeather(body: OneCallHome)
}