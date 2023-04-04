package com.example.weathermood.favourite

import com.example.weathermood.model.FavouriteLocation
import com.example.weathermood.model.OneCall
import com.example.weathermood.model.OneCallHome

sealed class ResponseStateFav {
    class Success (val data: List<FavouriteLocation>) : ResponseStateFav ()
    class SuccessApi (val data: OneCall) : ResponseStateFav ()
    class FailureResponse(val data: Int,val msg : String) : ResponseStateFav ()
    class Failure (val msg: Throwable) : ResponseStateFav ()
    object Loading : ResponseStateFav()
}
