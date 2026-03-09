package com.example.rxtracker.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
    tableName = "medications",
//    foreignKeys = [
//        ForeignKey(
//            entity = Profile::class,
//            parentColumns = ["id"],
//            childColumns = ["profileId"],
//            onDelete = ForeignKey.CASCADE
//        )],
//    indices = [Index("profileId")]
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
    val intakeTime: IntakeTime? = null,
    val profileId: Long = 0
)
