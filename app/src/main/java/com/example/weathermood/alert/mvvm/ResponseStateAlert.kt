package com.example.weathermood.alert.mvvm

import com.example.weathermood.model.MyAlert

sealed class ResponseStateAlert {
    class Success (val data: List<MyAlert>) : ResponseStateAlert()
    class Failure (val msg: Throwable) : ResponseStateAlert()
    object Loading :ResponseStateAlert()
}
