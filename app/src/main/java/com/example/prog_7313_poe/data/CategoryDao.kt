package com.example.prog_7313_poe.data

import androidx.room.*

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category)

    @Update
    suspend fun updateCategory(category: Category)

    @Delete
    suspend fun deleteCategory(category: Category)

    @Query("SELECT * FROM categories")
    suspend fun getAllCategories(): List<Category>

    @Query("SELECT * FROM categories WHERE section = :section")
    suspend fun getCategoriesBySection(section: String): List<Category>

    @Query("SELECT name FROM categories")
    suspend fun getAllCategoryNames(): List<String>

    @Query("SELECT * FROM categories WHERE name = :categoryName LIMIT 1")
    suspend fun getCategoryByName(categoryName: String): Category?

    @Query("UPDATE categories SET goal_progress = :newGoalProgress WHERE name = :categoryName")
    suspend fun updateGoalProgress(categoryName: String, newGoalProgress: Double)
}