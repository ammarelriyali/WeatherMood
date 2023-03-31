package com.example.weathermood.model

import androidx.room.Entity

@Entity( primaryKeys = ["lat","lon"])
data class OneCall(
    var current: Current  ,
    var daily: List<Daily>  ,
    var hourly: List<Hourly> ,
     var lat: Double ,
    var lon: Double ,
) : java.io.Serializable

