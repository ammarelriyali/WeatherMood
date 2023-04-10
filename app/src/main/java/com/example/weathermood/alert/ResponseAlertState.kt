package com.example.weathermood.alert

import com.example.weathermood.model.FavouriteLocation
import com.example.weathermood.model.OneCall

sealed class ResponseAlertState {
    class Success (val data: List<FavouriteLocation>) : ResponseAlertState()
    class SuccessApi (val data: OneCall) : ResponseAlertState()
    class FailureResponse(val data: Int,val msg : String) : ResponseAlertState()
    class Failure (val msg: Throwable) : ResponseAlertState()
    object Loading : ResponseAlertState()
}

