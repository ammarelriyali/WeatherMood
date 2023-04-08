package com.example.mvvm.DB

import com.example.weathermood.model.FavouriteLocation
import com.example.weathermood.model.MyAlert
import com.example.weathermood.model.OneCallHome
import kotlinx.coroutines.flow.Flow


interface LocalData{
     suspend fun insertCall(oneCall: OneCallHome)
    fun getCall(): Flow<List<OneCallHome>>
    suspend fun insertFav(favouriteLocation: FavouriteLocation)
    fun getFavItems():Flow<List<FavouriteLocation>>
    suspend fun deleteFavItem(data: FavouriteLocation)
    suspend fun setAlert(myAlert: MyAlert)
    fun getAlertItems(): Flow<List<MyAlert>>
    suspend fun deleteAlertItme(it: MyAlert)

}