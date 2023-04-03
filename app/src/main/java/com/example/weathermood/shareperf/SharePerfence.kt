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
    fun isNotFirstTime():Boolean{
        return sharedPreferences.getBoolean("is First Time",true)
    }
    fun setFirstTime(){
        val editor = sharedPreferences.edit()
        editor.putBoolean("is First Time",false)
        editor.apply()
    }




}