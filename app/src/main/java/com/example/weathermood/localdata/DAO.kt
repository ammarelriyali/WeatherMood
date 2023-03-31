
package com.example.weathermood.localdata
import androidx.room.*
import com.example.weathermood.model.OneCall
import kotlinx.coroutines.flow.Flow

@Dao
interface DAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(oneCall : OneCall):Flow<Long>
    @Query("Select * from OneCall WHERE isHome=true ")
    fun getCall(): Flow<List<OneCall>>




}