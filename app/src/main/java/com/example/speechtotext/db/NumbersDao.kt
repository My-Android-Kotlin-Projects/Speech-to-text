package com.example.speechtotext.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NumbersDao {
    @Insert
    fun insertNumber(number: NumbersModel)

    @Query("SELECT * FROM NumbersModel")
    fun getNumber(): LiveData<List<NumbersModel>>

    @Query("DELETE FROM NumbersModel")
    fun deleteAllNumbers()

    @Query("DELETE FROM NumbersModel WHERE id = :id")
    fun deleteNumber(id: Int)

    @Query("SELECT SUM(number) FROM NumbersModel")
    fun getSum(): LiveData<Int>
}