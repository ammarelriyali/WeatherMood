package com.example.weathermood.home.mvvvm.repository

import com.example.mvvm.DB.LocalData
import com.example.weathermood.model.OneCall
import com.example.weathermood.model.OneCallHome
import com.example.weathermood.remoltydata.RemotelyDataSource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response


class RepositoryHome(private val localData: LocalData, private val remotelyData: RemotelyDataSource) :
    IRepositoryHome {

    override  fun getCurrentLocation(lon: String, lat: String, unit: String, lang: String): Flow<Response<OneCall>> {
        return remotelyData.getCurrentLocation(lon,lat,unit,lang)
    }

    override suspend fun setWeather(body: OneCallHome){
        localData.insertCall( body)
    }
    override fun getWeather(): Flow<List<OneCallHome>> {
        return localData.getCall()
    }


}
