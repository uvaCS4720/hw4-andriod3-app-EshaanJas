package edu.nd.pmcburne.hello.data

import android.content.Context
import androidx.room.*

@Database(entities = [PlacemarkEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun placemarkDao(): PlacemarkDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "placemarks.db")
                    .build().also { INSTANCE = it }
            }
    }
}