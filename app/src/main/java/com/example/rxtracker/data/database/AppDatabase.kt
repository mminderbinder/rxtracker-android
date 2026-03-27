package com.example.rxtracker.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.rxtracker.data.converters.Converters
import com.example.rxtracker.data.models.DoseTaken
import com.example.rxtracker.data.models.ScheduledDose
import com.example.rxtracker.data.models.UserMedication

@Database(
    entities = [
        UserMedication::class,
        ScheduledDose::class,
        DoseTaken::class
    ],
    version = 5,
    exportSchema = true
)

@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun medicationDao(): MedicationDao
    abstract fun scheduledDoseDao(): ScheduledDoseDao
    abstract fun doseTakenDao(): DoseTakenDao
}