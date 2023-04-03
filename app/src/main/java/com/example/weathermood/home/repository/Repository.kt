package com.example.weathermood.home.repository

import androidx.lifecycle.viewModelScope
import com.example.mvvm.DB.LocalData
import com.example.weathermood.model.OneCall
import com.example.weathermood.model.OneCallHome
import com.example.weathermood.remoltydata.RemotelyDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import retrofit2.Response


class Repository(private val localData: LocalData, private val remotelyData: RemotelyDataSource) :
    IRepository {

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
