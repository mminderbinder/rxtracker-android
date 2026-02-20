package com.example.rxtracker.data.models

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator("type")
sealed class FrequencyDetails {
    @Serializable
    @SerialName("OnceDaily")
    object OnceDaily : FrequencyDetails()

    @Serializable
    @SerialName("AsNeeded")
    object AsNeeded : FrequencyDetails()

    @Serializable
    @SerialName("MultipleTimes")
    data class MultipleTimes(val timesPerDay: Int) : FrequencyDetails()

    @Serializable
    @SerialName("EveryXHours")
    data class EveryXHours(val hours: Int) : FrequencyDetails()

    @Serializable
    @SerialName("EveryXDays")
    data class EveryXDays(val days: Int) : FrequencyDetails()

    @Serializable
    @SerialName("SpecificWeekdays")
    data class SpecificWeekdays(val days: Set<DayOfWeek>) : FrequencyDetails()

    @Serializable
    @SerialName("Cyclic")
    data class Cyclic(val intakeDays: Int, val pauseDays: Int) : FrequencyDetails()
}