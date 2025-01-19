package com.example.speechtotext

import android.app.Application
import androidx.room.Room
import com.example.speechtotext.db.NumbersDB

class MainApplication : Application() {
    companion object {
        lateinit var numbersDB: NumbersDB
    }
    override fun onCreate() {
        super.onCreate()
        numbersDB = Room.databaseBuilder(
            applicationContext,
            NumbersDB::class.java,
            NumbersDB.DATABASE_NAME
        ).build()
    }
}