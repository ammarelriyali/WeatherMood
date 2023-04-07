package com.example.weathermood.home.mvvvm.repository

import com.example.weathermood.model.OneCall
import com.example.weathermood.model.OneCallHome
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface IRepositoryHome {
     fun getCurrentLocation(log: String, lat: String ):Flow<Response<OneCall>>
    fun getWeather(): Flow<List<OneCallHome>>
     suspend fun setWeather(body: OneCallHome)
}