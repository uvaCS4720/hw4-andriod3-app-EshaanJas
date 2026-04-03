package edu.nd.pmcburne.hello.data

import androidx.room.*

@Dao
interface PlacemarkDao {
    @Query("SELECT * FROM placemarks")
    suspend fun getAll(): List<PlacemarkEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)  // key for dedup!
    suspend fun insertAll(placemarks: List<PlacemarkEntity>)

    @Query("SELECT DISTINCT tagList FROM placemarks")
    suspend fun getAllTagStrings(): List<String>
}