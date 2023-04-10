package com.example.mvvm.DB

import com.example.weathermood.model.FavouriteLocation
import com.example.weathermood.model.AlertModel
import com.example.weathermood.model.OneCallHome
import kotlinx.coroutines.flow.Flow


interface LocalData{
     suspend fun insertCall(oneCall: OneCallHome)
    fun getCall(): Flow<List<OneCallHome>>
    suspend fun insertFav(favouriteLocation: FavouriteLocation)
    fun getFavItems():Flow<List<FavouriteLocation>>
    suspend fun deleteFavItem(data: FavouriteLocation)
    suspend fun setAlert(alertModel: AlertModel): Long
    fun getAlertItems(): Flow<List<AlertModel>>
    suspend fun deleteAlertItme(it: AlertModel)
    suspend fun getAlert(id: Int): AlertModel

}