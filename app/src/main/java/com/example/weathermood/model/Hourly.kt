package com.example.weathermood.model

data class Hourly(
    val dt: Long,
    val feels_like: Double,
    val temp: Double,
    val weather: List<Weather>,
):java.io.Serializable
