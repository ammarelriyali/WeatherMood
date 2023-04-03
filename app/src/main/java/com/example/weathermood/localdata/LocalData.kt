package com.example.mvvm.DB

import com.example.weathermood.model.FavouriteLocation
import com.example.weathermood.model.OneCall
import com.example.weathermood.model.OneCallHome
import kotlinx.coroutines.flow.Flow


interface LocalData{
     suspend fun insertCall(oneCall: OneCallHome)
    fun getCall(): Flow<List<OneCallHome>>
    suspend fun insertFav(favouriteLocation: FavouriteLocation)

}