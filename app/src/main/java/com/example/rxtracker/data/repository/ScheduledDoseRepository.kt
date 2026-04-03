package com.example.rxtracker.data.repository

import com.example.rxtracker.data.database.ScheduledDoseDao
import com.example.rxtracker.data.models.DoseStatus
import com.example.rxtracker.data.models.ScheduledDose
import com.example.rxtracker.data.models.ScheduledDoseWithMedication
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScheduledDoseRepository @Inject constructor(
    private val scheduledDoseDao: ScheduledDoseDao
) {
    fun getDosesForDate(date: LocalDate): Flow<List<ScheduledDoseWithMedication>> =
        scheduledDoseDao.getDosesForDate(date)

    suspend fun updateStatus(id: Long, status: DoseStatus, resolvedAt: LocalDateTime?) =
        scheduledDoseDao.updateStatus(id, status, resolvedAt)

    suspend fun updateRescheduledTime(id: Long, newTime: LocalTime) =
        scheduledDoseDao.updateRescheduledDateAndTime(id, newTime, DoseStatus.RESCHEDULED)

    suspend fun updateQuantity(id: Long, quantity: Double) =
        scheduledDoseDao.updateQuantity(id, quantity)

    suspend fun updateDoseNotes(id: Long, doseNotes: String?) =
        scheduledDoseDao.updateDoseNotes(id, doseNotes)

    suspend fun updateStatusBatch(ids: List<Long>, status: DoseStatus, resolvedAt: LocalDateTime?) =
        scheduledDoseDao.updateStatusBatch(ids, status, resolvedAt)

    suspend fun updateScheduledTimeBatch(ids: List<Long>, newTime: LocalTime) =
        scheduledDoseDao.updateScheduledTimeBatch(ids, newTime)

    suspend fun markPastPendingAsNotLogged(date: LocalDate) =
        scheduledDoseDao.markPastPendingAsNotLogged(date)

    suspend fun deleteFutureDoses(medicationId: Long, fromDate: LocalDate) =
        scheduledDoseDao.deleteFutureDoses(medicationId, fromDate)

    suspend fun insertAll(doses: List<ScheduledDose>) = scheduledDoseDao.insertAll(doses)
}