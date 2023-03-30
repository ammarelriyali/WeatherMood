
package com.example.weathermood.localdata
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weathermood.model.OneCall

@Database(entities = arrayOf(OneCall::class), version = 1)
@TypeConverters(DataConverter::class)
abstract class WeatherDB : RoomDatabase() {
    abstract fun getDao(): DAO

    companion object {
        @Volatile
        private var INSTANCE: WeatherDB? = null
        fun getInstance(ctx: Context): WeatherDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    ctx.applicationContext, WeatherDB::class.java, "Weather_Mood"
                ).build()

                instance

            }
        }
    }
}






