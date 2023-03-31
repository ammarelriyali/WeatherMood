package com.example.weathermood.model

import androidx.room.Entity

@Entity( primaryKeys = ["lat","lon","isHome"])
data class OneCall(
    var current: Current  ,
    var daily: List<Daily>  ,
    var hourly: List<Hourly> ,
     var lat: Double ,
    var lon: Double ,
    var city: String = "Empty",
    var isHome:Boolean=false
) : java.io.Serializable

