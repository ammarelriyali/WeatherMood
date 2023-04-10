package com.example.weathermood.alert.mvvm.repository

import com.example.mvvm.DB.LocalData
import com.example.weathermood.model.FavouriteLocation
import com.example.weathermood.model.AlertModel
import com.example.weathermood.model.AlertResponse
import com.example.weathermood.model.OneCall
import com.example.weathermood.remoltydata.IRemotelyDataSource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class RepositoryAlert(private val local: LocalData, private val remotely: IRemotelyDataSource) :
    IRepositoryAlert {
    override fun getFavItems(): Flow<List<FavouriteLocation>> {
        return local.getFavItems()
    }

    override fun getCurrentLocation(
        longitude: String,
        latitude: String,

    ): Flow<Response<OneCall>> {
        return remotely.getCurrentLocation(longitude, latitude)
    }

    override suspend fun insertFav(data: FavouriteLocation) {
        local.insertFav(data)
    }

    override suspend fun deleteFavItem(data: FavouriteLocation) {
        local.deleteFavItem(data)
    }

    override fun getAlertItems(): Flow<List<AlertModel>> {
        return local.getAlertItems()
    }

   override suspend   fun deleteAlertItem(it: AlertModel) {
        local.deleteAlertItme(it)
    }

     override fun getAlert(lat:String, log :String): Flow<Response<AlertResponse>> {
        return remotely.getAlerts(log,lat)
    }

    override suspend fun getAlertDB(id: Int): AlertModel {
       return local.getAlert(id)
    }

    override suspend fun deleteAlert(id: AlertModel) {
       local.deleteAlertItme(id)
    }

}
