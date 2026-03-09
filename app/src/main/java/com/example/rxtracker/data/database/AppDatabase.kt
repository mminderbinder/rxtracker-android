package com.example.rxtracker.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.rxtracker.data.converters.Converters
import com.example.rxtracker.data.models.UserMedication

@Database(
    entities = [UserMedication::class],
    version = 1,
    exportSchema = true
)

@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun medicationDao(): MedicationDao
}