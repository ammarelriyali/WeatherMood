
package com.example.weathermood.localdata
import androidx.room.*
import com.example.weathermood.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.selects.select

@Dao
interface DAO {
    @Query("Select * from OneCallHome")
    fun getCall(): Flow<List<OneCallHome>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun  insert(oneCall: OneCallHome)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFav(favouriteLocation: FavouriteLocation)
    @Query("Select * from FavouriteLocation")
    fun getFavItems(): Flow<List<FavouriteLocation>>
    @Delete
   suspend fun deleteFav(data: FavouriteLocation)

   @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alertModel: AlertModel):Long

    @Query("Select * from AlertModel")
    fun getAlertItems(): Flow<List<AlertModel>>
    @Delete
    suspend fun deleteAlert(it: AlertModel)
    @Query("Select * from AlertModel where id = :id LIMIT 1" )
    suspend fun getAlert(id: Int):AlertModel
}