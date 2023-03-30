package com.example.weathermood.model

<<<<<<< Updated upstream
data class OneCall(
    val alerts: List<Alert>? = null,
    val current: Current? = null,
    val daily: List<Daily>? =null,
    val hourly: List<Hourly>? =null,
    val lat: Double=0.0,
    val lon: Double=0.0,
    val minutely: List<Minutely>? =null,
    val timezone: String="",
    val timezone_offset: Int=0
):java.io.Serializable{
=======
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

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
>>>>>>> Stashed changes

