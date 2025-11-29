package com.example.myapplication.features.dollar.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.features.dollar.data.database.entity.DollarEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface IDollarDao {
    @Query("SELECT * FROM dollars")
    suspend fun getList(): List<DollarEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(dollar: DollarEntity)

    @Query("DELETE FROM dollars")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDollars(lists: List<DollarEntity>)

    @Query("SELECT * FROM dollars ORDER BY timestamp DESC")
    fun getHistory(): Flow<List<DollarEntity>>

    @Delete
    suspend fun delete(dollar: DollarEntity) // eliminar un registro específico
}