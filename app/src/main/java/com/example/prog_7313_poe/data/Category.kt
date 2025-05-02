package com.example.prog_7313_poe.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val goal: Int,
    val goal_progress: Int,
    val section: String // E.g., "Bills", "Wants", etc.
)