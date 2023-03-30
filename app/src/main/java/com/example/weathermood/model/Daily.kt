package com.example.weathermood.model

data class Daily(
<<<<<<< Updated upstream
    val clouds: Int,
    val dew_point: Double,
    val dt: Int,
    val feels_like: FeelsLike,
    val humidity: Int,
    val moon_phase: Double,
    val moonrise: Int,
    val moonset: Int,
    val pop: Double,
    val pressure: Int,
    val rain: Double,
    val sunrise: Int,
    val sunset: Int,
=======
    val dt: Long,
    val feels_like: FeelsLike,
>>>>>>> Stashed changes
    val temp: Temp,
    val weather: List<Weather>,
<<<<<<< Updated upstream
    val wind_deg: Int,
    val wind_speed: Double
)
=======
):java.io.Serializable
>>>>>>> Stashed changes
