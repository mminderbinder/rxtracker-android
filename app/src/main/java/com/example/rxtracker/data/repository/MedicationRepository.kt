package com.example.rxtracker.data.repository

import androidx.room.Transaction
import com.example.rxtracker.data.database.MedicationDao
import com.example.rxtracker.data.database.ScheduledDoseDao
import com.example.rxtracker.data.models.UserMedication
import com.example.rxtracker.data.service.DoseGenerationService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MedicationRepository @Inject constructor(
    private val medicationDao: MedicationDao,
    private val scheduledDoseDao: ScheduledDoseDao
) {
    fun getAll() : Flow<List<UserMedication>> = medicationDao.getAll()

    suspend fun getById(id: Long) : UserMedication? = medicationDao.getById(id)

    suspend fun delete(medication: UserMedication) = medicationDao.delete(medication)

    @Transaction
    suspend fun insertWithDoses(medication: UserMedication) {
        val id = medicationDao.insert(medication)
        val savedMedication = medication.copy(id = id)
        val doses = DoseGenerationService.generate(savedMedication)
        if (doses.isNotEmpty()) {
            scheduledDoseDao.insertAll(doses)
        }
    }
}