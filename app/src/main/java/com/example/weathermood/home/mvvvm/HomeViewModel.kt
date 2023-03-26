package com.example.weathermood.home.mvvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weathermood.home.repository.IRepository
import com.example.weathermood.model.OneCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class HomeViewModel(val repository: IRepository) : ViewModel() {
    private var oneCall: MutableLiveData<Response<OneCall>> = MutableLiveData()
    val response: LiveData<Response<OneCall>> = oneCall
    fun getCurrentWeather(lon: String, lat: String, unit: String = "default", lang: String = "en") {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getCurrentLocation(lon, lat, unit, lang)
            oneCall.postValue(response)
            if (response.isSuccessful)
                repository.setWeather(response.body()!!)

        }
    }
}