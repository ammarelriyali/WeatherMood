package com.example.weathermood.favourite.mvvm.repository

import com.example.mvvm.DB.LocalData
import com.example.weathermood.model.FavouriteLocation
import com.example.weathermood.model.OneCall
import com.example.weathermood.remoltydata.RemotelyDataSource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class RepositoryFavorite(val local: LocalData, val remotelyDataSource: RemotelyDataSource) :
    IRepositoryFavorite {
    override fun getFavItems(): Flow<List<FavouriteLocation>> {
        return local.getFavItems()
    }

    override fun getCurrentLocation(
        longitude: String,
        latitude: String,

    ): Flow<Response<OneCall>> {
        return remotelyDataSource.getCurrentLocation(longitude, latitude)
    }

    override suspend fun insertFav(data: FavouriteLocation) {
        local.insertFav(data)
    }

    override suspend fun deleteFavItem(data: FavouriteLocation) {
        local.deleteFavItem(data)
    }

}
