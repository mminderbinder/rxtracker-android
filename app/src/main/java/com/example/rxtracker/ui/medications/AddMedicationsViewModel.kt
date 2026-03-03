package com.example.rxtracker.ui.medications

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.rxtracker.data.models.DoseTime
import com.example.rxtracker.data.models.Frequency
import com.example.rxtracker.data.models.FrequencyDetails
import com.example.rxtracker.data.models.UserMedication
import java.time.LocalDate
import java.time.LocalTime

class AddMedicationsViewModel : ViewModel() {

    var uiState by mutableStateOf(AddMedicationsUiState())
        private set

    fun updateMedicationInfo(name: String, strength: String, form: String) {
        uiState = uiState.copy(
            name = name,
            strength = strength,
            form = form
        )
    }

    fun updateFrequency(type: Frequency, details: FrequencyDetails) {
        uiState = uiState.copy(
            frequencyType = type,
            frequencyDetails = details,
            doseTimes = emptyList()
        )
    }

    fun updateStartDate(date: LocalDate) {
        uiState = uiState.copy(startDate = date)
    }

    fun updateStartTime(time: LocalTime) {
        uiState = uiState.copy(startTime = time, doseTimes = emptyList())
    }

    fun updateEndDate(date: LocalDate?) {
        uiState = uiState.copy(endDate = date)
    }

    fun updateQuantity(qty: Double) {
        uiState = uiState.copy(quantity = qty, doseTimes = emptyList())
    }

    fun updateDoseTimes(doseTimes: List<DoseTime>) {
        uiState = uiState.copy(doseTimes = doseTimes)
    }

    fun updateRemindersEnabled(enabled: Boolean) {
        uiState = uiState.copy(remindersEnabled = enabled)
    }

    fun updateRefillReminderEnabled(enabled: Boolean) {
        uiState = uiState.copy(refillReminderEnabled = enabled)
    }

    fun updateRxNumber(number: String?) {
        uiState = uiState.copy(rxNumber = number)
    }

    fun updateInstructions(instructions: String?) {
        uiState = uiState.copy(instructions = instructions)
    }

    fun updateDoseCount(count: Int) {
        uiState = uiState.copy(doseCount = count)
    }

    fun updateRefillThreshold(threshold: Int) {
        uiState = uiState.copy(refillThreshold = threshold)
    }

    fun requiresTimesScreen(): Boolean {
        return when (uiState.frequencyType) {
            Frequency.ONCE_DAILY,
            Frequency.AS_NEEDED -> false

            else -> true
        }
    }

    fun isFixedSchedule(): Boolean {
        return when (uiState.frequencyType) {
            Frequency.ONCE_DAILY, Frequency.MULTIPLE_DAILY -> true
            else -> false
        }
    }

    fun generateInitialTimes(): List<DoseTime> {
        val generated = when (val details = uiState.frequencyDetails) {
            is FrequencyDetails.EveryXHours -> {
                val offsetHours = generateSequence(0) { it + details.hours }
                    .takeWhile { it <= 12 }
                    .toList()
                offsetHours.map { offset ->
                    DoseTime(
                        time = uiState.startTime.plusHours(offset.toLong()),
                        quantity = uiState.quantity
                    )
                }
            }

            is FrequencyDetails.MultipleTimes -> {
                val intervalHours = 12 / (details.timesPerDay - 1).coerceAtLeast(1)
                (0 until details.timesPerDay).map { i ->
                    DoseTime(
                        time = uiState.startTime.plusHours((i * intervalHours).toLong()),
                        quantity = uiState.quantity
                    )
                }
            }

            else -> listOf(DoseTime(time = uiState.startTime, quantity = uiState.quantity))
        }
        return generated.ifEmpty {
            listOf(DoseTime(time = uiState.startTime, quantity = uiState.quantity))
        }
    }

    fun toUserMedication(): UserMedication {
        return UserMedication(

        )
    }
}