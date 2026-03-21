package com.example.rxtracker.data.repository

import com.example.rxtracker.data.database.ScheduledDoseDao
import com.example.rxtracker.data.models.DoseStatus
import com.example.rxtracker.data.models.ScheduledDose
import com.example.rxtracker.data.models.ScheduledDoseWithMedication
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScheduledDoseRepository @Inject constructor(
    private val scheduledDoseDao: ScheduledDoseDao
) {
    fun getDosesForDate(date: LocalDate): Flow<List<ScheduledDoseWithMedication>> =
        scheduledDoseDao.getDosesForDate(date)

    suspend fun updateStatus(id: Long, status: DoseStatus, takenAt: LocalDateTime?) =
        scheduledDoseDao.updateStatus(id, status, takenAt)

    suspend fun updateScheduledTime(id: Long, newTime: LocalTime) =
        scheduledDoseDao.updateScheduledTime(id, newTime)

    suspend fun updateQuantity(id: Long, quantity: Double) =
        scheduledDoseDao.updateQuantity(id, quantity)

    suspend fun updateDoseNotes(id: Long, doseNotes: String?) =
        scheduledDoseDao.updateDoseNotes(id, doseNotes)

    suspend fun markPastPendingAsNotLogged(date: LocalDate) =
        scheduledDoseDao.markPastPendingAsNotLogged(date)

    suspend fun deleteFutureDoses(medicationId: Long, fromDate: LocalDate) =
        scheduledDoseDao.deleteFutureDoses(medicationId, fromDate)

    suspend fun insertAll(doses: List<ScheduledDose>) = scheduledDoseDao.insertAll(doses)
}