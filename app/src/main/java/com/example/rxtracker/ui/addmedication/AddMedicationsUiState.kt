package com.example.rxtracker.ui.addmedication

import com.example.rxtracker.data.models.DoseTime
import com.example.rxtracker.data.models.Frequency
import com.example.rxtracker.data.models.FrequencyDetails
import com.example.rxtracker.data.models.IntakeTime
import com.example.rxtracker.utils.today
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

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
    val startDate: LocalDate = today(),
    val startTime: LocalTime = LocalTime(8, 0),
    val quantity: Double = 1.0
)

data class OptionalDetailsState(
    val remindersEnabled: Boolean = true,
    val refillReminderEnabled: Boolean = false,
    val totalQuantity: Int? = null,
    val refillThreshold: Int? = null,
    val intakeTime: IntakeTime? = null,
    val instructions: String? = null,
)

data class AddMedicationsUiState(
    val medicationInfo: MedicationInfo = MedicationInfo(),
    val frequency: FrequencyState = FrequencyState(),
    val doseDetails: DoseDetailsState = DoseDetailsState(),
    val doseTimes: List<DoseTime> = emptyList(),
    val optionalDetails: OptionalDetailsState = OptionalDetailsState(),
    val isSaving: Boolean = false,
    val saveError: String? = null
)