package com.example.speechtotext.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [NumbersModel::class], version = 1)
abstract class NumbersDB:RoomDatabase() {
    companion object {
        const val DATABASE_NAME = "numbers_db"
    }
    abstract fun numbersDao(): NumbersDao
}