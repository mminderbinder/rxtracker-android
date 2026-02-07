package com.example.rxtracker.ui.medications

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

data class MedicationData(
    val name: String = "",
    val strength: String = "",
    val form: String = ""
)

class MedicationsViewModel : ViewModel() {
    var medicationData by mutableStateOf(MedicationData())
        private set

    fun updateMedicationInfo(name: String, strength: String, form: String) {
        medicationData = MedicationData(name, strength, form)
    }
}