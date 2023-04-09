package com.example.weathermood.remoltydata

import com.example.weathermood.model.AlertModel
import com.example.weathermood.model.AlertResponse
import com.example.weathermood.model.OneCall
import kotlinx.coroutines.flow.Flow
import retrofit2.Response


interface IRemotelyDataSource {
     fun getCurrentLocation(log: String, lat: String
    ): Flow<Response<OneCall>>

    fun getAlerts(log: String, lat: String
    ): Flow<Response<AlertResponse>>

}