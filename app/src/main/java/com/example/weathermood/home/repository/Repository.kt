package com.example.weathermood.home.repository

import com.example.mvvm.DB.LocalData
import com.example.weathermood.model.OneCall
import com.example.weathermood.remoltydata.RemotelyDataSource
import retrofit2.Response


class Repository(private val localData: LocalData, private val remotelyData: RemotelyDataSource) :
    IRepository {

    override suspend fun getCurrentLocation(lon: String, lat: String, unit: String, lang: String): Response<OneCall> {
        return remotelyData.getCurrentLocation(lon,lat,unit,lang)
    }

    override suspend fun setWeather(body: OneCall) {

    }


}
