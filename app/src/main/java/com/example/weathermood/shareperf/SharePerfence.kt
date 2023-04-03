package com.example.weathermood.shareperf

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log

object MySharedPreference{
    private const val NAME = "MySharedPreferenceWM"
    private lateinit var sharedPreferences :SharedPreferences
    fun getInstance(activity: Activity){
        Log.i("TAG", "getInstance: ")
        sharedPreferences = activity.getSharedPreferences(NAME, Context.MODE_PRIVATE)
    }
    fun isFirstTime():Boolean{
        return sharedPreferences.getBoolean("is First Time",true)
    }
    fun setFirstTime(){
        val editor = sharedPreferences.edit()
        editor.putBoolean("is First Time",false)
        editor.apply()
    }

    fun getWeatherFromMap(): Boolean {
        return sharedPreferences.getBoolean("get weather form map",true)
    }
    fun setWeatherFromMap(boolean: Boolean)  {
        Log.i("TAG", "setWeatherFromMap: $boolean")
        val editor = sharedPreferences.edit()
        editor.putBoolean("get weather form map",boolean)
        editor.apply()
    }


}