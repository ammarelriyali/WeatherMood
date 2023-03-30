package com.example.mvvm.DB

import com.example.weathermood.model.OneCall
import kotlinx.coroutines.flow.Flow


interface LocalData{
    suspend fun insertCall(oneCall: OneCall):Long
    fun getCall(): Flow<OneCall>

}