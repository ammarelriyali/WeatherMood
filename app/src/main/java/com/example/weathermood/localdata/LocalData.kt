package com.example.mvvm.DB

import com.example.weathermood.model.OneCall
import kotlinx.coroutines.flow.Flow


interface LocalData{
     fun insertCall(oneCall: OneCall):Flow<Long>
    fun getCall(): Flow<List<OneCall>>

}