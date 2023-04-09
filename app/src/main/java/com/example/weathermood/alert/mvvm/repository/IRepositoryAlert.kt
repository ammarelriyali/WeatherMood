package com.example.weathermood.alert.mvvm.repository

import com.example.weathermood.model.FavouriteLocation
import com.example.weathermood.model.AlertModel
import com.example.weathermood.model.AlertResponse
import com.example.weathermood.model.OneCall
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface IRepositoryAlert {
    fun getFavItems(): Flow<List<FavouriteLocation>>
    fun getCurrentLocation(
        longitude: String,
        latitude: String,
    ): Flow<Response<OneCall>>

    suspend fun insertFav(data: FavouriteLocation)

    suspend fun deleteFavItem(data: FavouriteLocation)
    fun getAlertItems(): Flow<List<AlertModel>>
    suspend fun deleteAlertItem(it: AlertModel)
    fun getAlert(lat: String, log: String): Flow<Response<AlertResponse>>
    suspend fun getAlertDB(id: Int): AlertModel
    suspend fun deleteAlert(id: AlertModel)
}
