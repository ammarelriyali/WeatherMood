package com.example.weathermood.home.mvvvm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weathermood.home.mvvvm.repository.IRepositoryHome
import com.example.weathermood.model.OneCallHome
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(val repository: IRepositoryHome) : ViewModel() {
    private val TAG ="TAGG"
    private var _oneCall: MutableStateFlow<ResponseStateHome> = MutableStateFlow(ResponseStateHome.Loading)
    val response: MutableStateFlow<ResponseStateHome> = _oneCall
    fun getCurrentWeather(lon: String, lat: String) {
        Log.i(TAG, "getCurrentWeather: ")
        viewModelScope.launch(Dispatchers.IO) {
            repository.getCurrentLocation(lon, lat )
                .catch { _oneCall.value= ResponseStateHome.Failure(it) }
                .collect() {
                    if (it.isSuccessful){
                        _oneCall.value = ResponseStateHome.SuccessApi(it.body()!!)
                    }
                    else
                        _oneCall.value = ResponseStateHome.FailureHomeResponse(it.code(), it.message())

                }
        }
    }
        fun getWeather() {
            viewModelScope.launch(Dispatchers.IO) {
                Log.i(TAG, "getWeather: ")
                repository.getWeather().catch { _oneCall.value= ResponseStateHome.Failure(it) }
                    .collect() {
                        if (it.isNotEmpty())
                            _oneCall.value = ResponseStateHome.Success(it.get(0))
                        else
                            _oneCall.value =
                                ResponseStateHome.Failure(java.lang.NullPointerException("null"))
                    }

            }
        }

        fun insertCall(oneCall: OneCallHome) {
            Log.i(TAG, "insertCall: ")
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    repository.setWeather(oneCall)
                }catch (e:java.lang.Exception){
                    _oneCall.value= ResponseStateHome.Failure(e)
                }
            }

        }
    }