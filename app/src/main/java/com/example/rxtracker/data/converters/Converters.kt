package com.example.rxtracker.data.converters

import androidx.room.TypeConverter
import com.example.rxtracker.data.models.DoseTime
import com.example.rxtracker.data.models.Frequency
import com.example.rxtracker.data.models.FrequencyDetails
import kotlinx.serialization.json.Json
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class Converters {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromLocalDate(date: LocalDate): String = date.toString()

    @TypeConverter
    fun toLocalDate(value: String): LocalDate = LocalDate.parse(value)

    @TypeConverter
    fun fromLocalTime(time: LocalTime): String = time.toString()

    @TypeConverter
    fun toLocalTime(value: String): LocalTime = LocalTime.parse(value)

    @TypeConverter
    fun fromLocalDateTime(dt: LocalDateTime): String = dt.toString()

    @TypeConverter
    fun toLocalDateTime(value: String): LocalDateTime = LocalDateTime.parse(value)

    @TypeConverter
    fun fromFrequency(freq: Frequency): String = freq.name

    @TypeConverter
    fun toFrequency(value: String): Frequency = Frequency.valueOf(value)

    @TypeConverter
    fun fromFrequencyDetails(details: FrequencyDetails): String =
        json.encodeToString(FrequencyDetails.serializer(), details)

    @TypeConverter
    fun toFrequencyDetails(value: String): FrequencyDetails =
        json.decodeFromString(FrequencyDetails.serializer(), value)

    @TypeConverter
    fun fromDoseTimes(doseTimes: List<DoseTime>): String =
        json.encodeToString(doseTimes)

    @TypeConverter
    fun toDoseTimes(value: String): List<DoseTime> =
        json.decodeFromString(value)
}