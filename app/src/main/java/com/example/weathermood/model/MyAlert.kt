package com.example.weathermood.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MyAlert (
    @PrimaryKey(autoGenerate = true)
    val id :Int=0,
    var dateForm:Long= 0L,
    var hourFrom:Int = 0,
    var minuteFrom:Int = 0,
    var dateTo:Long = 0L,
    var hourTo:Int = 0,
    var minuteTo :Int = 0,
    var lat:Double = 0.0,
    var log:Double = 0.0 ,
    var city :String="empty",
    var event :String="Rain",
    var typeOfAlert:String="alert"
)