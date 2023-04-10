package com.example.weathermood.favourite.mvvm.repository.repoitory

import com.example.mvvm.DB.LocalData
import com.example.weathermood.model.AlertModel
import com.example.weathermood.model.FavouriteLocation
import com.example.weathermood.model.OneCallHome
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeLocalData(private val arr: MutableList<FavouriteLocation>):LocalData{
    override suspend fun insertCall(oneCall: OneCallHome) {
        TODO("Not yet implemented")
    }

    override fun getCall(): Flow<List<OneCallHome>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertFav(favouriteLocation: FavouriteLocation) {
       arr.add(favouriteLocation)
    }

    override fun getFavItems(): Flow<List<FavouriteLocation>> {
    return flow {  emit(arr as List<FavouriteLocation>)}
    }

    override suspend fun deleteFavItem(data: FavouriteLocation) {
        arr.remove(data)
    }

    override suspend fun setAlert(alertModel: AlertModel): Long {
        TODO("Not yet implemented")
    }

    override fun getAlertItems(): Flow<List<AlertModel>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAlertItme(it: AlertModel) {
        TODO("Not yet implemented")
    }

    override suspend fun getAlert(id: Int): AlertModel {
        TODO("Not yet implemented")
    }

}