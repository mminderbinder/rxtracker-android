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

class MedicationsViewModel : ViewModel() {
    var userMedication by mutableStateOf(UserMedication())
        private set

    var pendingStartTime: LocalTime = LocalTime.of(8, 0)
        private set

    var pendingQuantity: Double = 1.0
        private set

    fun updateMedicationInfo(name: String, strength: String, form: String) {
        userMedication = userMedication.copy(
            name = name,
            strength = strength,
            form = form
        )
    }

    fun updateFrequency(type: Frequency, details: FrequencyDetails) {
        userMedication = userMedication.copy(
            frequencyType = type,
            frequencyDetails = details
        )
    }

    fun updateDoseDetails(startDate: LocalDate, startTime: LocalTime, quantity: Double) {
        pendingStartTime = startTime
        pendingQuantity = quantity

        userMedication = when (userMedication.frequencyType) {
            Frequency.ONCE_DAILY -> {
                userMedication.copy(
                    startDate = startDate,
                    doseTimes = listOf(DoseTime(time = startTime, quantity = quantity))
                )
            }

            else -> userMedication.copy(startDate = startDate)
        }
    }


    fun updateDoseTimes(doseTimes: List<DoseTime>) {
        userMedication = userMedication.copy(doseTimes = doseTimes)
    }

    fun requiresTimesScreen(): Boolean {
        return when (userMedication.frequencyType) {
            Frequency.ONCE_DAILY,
            Frequency.AS_NEEDED -> false

            else -> true
        }
    }

    fun isFixedSchedule(): Boolean {
        return when (userMedication.frequencyType) {
            Frequency.ONCE_DAILY, Frequency.MULTIPLE_DAILY -> true
            else -> false
        }
    }

    fun generateInitialTimes(): List<DoseTime> {
        val generated = when (val details = userMedication.frequencyDetails) {
            is FrequencyDetails.EveryXHours -> {
                val offsetHours = generateSequence(0) { it + details.hours }
                    .takeWhile { it < 12 }
                    .toList()
                offsetHours.map { offset ->
                    DoseTime(
                        time = pendingStartTime.plusHours(offset.toLong()),
                        quantity = pendingQuantity
                    )
                }
            }

            is FrequencyDetails.MultipleTimes -> {
                val intervalHours = 12 / (details.timesPerDay - 1).coerceAtLeast(1)
                (0 until details.timesPerDay).map { i ->
                    DoseTime(
                        time = pendingStartTime.plusHours((i * intervalHours).toLong()),
                        quantity = pendingQuantity
                    )
                }
            }

            else -> listOf(DoseTime(time = pendingStartTime, quantity = pendingQuantity))
        }
        return generated.ifEmpty {
            listOf(DoseTime(time = pendingStartTime, quantity = pendingQuantity))
        }
    }
}