package com.example.rxtracker.data.models

data class MedicationData(
    val name: String = "",
    val strength: String = "",
    val form: String = "",
    val frequencyType: Frequency? = null,
    val frequencyDetails: FrequencyDetails? = null,
    val doseTimes: List<DoseTimes> = emptyList()
)
