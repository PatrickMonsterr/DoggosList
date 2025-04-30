package com.example.doggoslist

import android.app.Application
import androidx.room.Room
import com.verticalcoding.mystudentlist.data.local.database.AppDatabase

class DoggoApplication : Application() {

    lateinit var database: AppDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "doggos-db"
        ).build()
    }
}
