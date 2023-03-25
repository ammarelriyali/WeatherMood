package com.example.weathermood.model

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

}