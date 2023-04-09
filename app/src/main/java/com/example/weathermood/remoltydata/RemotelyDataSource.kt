package com.example.mvvm.retroit

import com.example.createrecwithkotlin.retroit.ApiInterface
import com.example.weathermood.model.AlertModel
import com.example.weathermood.model.AlertResponse
import com.example.weathermood.model.OneCall
import com.example.weathermood.remoltydata.IRemotelyDataSource
import com.example.weathermood.remoltydata.RetrofitClient
import com.example.weathermood.shareperf.MySharedPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response


class RemotelyDataSource : IRemotelyDataSource {
    val api: ApiInterface

    init {
        api = RetrofitClient.getInstance().create(ApiInterface::class.java)
    }

    override fun getCurrentLocation(
        lon: String, lat: String
    ): Flow<Response<OneCall>> {
        return flow { emit(api.getCurrentWeather(lat, lon, MySharedPreference.getUnits(), MySharedPreference.getLanguage())) }
    }

    override fun getAlerts(
        log: String, lat: String
    ): Flow<Response<AlertResponse>> {
        return flow{ emit(api.getAlerts(lat, log, MySharedPreference.getLanguage()))}
    }



}