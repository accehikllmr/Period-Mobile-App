package com.example.period_app_01.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy
import kotlinx.coroutines.flow.Flow

/*
 * Data Access Object provides methods for performing SQL actions, without writing SQL code
 */
@Dao
interface DatesDao {
    // not expecting conflicts, since data only added to table from single source
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    /*
     * function to insert entry into database, passing Dates entity as argument
     * using coroutines, suspend keyword makes it run on a different thread
     * i.e. not block other operations in same thread, while awaiting SQL query result
     */
    suspend fun insert(dates: Dates)

    @Query("DELETE FROM Dates WHERE id = (SELECT MAX(id) FROM Dates)")
    suspend fun deleteLast()

    @Query("SELECT * FROM Dates ORDER BY id DESC LIMIT 1")
    fun getLastEntry(): Flow<Dates?>
}