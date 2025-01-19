package com.example.speechtotext.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NumbersModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val number: Int
)
