package com.example.weathermood.remoltydata

import com.example.weathermood.model.OneCall
import retrofit2.Response


interface RemotelyDataSource {
    suspend fun getCurrentLocation(log: String, lat: String, unit: String , lang: String
    ):Response<OneCall>

    suspend fun getAlerts(log: String, lat: String, unit: String , lang: String
    ): Response<OneCall>
}