package com.example.weathermood.model

data class Current(
<<<<<<< Updated upstream
    val clouds: Int,
    val dew_point: Double,
    val dt: Long,
    val feels_like: Double,
    val humidity: Int,
    val pressure: Int,
    val rain: Rain,
    val sunrise: Int,
    val sunset: Int,
    val temp: Double,
    val uvi: Double,
    val visibility: Int,
    val weather: List<Weather>,
    val wind_deg: Int,
=======
    val clouds: Double,
    val dt: Long,
    val feels_like: Double,
    val humidity: Double,
    val pressure: Double,
    val temp: Double,
    val weather: List<Weather>,
>>>>>>> Stashed changes
    val wind_speed: Double
):java.io.Serializable