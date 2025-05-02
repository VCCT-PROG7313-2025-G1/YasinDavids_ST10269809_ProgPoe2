package com.example.prog_7313_poe.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.prog_7313_poe.data.User
import com.example.prog_7313_poe.data.UserDao
import com.example.prog_7313_poe.data.Category
import com.example.prog_7313_poe.data.CategoryDao

@Database(
    entities = [User::class, Category::class], // Added Category
    version = 2, // Make sure this is greater than your previous version
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun categoryDao(): CategoryDao // New DAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "my_app_db"
                )
                   // .fallbackToDestructiveMigration() // to update database to version 2,I commented out for now because im unsure if the database will break
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}