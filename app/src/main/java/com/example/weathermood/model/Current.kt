package com.example.weathermood.model

data class Current(

    val clouds: Double,
    val dt: Long,
    val feels_like: Double,
    val humidity: Double,
    val pressure: Double,
    val temp: Double,
    val weather: List<Weather>,
    val wind_speed: Double
):java.io.Serializable