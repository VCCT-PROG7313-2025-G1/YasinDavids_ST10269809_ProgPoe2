package com.example.prog_7313_poe.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Account(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String,
    val name: String,
    val startingAmount: Double,
    val notes: String?
)