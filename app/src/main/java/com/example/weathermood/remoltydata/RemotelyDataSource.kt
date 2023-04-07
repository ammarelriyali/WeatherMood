package com.example.weathermood.remoltydata

import com.example.weathermood.model.OneCall
import kotlinx.coroutines.flow.Flow
import retrofit2.Response


interface RemotelyDataSource {
     fun getCurrentLocation(log: String, lat: String
    ): Flow<Response<OneCall>>

    fun getAlerts(log: String, lat: String
    ): Flow<Response<OneCall>>
}