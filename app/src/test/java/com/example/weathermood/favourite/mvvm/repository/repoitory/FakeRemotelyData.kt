package com.example.weathermood.favourite.mvvm.repository.repoitory

import com.example.weathermood.model.AlertResponse
import com.example.weathermood.model.OneCall
import com.example.weathermood.model.OneCallHome
import com.example.weathermood.remoltydata.IRemotelyDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class FakeRemotelyData(private val oneCall: OneCallHome):IRemotelyDataSource {
    override fun getCurrentLocation(log: String, lat: String): Flow<Response<OneCall>> {
        oneCall.apply {
            this.oneCall.lat=lat.toDouble()
            this.oneCall.lon=log.toDouble()
        }
        val res:Response<OneCall> =Response.success(oneCall.oneCall)
        return flow {  emit(res)}
    }

    override fun getAlerts(log: String, lat: String): Flow<Response<AlertResponse>> {
        TODO("Not yet implemented")
    }
}