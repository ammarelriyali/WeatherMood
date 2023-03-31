package com.example.weathermood.home

import com.example.weathermood.model.OneCall
import com.example.weathermood.model.OneCallHome

sealed class ResponseState {
    class Success (val data: OneCallHome) : ResponseState ()
    class SuccessApi (val data: OneCall) : ResponseState ()
    class FailureResponse(val data: Int,val msg : String) : ResponseState ()
    class Failure (val msg: Throwable) : ResponseState ()
    object Loading : ResponseState()
}
