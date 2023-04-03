
package com.example.weathermood.localdata
import androidx.room.*
import com.example.weathermood.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DAO {
    @Query("Select * from OneCallHome")
    fun getCall(): Flow<List<OneCallHome>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun  insert(oneCall: OneCallHome)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFav(favouriteLocation: FavouriteLocation)


}