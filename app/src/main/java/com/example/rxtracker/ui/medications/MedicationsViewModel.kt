package com.example.rxtracker.ui.medications

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.rxtracker.data.models.DoseTimes
import com.example.rxtracker.data.models.Frequency
import com.example.rxtracker.data.models.FrequencyDetails
import com.example.rxtracker.data.models.MedicationData

class MedicationsViewModel : ViewModel() {
    var medicationData by mutableStateOf(MedicationData())
        private set

    fun updateMedicationInfo(name: String, strength: String, form: String) {
        medicationData = medicationData.copy(
            name = name,
            strength = strength,
            form = form
        )
    }

    fun updateFrequency(type: Frequency, details: FrequencyDetails) {
        medicationData = medicationData.copy(
            frequencyType = type,
            frequencyDetails = details
        )
    }

    fun updateDoseTimes(doseTimes: List<DoseTimes>) {
        medicationData = medicationData.copy(
            doseTimes = doseTimes
        )
    }

    fun getTimesPerDay(): Int {
        return when (val details = medicationData.frequencyDetails) {
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