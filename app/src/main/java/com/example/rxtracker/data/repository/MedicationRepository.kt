package com.example.rxtracker.data.repository

import com.example.rxtracker.data.database.MedicationDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MedicationRepository @Inject constructor(
    private val medicationDao: MedicationDao
) {
    
}