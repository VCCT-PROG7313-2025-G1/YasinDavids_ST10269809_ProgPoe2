package com.example.prog_7313_poe.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val goal: Double,
    val goal_progress: Double,
    val section: String // E.g., "Bills", "Wants", etc.
)