package com.example.prog_7313_poe.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AccountDao {
    @Insert
    suspend fun insert(account: Account)

    @Query("SELECT * FROM Account")
    suspend fun getAll(): List<Account>
}