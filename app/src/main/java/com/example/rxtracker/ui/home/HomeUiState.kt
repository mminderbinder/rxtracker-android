package com.example.rxtracker.ui.home

import com.example.rxtracker.data.models.ScheduledDoseWithMedication
import java.time.LocalDate

data class HomeUiState(
    val selectedDate: LocalDate = LocalDate.now(),
    val doses: List<ScheduledDoseWithMedication> = emptyList(),
    val selectedDose: ScheduledDoseWithMedication? = null,
    val selectedBatchDoses: List<ScheduledDoseWithMedication> = emptyList(),
    val doseNotes: String? = null
)