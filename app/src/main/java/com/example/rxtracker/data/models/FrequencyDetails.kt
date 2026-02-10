package com.example.rxtracker.data.models

import com.example.rxtracker.ui.medications.components.dialogs.DayOfWeek

sealed class FrequencyDetails {
    object OnceDaily : FrequencyDetails()
    object AsNeeded : FrequencyDetails()
    data class MultipleTimes(val timesPerDay: Int) : FrequencyDetails()
    data class EveryXHours(val hours: Int) : FrequencyDetails()
    data class EveryXDays(val days: Int) : FrequencyDetails()
    data class SpecificWeekdays(val days: Set<DayOfWeek>) : FrequencyDetails()
    data class Cyclic(val intakeDays: Int, val pauseDays: Int) : FrequencyDetails()
}