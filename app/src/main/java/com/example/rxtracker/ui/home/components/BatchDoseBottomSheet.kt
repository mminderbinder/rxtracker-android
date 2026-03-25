package com.example.rxtracker.ui.home.components

import androidx.compose.runtime.Composable
import com.example.rxtracker.data.models.ScheduledDoseWithMedication
import java.time.LocalDate
import java.time.LocalDateTime

@Composable
fun BatchDoseBottomSheet(
    doses: List<ScheduledDoseWithMedication>,
    selectedDate: LocalDate,
    onDismiss: () -> Unit,
    onTakeAllOnTime: () -> Unit,
    onTakeAllNow: () -> Unit,
    onTakeAllAtTime: (LocalDateTime) -> Unit,
    onUndoTakeAll: () -> Unit,
    onSkipAll: () -> Unit,
    onUnskipAll: () -> Unit,
    onRescheduleAll: (LocalDateTime) -> Unit
) {
    val today = LocalDate.now()
    val isToday = selectedDate == today
    val isPastDate = selectedDate.isBefore(today)
    val isFutureDate = selectedDate.isAfter(today)
}