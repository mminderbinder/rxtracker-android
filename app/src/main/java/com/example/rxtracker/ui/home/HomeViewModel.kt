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
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val scheduledDoseRepository: ScheduledDoseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    @OptIn(ExperimentalCoroutinesApi::class)
    private val dosesForDate = _uiState
        .map { it.selectedDate }
        .flatMapLatest { date -> scheduledDoseRepository.getDosesForDate(date) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        viewModelScope.launch {
            scheduledDoseRepository.markPastPendingAsNotLogged(LocalDate.now())
            dosesForDate.collect { doses ->
                val resolved = resolveLateStatuses(doses, _uiState.value.selectedDate)
                _uiState.update { state ->
                    state.copy(
                        doses = resolved,
                        selectedDose = state.selectedDose?.let { selected ->
                            resolved.find { it.id == selected.id }
                        }
                    )
                }
            }
        }
    }

    fun selectDate(date: LocalDate) {
        _uiState.update { it.copy(selectedDate = date) }
    }

    fun selectDose(dose: ScheduledDoseWithMedication?) {
        _uiState.update { it.copy(selectedDose = dose) }
    }

    fun selectBatchDoses(doses: List<ScheduledDoseWithMedication>) {
        _uiState.update { it.copy(selectedBatchDoses = doses) }
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
        val isToday = _uiState.value.selectedDate == LocalDate.now()
        viewModelScope.launch {
            scheduledDoseRepository.updateStatus(
                id = dose.id,
                status = if (isToday) DoseStatus.PENDING else DoseStatus.NOT_LOGGED,
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
        val isToday = _uiState.value.selectedDate == LocalDate.now()
        viewModelScope.launch {
            scheduledDoseRepository.updateStatus(
                id = dose.id,
                status = if (isToday) DoseStatus.PENDING else DoseStatus.NOT_LOGGED,
                takenAt = null
            )
        }
    }

    fun takeAtTime(dose: ScheduledDoseWithMedication, takenAt: LocalDateTime) {
        viewModelScope.launch {
            scheduledDoseRepository.updateStatus(
                id = dose.id,
                status = DoseStatus.TAKEN,
                takenAt = takenAt
            )
        }
    }

    fun rescheduleDose(dose: ScheduledDoseWithMedication, newTime: LocalTime) {
        viewModelScope.launch {
            scheduledDoseRepository.updateScheduledTime(dose.id, newTime)
        }
    }

    fun updateQuantity(dose: ScheduledDoseWithMedication, quantity: Double) {
        viewModelScope.launch {
            scheduledDoseRepository.updateQuantity(dose.id, quantity)
        }
    }

    fun updateDoseNotes(dose: ScheduledDoseWithMedication, doseNotes: String?) {
        viewModelScope.launch {
            scheduledDoseRepository.updateDoseNotes(dose.id, doseNotes)
        }
    }

    private fun resolveLateStatuses(
        doses: List<ScheduledDoseWithMedication>,
        selectedDate: LocalDate
    ): List<ScheduledDoseWithMedication> {
        if (selectedDate != LocalDate.now()) return doses
        val now = LocalTime.now()
        return doses.map { dose ->
            if (dose.status == DoseStatus.PENDING && dose.scheduledTime.isBefore(now)) {
                dose.copy(status = DoseStatus.LATE)
            } else {
                dose
            }
        }
    }
}