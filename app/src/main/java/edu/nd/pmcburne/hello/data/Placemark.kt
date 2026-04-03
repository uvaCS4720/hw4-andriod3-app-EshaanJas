package edu.nd.pmcburne.hello.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName

data class VisualCenter(val latitude: Double, val longitude: Double)

data class PlacemarkDto(
    val id: Int,
    val name: String,
    val description: String,
    @SerializedName("tag_list") val tagList: List<String>,
    @SerializedName("visual_center") val visualCenter: VisualCenter
)

@Entity(tableName = "placemarks")
@TypeConverters(Converters::class)
data class PlacemarkEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val description: String,
    val tagList: List<String>,
    val latitude: Double,
    val longitude: Double
)

class Converters {
    @TypeConverter
    fun fromList(value: List<String>): String = value.joinToString(",")
    @TypeConverter
    fun toList(value: String): List<String> = if (value.isEmpty()) emptyList() else value.split(",")
}