package com.example.mvvm.DB

import android.content.Context
import com.example.weathermood.localdata.DAO
import com.example.weathermood.localdata.WeatherDB
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


}

