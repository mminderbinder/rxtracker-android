package com.example.rxtracker.ui.medications

import com.example.rxtracker.data.models.DoseTime
import com.example.rxtracker.data.models.Frequency
import com.example.rxtracker.data.models.FrequencyDetails
import java.time.LocalDate
import java.time.LocalTime


data class AddMedicationsUiState(
    val name: String = "",
    val strength: String = "",
    val form: String = "",
    val frequencyType: Frequency? = null,
    val frequencyDetails: FrequencyDetails? = null,
    val startDate: LocalDate = LocalDate.now(),
    val startTime: LocalTime = LocalTime.of(8, 0),
    val quantity: Double = 1.0,
    val doseTimes: List<DoseTime> = emptyList()
)
