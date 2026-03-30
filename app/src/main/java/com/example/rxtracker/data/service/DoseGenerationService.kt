package com.example.rxtracker.data.service

import com.example.rxtracker.data.models.DoseStatus
import com.example.rxtracker.data.models.FrequencyDetails
import com.example.rxtracker.data.models.ScheduledDose
import com.example.rxtracker.data.models.UserMedication
import com.example.rxtracker.utils.minusDays
import com.example.rxtracker.utils.plusDays
import com.example.rxtracker.utils.today
import kotlinx.datetime.LocalDate
import kotlinx.datetime.daysUntil

object DoseGenerationService {
    private const val ROLLING_WINDOW_DAYS = 90
    private const val HISTORY_CAP_DAYS = 90

    fun generate(
        medication: UserMedication,
        fromDate: LocalDate = medication.startDate
    ): List<ScheduledDose> {
        if (medication.frequencyDetails is FrequencyDetails.AsNeeded) return emptyList()

        val today = today()
        val effectiveStartDate = maxOf(fromDate, today.minusDays(HISTORY_CAP_DAYS))
        val windowEnd = minOf(
            today.plusDays(ROLLING_WINDOW_DAYS),
            medication.endDate ?: today.plusDays(ROLLING_WINDOW_DAYS)
        )

        val doses = mutableListOf<ScheduledDose>()
        var current = effectiveStartDate

        while (current <= windowEnd) {
            if (shouldDoseOnDate(medication, current)) {
                doses.addAll(generateDosesForDay(medication, current))
            }
            current = current.plusDays(1)
        }
        return doses
    }

    private fun shouldDoseOnDate(medication: UserMedication, date: LocalDate): Boolean {
        val daysSinceStart = medication.startDate.daysUntil(date).toLong()
        if (daysSinceStart < 0) return false

        return when (val details = medication.frequencyDetails) {
            is FrequencyDetails.OnceDaily -> true
            is FrequencyDetails.MultipleTimes -> true
            is FrequencyDetails.EveryXHours -> true
            is FrequencyDetails.EveryXDays -> daysSinceStart % details.days == 0L
            is FrequencyDetails.SpecificWeekdays -> date.dayOfWeek in details.days
            is FrequencyDetails.Cyclic -> {
                val cycleLength = (details.intakeDays + details.pauseDays.toLong())
                daysSinceStart % cycleLength < details.intakeDays
            }

            is FrequencyDetails.AsNeeded -> false
        }
    }

    private fun generateDosesForDay(
        medication: UserMedication,
        date: LocalDate
    ): List<ScheduledDose> {
        return medication.doseTimes.map { doseTime ->
            ScheduledDose(
                medicationId = medication.id,
                scheduledDate = date,
                scheduledTime = doseTime.time,
                quantity = doseTime.quantity,
                status = DoseStatus.PENDING
            )
        }
    }
}