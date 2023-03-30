package com.example.weathermood.localdata

import androidx.room.TypeConverter
import com.example.weathermood.model.Current
import com.example.weathermood.model.Daily
import com.example.weathermood.model.Hourly
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class DataConverter {
    @TypeConverter
    fun fromCountryDailyList(dailyList: List<Daily>?): String? {
        if (dailyList == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Daily>?>() {}.type
        return gson.toJson(dailyList, type)
    }

    @TypeConverter
    fun fromCountryHourlyList(hourlyList: List<Hourly>?): String? {
        if (hourlyList == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Hourly>?>() {}.type
        return gson.toJson(hourlyList, type)
    }

    @TypeConverter
    fun fromCurrent(current: Current?): String? {
        if (current == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<Current?>() {}.type
        return gson.toJson(current, type)
    }

    @TypeConverter
    fun toHourlyList(hourlyString: String?): List<Hourly>? {
        if (hourlyString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Hourly>?>() {}.type
        return gson.fromJson(hourlyString, type)
    }

    @TypeConverter
    fun toDailyList(dailyString: String?): List<Daily>? {
        if (dailyString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Daily>?>() {}.type
        return gson.fromJson(dailyString, type)
    }

    @TypeConverter
    fun toCurrent(current: String?): Current? {
        if (current == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<Current?>() {}.type
        return gson.fromJson(current, type)
    }
}