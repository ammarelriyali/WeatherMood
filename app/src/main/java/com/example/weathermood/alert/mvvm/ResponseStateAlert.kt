package com.example.weathermood.alert.mvvm

import com.example.weathermood.model.AlertModel

sealed class ResponseStateAlert {
    class Success (val data: List<AlertModel>) : ResponseStateAlert()
    class Failure (val msg: Throwable) : ResponseStateAlert()
    object Loading :ResponseStateAlert()
}
