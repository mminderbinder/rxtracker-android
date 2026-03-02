package com.example.rxtracker.data.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
    tableName = "medication",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )],
    indices = [Index("userId")]
)
data class UserMedication(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String = "",
    val strength: String = "",
    val form: String = "",
    val frequencyType: Frequency? = null,
    val startDate: LocalDate = LocalDate.now(),
    val endDate: LocalDate? = null,
    val frequencyDetails: FrequencyDetails? = null,
    val doseTimes: List<DoseTime> = mutableListOf(),
    val remindersEnabled: Boolean = true,
    val rxNumber: String? = null,
    val instructions: String? = null,
    val refillReminderEnabled: Boolean = false,
    val refillThreshold: Int? = null,
    val doseCount: Int? = null,
    val userId: Long = 0
)
