package com.example.weathermood.home.mvvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weathermood.home.ResponseState
import com.example.weathermood.home.repository.IRepository
import com.example.weathermood.model.OneCallHome
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.io.IOException

class HomeViewModel(val repository: IRepository) : ViewModel() {
    private val TAG ="TAGG"
    private var _oneCall: MutableStateFlow<ResponseState> = MutableStateFlow(ResponseState.Loading)
    val response: MutableStateFlow<ResponseState> = _oneCall
    fun getCurrentWeather(lon: String, lat: String, unit: String = "default", lang: String = "en") {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getCurrentLocation(lon, lat, unit, lang)
                .catch { _oneCall.value=ResponseState.Failure(it) }
                .collect() {
                    if (it.isSuccessful){
                        _oneCall.value = ResponseState.SuccessApi(it.body()!!)
                    }
                    else
                        _oneCall.value = ResponseState.FailureResponse(it.code(), it.message())

                }
        }
    }
        fun getWeather() {
            viewModelScope.launch(Dispatchers.IO) {
                repository.getWeather().catch { _oneCall.value=ResponseState.Failure(it) }
                    .collect() {
                        delay(1000)
                        if (it.isNotEmpty())
                            _oneCall.value = ResponseState.Success(it.get(0))
                        else
                            _oneCall.value =
                                ResponseState.Failure(java.lang.NullPointerException("null"))
                    }

            }
        }

        fun insertCall(oneCall: OneCallHome) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    repository.setWeather(oneCall)
                }catch (e:java.lang.Exception){
                    _oneCall.value=ResponseState.Failure(e)
                }
            }

        }
    }