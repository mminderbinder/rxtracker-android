package com.example.rxtracker.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.rxtracker.data.models.DoseStatus
import com.example.rxtracker.data.models.ScheduledDose
import com.example.rxtracker.data.models.ScheduledDoseWithMedication
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Dao
interface ScheduledDoseDao {
    @Insert
    suspend fun insertAll(doses: List<ScheduledDose>): List<Long>

    @Query(
        "SELECT sd.*, um.name, um.strength, um.form " +
                "FROM scheduled_doses sd " +
                "INNER JOIN medications um " +
                "ON sd.medicationId = um.id " +
                "WHERE sd.scheduledDate = :date " +
                "ORDER BY sd.scheduledTime ASC"
    )
    fun getDosesForDate(date: LocalDate): Flow<List<ScheduledDoseWithMedication>>

    @Query(
        "SELECT * FROM scheduled_doses " +
                "WHERE medicationId = :medicationId " +
                "AND scheduledDate >= :fromDate " +
                "ORDER BY scheduledDate ASC"
    )
    suspend fun getFutureDoses(medicationId: Long, fromDate: LocalDate): List<ScheduledDose>

    @Query(
        "UPDATE scheduled_doses " +
                "SET status = :status, takenAt = :takenAt " +
                "WHERE id = :id"
    )
    suspend fun updateStatus(id: Long, status: DoseStatus, takenAt: LocalDateTime?)

    @Query(
        "UPDATE scheduled_doses " +
                "SET status = 'NOT_LOGGED' " +
                "WHERE status = 'PENDING' " +
                "AND scheduledDate < :today"
    )
    suspend fun markPastPendingAsNotLogged(today: LocalDate)

    @Query(
        "" +
                "UPDATE scheduled_doses " +
                "SET status = :status, takenAt = :takenAt " +
                "WHERE id " +
                "IN (:ids)"
    )
    suspend fun updateStatusBatch(ids: List<Long>, status: DoseStatus, takenAt: LocalDateTime?)

    @Query(
        "UPDATE scheduled_doses " +
                "SET quantity = :quantity " +
                "WHERE id = :id"
    )
    suspend fun updateQuantity(id: Long, quantity: Double)

    @Query(
        "UPDATE scheduled_doses " +
                "SET scheduledTime = :newTime " +
                "WHERE id = :id"
    )
    suspend fun updateScheduledTime(id: Long, newTime: LocalTime)

    @Query(
        "UPDATE scheduled_doses " +
                "SET doseNotes = :notes " +
                "WHERE id = :id"
    )
    suspend fun updateDoseNotes(id: Long, notes: String?)


    @Query(
        "DELETE from scheduled_doses " +
                "WHERE medicationId = :medicationId " +
                "AND scheduledDate >= :fromDate"
    )
    suspend fun deleteFutureDoses(medicationId: Long, fromDate: LocalDate)

    @Insert
    suspend fun insert(dose: ScheduledDose): Long
}