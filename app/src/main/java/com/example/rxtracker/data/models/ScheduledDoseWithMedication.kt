package com.example.rxtracker.data.models

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

data class ScheduledDoseWithMedication(
    val id: Long,
    val medicationId: Long,
    val scheduledDate: LocalDate,
    val rescheduledDate: LocalDateTime?,
    val scheduledTime: LocalTime,
    val quantity: Double,
    val status: DoseStatus,
    val resolvedAt: LocalDateTime?,
    val doseNotes: String?,
    val name: String,
    val strength: String,
    val form: String
)
