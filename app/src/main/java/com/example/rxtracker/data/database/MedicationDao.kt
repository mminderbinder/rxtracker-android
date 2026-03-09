package com.example.rxtracker.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.rxtracker.data.models.UserMedication
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicationDao {

    @Insert
    suspend fun insert(medication: UserMedication): Long

    @Query("SELECT * FROM medications")
    fun getAll(): Flow<List<UserMedication>>

    @Query("SELECT * FROM medications WHERE id = :id")
    suspend fun getById(id: Long): UserMedication?

    @Delete
    suspend fun delete(medication: UserMedication)
}