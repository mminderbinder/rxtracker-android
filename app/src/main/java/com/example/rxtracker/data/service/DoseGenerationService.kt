package com.example.rxtracker.data.service

import com.example.rxtracker.data.models.DoseStatus
import com.example.rxtracker.data.models.FrequencyDetails
import com.example.rxtracker.data.models.ScheduledDose
import com.example.rxtracker.data.models.UserMedication
import com.example.rxtracker.utils.plusDays
import kotlinx.datetime.LocalDate
import kotlinx.datetime.daysUntil

object DoseGenerationService {
    private const val ROLLING_WINDOW_DAYS = 60

    fun generate(
        medication: UserMedication,
        fromDate: LocalDate = medication.startDate,
        toDate: LocalDate = fromDate.plusDays(ROLLING_WINDOW_DAYS)
    ): List<ScheduledDose> {
        if (medication.frequencyDetails is FrequencyDetails.AsNeeded) return emptyList()

        val windowEnd = if (medication.endDate != null && medication.endDate < toDate) {
            medication.endDate
        } else {
            toDate
        }

        val doses = mutableListOf<ScheduledDose>()
        var current = fromDate

        while (current <= windowEnd) {
            if (shouldDoseOnDate(medication, current)) {
                val dosesForDay = generateDosesForDay(medication, current)
                doses.addAll(dosesForDay)
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