package com.example.weathermood.home.mvvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weathermood.home.ResponseState
import com.example.weathermood.home.repository.IRepository
import com.example.weathermood.model.OneCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class HomeViewModel(val repository: IRepository) : ViewModel() {
    private var _oneCall: MutableStateFlow<ResponseState> = MutableStateFlow(ResponseState.Loading)

    val response: MutableStateFlow<ResponseState> = _oneCall

    private var _oneCallLocal: MutableStateFlow<OneCall> = MutableLiveData()
    val responseLocal: MutableStateFlow<OneCall> = _oneCallLocal

    fun getCurrentWeather(lon: String, lat: String, unit: String = "default", lang: String = "en") {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getCurrentLocation(lon, lat, unit, lang)
            _oneCall.postValue(response)
            if (response.isSuccessful)
                repository.setWeather(response.body()!!)

        }
    }
    fun getWeather(){
        viewModelScope.launch(){
       repository.getWeather().catch { ResponseState.Failure(it) }
           .collect(){
               if (it.get(0)!=null)
                   _oneCall.value=ResponseState.Success(it.get(0))
               else
                   _oneCall.value=ResponseState.Failure(java.lang.NullPointerException("null"))
           }

        }
    }
    fun insertCall(oneCall:OneCall){

    }
}