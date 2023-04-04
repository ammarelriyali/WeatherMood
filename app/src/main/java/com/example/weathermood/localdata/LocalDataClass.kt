package com.example.mvvm.DB

import android.content.Context
import android.util.Log
import com.example.weathermood.localdata.DAO
import com.example.weathermood.localdata.WeatherDB
import com.example.weathermood.model.FavouriteLocation
import com.example.weathermood.model.OneCall
import com.example.weathermood.model.OneCallHome
import kotlinx.coroutines.flow.Flow

class LocalDataClass(context:Context) : LocalData {
    var dao: DAO = WeatherDB.getInstance(context).getDao()

    override suspend fun insertCall(oneCall:OneCallHome){
        return dao.insert(oneCall)
    }

    override  fun getCall():Flow<List<OneCallHome>> {
       return dao.getCall()
    }

    override suspend fun insertFav(favouriteLocation: FavouriteLocation) {
        Log.i("TAG", "insertFav: "+favouriteLocation.city)
      dao.insertFav(favouriteLocation)
    }

    override fun getFavItems(): Flow<List<FavouriteLocation>> {
        return dao.getFavItems()
    }

    override suspend fun deleteFavItem(data: FavouriteLocation) {
       dao.deleteFav(data)
    }


}

