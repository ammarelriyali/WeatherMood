package com.example.mvvm.retroit

import android.util.Log
import com.example.createrecwithkotlin.retroit.ApiInterface
import com.example.weathermood.model.OneCall
import com.example.weathermood.remoltydata.RemotelyDataSource
import com.example.weathermood.remoltydata.RetrofitClient
import retrofit2.Response


class Serves : RemotelyDataSource {
    val api:ApiInterface
   init {
       api = RetrofitClient.getInstance().create(ApiInterface::class.java)
   }

    override suspend fun getCurrentLocation(lon:String, lat:String, unit:String, lang:String
    ):Response<OneCall>{
        return api.getCurrentWeather(lat,lon,unit,lang)
    }
    override suspend fun getAlerts(log:String, lat:String, unit:String, lang:String
    ): Response<OneCall> {
        return api.getAlerts(lat,log,unit,lang)
    }

}