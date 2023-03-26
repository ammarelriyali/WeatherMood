package com.example.createrecwithkotlin.DB

import androidx.room.*
import com.example.weathermood.model.OneCall

@Dao
interface DAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(oneCall : OneCall):Long

//
//    @Query("SELECT * FROM product")
//    suspend fun getPorduct(): List<Product>
}