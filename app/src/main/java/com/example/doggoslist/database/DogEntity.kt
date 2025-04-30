package com.verticalcoding.mystudentlist.data.local.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "dogs")
data class DogEntity(
    val name: String,
    val breed: String,

    @ColumnInfo(defaultValue = "0")
    var isLiked: Boolean,

    @ColumnInfo(defaultValue = "null")
    var imageUrl: String?

) {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
}