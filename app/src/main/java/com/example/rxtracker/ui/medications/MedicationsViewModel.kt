package com.example.rxtracker.ui.medications

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.rxtracker.data.models.Frequency
import com.example.rxtracker.data.models.FrequencyDetails
import com.example.rxtracker.data.models.MedicationEntity

class MedicationsViewModel : ViewModel() {
    var medicationEntity by mutableStateOf(MedicationEntity())
        private set

    fun updateMedicationInfo(name: String, strength: String, form: String) {
        medicationEntity = medicationEntity.copy(
            name = name,
            strength = strength,
            form = form
        )
    }

    fun updateFrequency(type: Frequency, details: FrequencyDetails) {
        medicationEntity = medicationEntity.copy(
            frequencyType = type,
            frequencyDetails = details
        )
    }

    fun getTimesPerDay(): Int {
        return when (val details = medicationEntity.frequencyDetails) {
            is FrequencyDetails.OnceDaily -> 1
            is FrequencyDetails.MultipleTimes -> details.timesPerDay
            is FrequencyDetails.AsNeeded -> 0
            is FrequencyDetails.EveryXHours -> 24 / details.hours
            is FrequencyDetails.EveryXDays -> 1
            is FrequencyDetails.SpecificWeekdays -> 1
            is FrequencyDetails.Cyclic -> 1
            null -> 0
        }
    }
}