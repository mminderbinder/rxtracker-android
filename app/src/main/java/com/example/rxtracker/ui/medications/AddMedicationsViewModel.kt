package com.example.rxtracker.ui.medications

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.rxtracker.data.models.DoseTime
import com.example.rxtracker.data.models.Frequency
import com.example.rxtracker.data.models.FrequencyDetails
import com.example.rxtracker.data.models.UserMedication
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class AddMedicationsViewModel @Inject constructor() : ViewModel() {

    var uiState by mutableStateOf(AddMedicationsUiState())
        private set

    /**
     * Forward cascade wipe. Resets all fields belonging to screens
     * at or after the given step back to defaults.
     *
     * Screen 1: name, strength, form (handled by full reset)
     *
     * Screen 2: frequencyType, frequencyDetails
     * Screen 3: startDate, startTime, quantity
     * Screen 4: doseTimes
     * Screen 5: reminders, refill, endDate, rxNumber, instructions
     */

    private fun wipeFrom(step: Int, state: AddMedicationsUiState): AddMedicationsUiState {
        var s = state
        if (step <= 4) s = s.copy(doseTimes = emptyList())
        return s
    }

    // Screen 1
    fun updateMedicationInfo(name: String, strength: String, form: String) {
        uiState = AddMedicationsUiState(
            medicationInfo = MedicationInfo(
                name = name,
                strength = strength,
                form = form
            )
        )
    }

    // Screen 2
    fun updateFrequency(type: Frequency, details: FrequencyDetails) {
        uiState = wipeFrom(
            step = 3,
            state = uiState.copy(
                frequency = FrequencyState(type = type, details = details),
            )
        )
    }

    // Screen 3
    fun updateStartDate(date: LocalDate) {
        uiState = wipeFrom(
            step = 4,
            state = uiState.copy(doseDetails = uiState.doseDetails.copy(startDate = date))
        )
    }

    fun updateStartTime(time: LocalTime) {
        uiState = wipeFrom(
            step = 4,
            state = uiState.copy(doseDetails = uiState.doseDetails.copy(startTime = time))
        )
    }

    fun updateQuantity(qty: Double) {
        uiState = wipeFrom(
            step = 4,
            state = uiState.copy(doseDetails = uiState.doseDetails.copy(quantity = qty))
        )
    }

    // Screen 4
    fun updateDoseTimes(doseTimes: List<DoseTime>) {
        uiState = uiState.copy(doseTimes = doseTimes)
    }

    // Screen 5
    fun updateRemindersEnabled(enabled: Boolean) {
        uiState =
            uiState.copy(optionalDetails = uiState.optionalDetails.copy(remindersEnabled = enabled))
    }

    fun updateRefillReminderEnabled(enabled: Boolean) {
        uiState =
            uiState.copy(optionalDetails = uiState.optionalDetails.copy(refillReminderEnabled = enabled))
    }

    fun updateInstructions(instructions: String?) {
        uiState =
            uiState.copy(optionalDetails = uiState.optionalDetails.copy(instructions = instructions))
    }

    fun updateDoseCount(count: Int) {
        uiState = uiState.copy(optionalDetails = uiState.optionalDetails.copy(doseCount = count))
    }

    fun updateRefillThreshold(threshold: Int) {
        uiState =
            uiState.copy(optionalDetails = uiState.optionalDetails.copy(refillThreshold = threshold))
    }

    fun requiresTimesScreen(): Boolean {
        return when (uiState.frequency.type) {
            Frequency.ONCE_DAILY,
            Frequency.AS_NEEDED -> false

            else -> true
        }
    }

    fun isFixedSchedule(): Boolean {
        return when (uiState.frequency.type) {
            Frequency.ONCE_DAILY,
            Frequency.MULTIPLE_DAILY -> true

            else -> false
        }
    }

    fun generateInitialTimes(): List<DoseTime> {
        val startTime = uiState.doseDetails.startTime ?: LocalTime.of(8, 0)
        val quantity = uiState.doseDetails.quantity ?: 1.0

        val generated = when (val details = uiState.frequency.details) {
            is FrequencyDetails.EveryXHours -> {
                val offsetHours = generateSequence(0) { it + details.hours }
                    .takeWhile { it <= 12 }
                    .toList()
                offsetHours.map { offset ->
                    DoseTime(
                        time = startTime.plusHours(offset.toLong()),
                        quantity = quantity
                    )
                }
            }

            is FrequencyDetails.MultipleTimes -> {
                val intervalHours = 12 / (details.timesPerDay - 1).coerceAtLeast(1)
                (0 until details.timesPerDay).map { i ->
                    DoseTime(
                        time = startTime.plusHours((i * intervalHours).toLong()),
                        quantity = quantity
                    )
                }
            }

            else -> listOf(
                DoseTime(
                    time = startTime,
                    quantity = quantity
                )
            )
        }
        return generated.ifEmpty {
            listOf(
                DoseTime(
                    time = startTime,
                    quantity = quantity
                )
            )
        }
    }

    fun toUserMedication(): UserMedication {
        return UserMedication(

        )
    }
}