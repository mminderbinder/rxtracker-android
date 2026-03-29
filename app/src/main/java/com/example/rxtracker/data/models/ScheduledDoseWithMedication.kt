package com.example.rxtracker.data.models

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime


data class ScheduledDoseWithMedication(
    val id: Long,
    val medicationId: Long,
    val scheduledDate: LocalDate,
    val scheduledTime: LocalTime,
    val rescheduledTime: LocalTime? = null,
    val quantity: Double,
    val status: DoseStatus,
    val resolvedAt: LocalDateTime?,
    val doseNotes: String?,
    val name: String,
    val strength: String,
    val form: String
)
