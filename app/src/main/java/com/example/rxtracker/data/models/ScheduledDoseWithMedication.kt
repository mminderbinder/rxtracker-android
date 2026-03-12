package com.example.rxtracker.data.models

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

data class ScheduledDoseWithMedication(
    val id: Long,
    val medicationId: Long,
    val scheduledDate: LocalDate,
    val scheduledTime: LocalTime,
    val quantity: Double,
    val status: DoseStatus,
    val takenAt: LocalDateTime?,
    val name: String,
    val strength: String,
    val form: String
)
