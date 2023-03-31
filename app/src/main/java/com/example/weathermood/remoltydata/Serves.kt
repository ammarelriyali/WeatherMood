package com.example.mvvm.retroit

import android.util.Log
import com.example.createrecwithkotlin.retroit.ApiInterface
import com.example.weathermood.model.OneCall
import com.example.weathermood.remoltydata.RemotelyDataSource
import com.example.weathermood.remoltydata.RetrofitClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import retrofit2.Response


class Serves : RemotelyDataSource {
    val api: ApiInterface

    init {
        api = RetrofitClient.getInstance().create(ApiInterface::class.java)
    }

    override fun getCurrentLocation(
        lon: String, lat: String, unit: String, lang: String
    ): Flow<Response<OneCall>> {
        return flow { emit(api.getCurrentWeather(lat, lon, unit, lang)) }
    }

    override fun getAlerts(
        log: String, lat: String, unit: String, lang: String
    ): Flow<Response<OneCall>> {
        return flow{ emit(api.getAlerts(lat, log, unit, lang))}
    }

}