package com.example.rxtracker.data.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

@Entity(
    tableName = "scheduled_doses",
    foreignKeys = [ForeignKey(
        entity = UserMedication::class,
        parentColumns = ["id"],
        childColumns = ["medicationId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("medicationId")]
)
data class ScheduledDose(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val medicationId: Long,
    val scheduledDate: LocalDate,
    val scheduledTime: LocalTime,
    val rescheduledTime: LocalTime? = null,
    val quantity: Double,
    val status: DoseStatus = DoseStatus.PENDING,
    val resolvedAt: LocalDateTime? = null,
    val doseNotes: String? = null
)
