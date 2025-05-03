package com.example.prog_7313_poe.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [User::class, Category::class, Account::class], // Added Category
    version = 4, // Make sure this is greater than your previous version
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun categoryDao(): CategoryDao // New DAO
    abstract fun accountDao(): AccountDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Migration from version 2 to version 3
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Create a temporary table with the updated column types
                db.execSQL("""
                    CREATE TABLE categories_temp (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        name TEXT NOT NULL,
                        section TEXT NOT NULL,
                        goal REAL NOT NULL,
                        goal_progress REAL NOT NULL
                    )
                """)

                // Copy the data from the old table to the temporary table..
                db.execSQL("""
                    INSERT INTO categories_temp (id, name, section, goal, goal_progress)
                    SELECT id, name, section, goal, goal_progress FROM categories
                """)

                // Drop the old table
                db.execSQL("DROP TABLE categories")

                // Rename the temporary table to the original table name
                db.execSQL("ALTER TABLE categories_temp RENAME TO categories")
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "my_app_db"
                )
                   // .fallbackToDestructiveMigration() // to update database to version 2,I commented out for now because im unsure if the database will break
                    .addMigrations(MIGRATION_2_3)
                    .build()
                INSTANCE = instance
                instance
            }
        }


    }
}