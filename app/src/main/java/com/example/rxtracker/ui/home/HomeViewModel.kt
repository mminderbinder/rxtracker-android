package com.example.rxtracker.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rxtracker.data.models.DoseStatus
import com.example.rxtracker.data.models.ScheduledDoseWithMedication
import com.example.rxtracker.data.repository.ScheduledDoseRepository
import com.example.rxtracker.utils.currentTime
import com.example.rxtracker.utils.now
import com.example.rxtracker.utils.today
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
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.toKotlinLocalDate
import javax.inject.Inject
import java.time.LocalDate as LocalDateJava

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val scheduledDoseRepository: ScheduledDoseRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState
    val isToday = _uiState.value.selectedDate == LocalDateJava.now()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val dosesForDate = _uiState
        .map { it.selectedDate }
        .flatMapLatest { date -> scheduledDoseRepository.getDosesForDate(date.toKotlinLocalDate()) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        viewModelScope.launch {
            scheduledDoseRepository.markPastPendingAsNotLogged(today())
            dosesForDate.collect { doses ->
                val resolved =
                    resolveLateStatuses(doses, _uiState.value.selectedDate.toKotlinLocalDate())
                _uiState.update { state ->
                    state.copy(doses = resolved)
                }
            }
        }
    }

    fun selectDate(date: LocalDateJava) {
        _uiState.update { it.copy(selectedDate = date) }
    }

    fun selectDose(dose: ScheduledDoseWithMedication?) {
        _uiState.update { it.copy(selectedDose = dose) }
    }

    fun selectBatchDoses(doses: List<ScheduledDoseWithMedication>?) {
        Log.d("HomeViewModel", "selectBatchDoses: ${doses?.size}")
        _uiState.update { it.copy(selectedBatchDoses = doses) }
    }

    fun takeNow(dose: ScheduledDoseWithMedication) {
        viewModelScope.launch {
            scheduledDoseRepository.updateStatus(
                id = dose.id,
                status = DoseStatus.TAKEN,
                resolvedAt = now()
            )
        }
    }

    fun takeAtTime(dose: ScheduledDoseWithMedication, takenAt: LocalDateTime) {
        viewModelScope.launch {
            scheduledDoseRepository.updateStatus(
                id = dose.id,
                status = DoseStatus.TAKEN,
                resolvedAt = takenAt
            )
        }
    }

    fun undoDoseTaken(dose: ScheduledDoseWithMedication) {
        val isToday = _uiState.value.selectedDate == LocalDateJava.now()
        viewModelScope.launch {
            scheduledDoseRepository.updateStatus(
                id = dose.id,
                status = if (isToday) DoseStatus.PENDING else DoseStatus.NOT_LOGGED,
                resolvedAt = null
            )
        }
    }

    fun skipDose(dose: ScheduledDoseWithMedication) {
        viewModelScope.launch {
            scheduledDoseRepository.updateStatus(
                id = dose.id,
                status = DoseStatus.SKIPPED,
                resolvedAt = now()
            )
        }
    }

    fun unskipDose(dose: ScheduledDoseWithMedication) {
        viewModelScope.launch {
            scheduledDoseRepository.updateStatus(
                id = dose.id,
                status = if (isToday) DoseStatus.PENDING else DoseStatus.NOT_LOGGED,
                resolvedAt = null
            )
        }
    }

    fun rescheduleDose(dose: ScheduledDoseWithMedication, newTime: LocalTime) {
        viewModelScope.launch {
            scheduledDoseRepository.updateRescheduledTime(
                dose.id,
                newTime
            )
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

    fun takeAllNow(doses: List<ScheduledDoseWithMedication>) {
        viewModelScope.launch {
            scheduledDoseRepository.updateStatusBatch(
                ids = doses.map { it.id },
                status = DoseStatus.TAKEN,
                resolvedAt = now()
            )
        }
    }

    fun takeAllAtTime(doses: List<ScheduledDoseWithMedication>, takenAt: LocalDateTime) {
        viewModelScope.launch {
            scheduledDoseRepository.updateStatusBatch(
                ids = doses.map { it.id },
                status = DoseStatus.TAKEN,
                resolvedAt = takenAt
            )
        }
    }

    fun undoAllDosesTaken(doses: List<ScheduledDoseWithMedication>) {
        viewModelScope.launch {
            scheduledDoseRepository.updateStatusBatch(
                ids = doses.map { it.id },
                status = if (isToday) DoseStatus.PENDING else DoseStatus.NOT_LOGGED,
                resolvedAt = null
            )
        }
    }


    fun skipAllDoses(doses: List<ScheduledDoseWithMedication>) {
        viewModelScope.launch {
            scheduledDoseRepository.updateStatusBatch(
                ids = doses.map { it.id },
                status = DoseStatus.SKIPPED,
                resolvedAt = now()
            )
        }
    }

    fun unskipAllDoses(doses: List<ScheduledDoseWithMedication>) {
        viewModelScope.launch {
            scheduledDoseRepository.updateStatusBatch(
                ids = doses.map { it.id },
                status = if (isToday) DoseStatus.PENDING else DoseStatus.NOT_LOGGED,
                resolvedAt = null
            )
        }
    }

    fun rescheduleAllDoses(doses: List<ScheduledDoseWithMedication>, newTime: LocalTime) {
        viewModelScope.launch {
            scheduledDoseRepository.updateScheduledTimeBatch(
                ids = doses.map { it.id },
                newTime = newTime
            )
        }
    }

    private fun resolveLateStatuses(
        doses: List<ScheduledDoseWithMedication>,
        selectedDate: LocalDate
    ): List<ScheduledDoseWithMedication> {
        if (selectedDate != today()) return doses
        val now = currentTime()
        return doses.map { dose ->
            if (dose.status == DoseStatus.PENDING && dose.scheduledTime < now) {
                dose.copy(status = DoseStatus.LATE)
            } else {
                dose
            }
        }
    }
}