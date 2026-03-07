package com.example.rxtracker.ui.medications

import com.example.rxtracker.data.models.DoseTime
import com.example.rxtracker.data.models.EndDateMode
import com.example.rxtracker.data.models.Frequency
import com.example.rxtracker.data.models.FrequencyDetails
import java.time.LocalDate
import java.time.LocalTime

data class MedicationInfo(
    val name: String = "",
    val strength: String = "",
    val form: String = ""
) {
    val selectionSummary: String get() = "$name $strength $form"
}

data class FrequencyState(
    val type: Frequency? = null,
    val details: FrequencyDetails? = null
)

data class DoseDetailsState(
    val startDate: LocalDate = LocalDate.now(),
    val startTime: LocalTime = LocalTime.of(8, 0),
    val quantity: Double = 1.0
)

data class OptionalDetailsState(
    val remindersEnabled: Boolean = true,
    val refillReminderEnabled: Boolean = false,
    val endDateMode: EndDateMode = EndDateMode.ONGOING,
    val endDate: LocalDate? = null,
    val instructions: String? = null,
    val doseCount: Int? = null,
    val refillThreshold: Int? = null
)

data class AddMedicationsUiState(
    val medicationInfo: MedicationInfo = MedicationInfo(),
    val frequency: FrequencyState = FrequencyState(),
    val doseDetails: DoseDetailsState = DoseDetailsState(),
    val doseTimes: List<DoseTime> = emptyList(),
    val optionalDetails: OptionalDetailsState = OptionalDetailsState()
)