package com.example.weathermood.model

import androidx.room.Entity

@Entity( primaryKeys = ["longitude","latitude"])
data class FavouriteLocation(var longitude: String,var latitude: String,var city: String):java.io.Serializable
