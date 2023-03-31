package com.example.weathermood.model

data class Daily(
    val dt: Long,

    val temp: Temp,
    val weather: List<Weather>,

):java.io.Serializable
