package com.example.rxtracker.data.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    tableName = "doses_taken",
    foreignKeys = [ForeignKey(
        entity = UserMedication::class,
        parentColumns = ["id"],
        childColumns = ["medicationId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("medicationId")]
)
data class DoseTaken(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val medicationId: Long,
    val takenAt: LocalDateTime,
    val quantity: Double,
    val notes: String? = null
)
