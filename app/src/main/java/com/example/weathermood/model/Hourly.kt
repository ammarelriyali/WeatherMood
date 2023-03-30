package com.example.weathermood.model

data class Hourly(
<<<<<<< Updated upstream
    val clouds: Int,
    val dew_point: Double,
    val dt: Int,
    val feels_like: Double,
    val humidity: Int,
    val pop: Int,
    val pressure: Int,
    val temp: Double,
    val uvi: Double,
    val visibility: Int,
    val weather: List<Weather>,
    val wind_deg: Int,
    val wind_gust: Double,
    val wind_speed: Double
)
=======
    val dt: Long,
    val feels_like: Double,
    val temp: Double,
    val weather: List<Weather>,
):java.io.Serializable
>>>>>>> Stashed changes
