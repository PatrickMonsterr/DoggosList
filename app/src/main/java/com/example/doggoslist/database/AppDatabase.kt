package com.verticalcoding.mystudentlist.data.local.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DogEntity::class], version = 2)
abstract class AppDatabase: RoomDatabase() {
    abstract fun dogDao(): DogEntityDao
}