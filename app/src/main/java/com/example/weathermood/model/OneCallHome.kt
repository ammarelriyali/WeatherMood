package com.example.weathermood.model

import androidx.room.Entity

@Entity( primaryKeys = ["id"])
data class OneCallHome (
    var id:Int =1,
    var oneCall:OneCall,
    var city: String = "Empty",
):java.io.Serializable