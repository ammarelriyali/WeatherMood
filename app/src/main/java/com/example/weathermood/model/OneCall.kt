package com.example.weathermood.model

data class OneCall(
    val alerts: List<Alert>? = null,
    val current: Current? = null,
    val daily: List<Daily>? =null,
    val hourly: List<Hourly>? =null,
    val lat: Double=0.0,
    val lon: Double=0.0,
    val timezone: String="",
    val timezone_offset: Double=0.0,
    var city :String="Empty"
):java.io.Serializable{

}