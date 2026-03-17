package com.example.rxtracker.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rxtracker.data.models.DoseStatus
import com.example.rxtracker.data.models.ScheduledDoseWithMedication
import com.example.rxtracker.data.repository.ScheduledDoseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val scheduledDoseRepository: ScheduledDoseRepository
) : ViewModel() {
    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val dosesForDate: StateFlow<List<ScheduledDoseWithMedication>> =
        _selectedDate
            .flatMapLatest { date ->
                scheduledDoseRepository.getDosesForDate(date)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
    }

    fun markTaken(dose: ScheduledDoseWithMedication) {
        viewModelScope.launch {
            scheduledDoseRepository.updateStatus(
                id = dose.id,
                status = DoseStatus.TAKEN,
                takenAt = LocalDateTime.now()
            )
        }
    }

    fun unmarkAsTaken(dose: ScheduledDoseWithMedication) {
        viewModelScope.launch {
            scheduledDoseRepository.updateStatus(
                id = dose.id,
                status = DoseStatus.PENDING,
                takenAt = null
            )
        }
    }

    fun skipDose(dose: ScheduledDoseWithMedication) {
        viewModelScope.launch {
            scheduledDoseRepository.updateStatus(
                id = dose.id,
                status = DoseStatus.SKIPPED,
                takenAt = null
            )
        }
    }

    fun unskipDose(dose: ScheduledDoseWithMedication) {
        viewModelScope.launch {
            scheduledDoseRepository.updateStatus(
                id = dose.id,
                status = DoseStatus.PENDING,
                takenAt = null
            )
        }
    }
}