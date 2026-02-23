package com.example.rxtracker.ui.medications

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.rxtracker.data.models.DoseTime
import com.example.rxtracker.data.models.Frequency
import com.example.rxtracker.data.models.FrequencyDetails
import com.example.rxtracker.data.models.UserMedication
import java.time.LocalTime

class MedicationsViewModel : ViewModel() {
    var userMedication by mutableStateOf(UserMedication())
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

    fun updateDoseTimes(doseTimes: List<DoseTime>) {
        userMedication = userMedication.copy(doseTimes = doseTimes)
    }

    fun getIntervalHours(): Int {
        return when (val details = userMedication.frequencyDetails) {
            is FrequencyDetails.EveryXHours -> details.hours
            is FrequencyDetails.MultipleTimes -> {
                12 / (details.timesPerDay - 1).coerceAtLeast(1)
            }

            else -> 4
        }
    }

    fun generateInitialTimes(): List<DoseTime> {
        val start = LocalTime.of(8, 0)
        val generated = when (val details = userMedication.frequencyDetails) {
            is FrequencyDetails.EveryXHours -> {
                val count = 24 / details.hours
                (0 until count).map { i ->
                    DoseTime(time = start.plusHours((i * details.hours).toLong()), quantity = 1.0)
                }
            }

            is FrequencyDetails.MultipleTimes -> {
                val intervalHours = 12 / (details.timesPerDay - 1).coerceAtLeast(1)
                (0 until details.timesPerDay).map { i ->
                    DoseTime(time = start.plusHours((i * intervalHours).toLong()), quantity = 1.0)
                }
            }

            else -> listOf(DoseTime(time = start, quantity = 1.0))
        }
        return generated.filter { it.time >= start }
            .ifEmpty { listOf(DoseTime(time = start, quantity = 1.0)) }
    }
}