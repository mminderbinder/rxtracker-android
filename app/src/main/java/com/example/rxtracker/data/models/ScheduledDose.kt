package com.example.rxtracker.data.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

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
    val quantity: Double,
    val status: DoseStatus = DoseStatus.PENDING,
    val resolvedAt: LocalDateTime? = null,
    val doseNotes: String? = null
)
