package com.example.rxtracker.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.rxtracker.R
import com.example.rxtracker.data.models.DoseStatus
import com.example.rxtracker.data.models.ScheduledDoseWithMedication
import com.example.rxtracker.ui.home.components.CalendarDay
import com.example.rxtracker.ui.shared.DoseCard
import com.example.rxtracker.ui.home.components.DoseTimeHeader
import com.example.rxtracker.ui.home.components.ResolvedAccordionHeader
import com.example.rxtracker.ui.home.components.ResolvedDoseBatchBottomSheet
import com.example.rxtracker.ui.home.components.ResolvedDoseBottomSheet
import com.example.rxtracker.ui.home.components.ResolvedDoseRow
import com.example.rxtracker.ui.home.components.UnresolvedDoseBatchBottomSheet
import com.example.rxtracker.ui.home.components.UnresolvedDoseBottomSheet
import com.example.rxtracker.utils.getFormattedTime
import com.example.rxtracker.utils.getWeekPageTitle
import com.example.rxtracker.utils.rememberFirstVisibleWeekAfterScroll
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalTime
import kotlinx.datetime.atDate
import kotlinx.datetime.toKotlinLocalDate
import java.time.LocalDate

@Composable
fun HomeScreen(
    viewModel: HomeViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    val currentDate = remember { LocalDate.now() }
    val startDate = remember { currentDate.minusDays(500) }
    val endDate = remember { currentDate.plusDays(500) }
    val coroutineScope = rememberCoroutineScope()

    val state = rememberWeekCalendarState(
        startDate = startDate,
        endDate = endDate,
        firstVisibleWeekDate = currentDate
    )

    val visibleWeek = rememberFirstVisibleWeekAfterScroll(state)

    LaunchedEffect(visibleWeek) {
        val weekContainsToday = visibleWeek.days.any { it.date == currentDate }
        viewModel.selectDate(
            if (weekContainsToday) currentDate
            else visibleWeek.days.first().date
        )
    }

    val activeDoses: Map<LocalTime, List<ScheduledDoseWithMedication>> =
        uiState.doses
            .filter { it.status !in listOf(DoseStatus.TAKEN, DoseStatus.SKIPPED) }
            .groupBy { it.scheduledTime }

    val resolvedDoses: List<ScheduledDoseWithMedication> = uiState.doses
        .filter { it.status in listOf(DoseStatus.TAKEN, DoseStatus.SKIPPED) }
        .sortedBy { it.resolvedAt }

    val showTodayButton = uiState.selectedDate != currentDate ||
            !visibleWeek.days.any { it.date == currentDate }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = getWeekPageTitle(visibleWeek),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        WeekCalendar(
            state = state,
            dayContent = { day ->
                CalendarDay(
                    date = day.date,
                    isSelected = uiState.selectedDate == day.date,
                    onClick = { date ->
                        if (uiState.selectedDate != date) {
                            viewModel.selectDate(date)
                        }
                    }
                )
            },
            modifier = Modifier.padding(bottom = 12.dp)
        )

        AnimatedVisibility(visible = showTodayButton) {
            TextButton(
                onClick = {
                    viewModel.selectDate(currentDate)
                    coroutineScope.launch {
                        state.animateScrollToWeek(currentDate)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.width(4.dp))
                Text("Return to today")
            }
        }

        if (uiState.doses.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(R.drawable.schedule),
                        contentDescription = null,
                        modifier = Modifier.size(80.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "No medications scheduled",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            val resolvedExpanded = remember { mutableStateOf(false) }

            val resolvedGrouped = remember(resolvedDoses) {
                resolvedDoses
                    .sortedBy { it.resolvedAt }
                    .groupBy { it.resolvedAt?.time ?: it.scheduledTime }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                activeDoses.forEach { (time, dosesAtTime) ->
                    item {
                        DoseTimeHeader(
                            title = getFormattedTime(time),
                            onSelectAll = if (dosesAtTime.size > 1) {
                                { viewModel.selectBatchDoses(dosesAtTime) }
                            } else null
                        )
                    }
                    items(dosesAtTime) { dose ->
                        DoseCard(
                            dose = dose,
                            selectedDate = uiState.selectedDate.toKotlinLocalDate(),
                            onTap = { viewModel.selectDose(dose) }
                        )
                    }
                }

                if (resolvedDoses.isNotEmpty()) {
                    item {
                        ResolvedAccordionHeader(
                            count = resolvedDoses.size,
                            expanded = resolvedExpanded.value,
                            onToggle = { resolvedExpanded.value = !resolvedExpanded.value }
                        )
                    }

                    if (resolvedExpanded.value) {
                        resolvedGrouped.forEach { (resolvedTime, dosesAtTime) ->
                            item {
                                DoseTimeHeader(
                                    title = getFormattedTime(resolvedTime),
                                    onSelectAll = if (dosesAtTime.size > 1) {
                                        { viewModel.selectBatchDoses(dosesAtTime) }
                                    } else null
                                )
                            }
                            items(dosesAtTime) { dose ->
                                ResolvedDoseRow(
                                    dose = dose,
                                    onTap = { viewModel.selectDose(dose) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    uiState.selectedDose?.let { dose ->
        when {
            dose.status in listOf(DoseStatus.TAKEN, DoseStatus.SKIPPED) -> {
                ResolvedDoseBottomSheet(
                    dose = dose,
                    selectedDate = uiState.selectedDate.toKotlinLocalDate(),
                    onDismiss = { viewModel.selectDose(null) },
                    onTakeAtTime = { takenAt -> viewModel.takeAtTime(dose, takenAt) },
                    onUndoTake = { viewModel.undoDoseTaken(dose) },
                    onSkip = { viewModel.skipDose(dose) },
                    onUndoSkip = { viewModel.unskipDose(dose) },
                    onReschedule = { newDateTime -> viewModel.rescheduleDose(dose, newDateTime) },
                    onQuantityChange = { qty -> viewModel.updateQuantity(dose, qty) },
                    onNotesChange = { notes -> viewModel.updateDoseNotes(dose, notes) }
                )
            }

            uiState.selectedDate > LocalDate.now() -> {
                // TODO: FutureDoseDialog
            }

            else -> {
                UnresolvedDoseBottomSheet(
                    dose = dose,
                    selectedDate = uiState.selectedDate.toKotlinLocalDate(),
                    onDismiss = { viewModel.selectDose(null) },
                    onTakeOnTime = {
                        viewModel.takeAtTime(
                            dose,
                            dose.scheduledTime.atDate(uiState.selectedDate.toKotlinLocalDate())
                        )
                    },
                    onTakeNow = { viewModel.takeNow(dose) },
                    onTakeAtTime = { takenAt -> viewModel.takeAtTime(dose, takenAt) },
                    onSkip = { viewModel.skipDose(dose) },
                    onReschedule = { newTime -> viewModel.rescheduleDose(dose, newTime) },
                    onQuantityChange = { qty -> viewModel.updateQuantity(dose, qty) },
                    onNotesChange = { notes -> viewModel.updateDoseNotes(dose, notes) }
                )
            }
        }
    }

    uiState.selectedBatchDoses?.let { doses ->
        when {
            doses.first().status in listOf(DoseStatus.TAKEN, DoseStatus.SKIPPED) -> {
                ResolvedDoseBatchBottomSheet(
                    doses = doses,
                    selectedDate = uiState.selectedDate.toKotlinLocalDate(),
                    onDismiss = { viewModel.selectBatchDoses(null) },
                    onTakeAllAtTime = { takenAt -> viewModel.takeAllAtTime(doses, takenAt) },
                    onUndoTakeAll = { viewModel.undoAllDosesTaken(doses) },
                    onSkipAll = { viewModel.skipAllDoses(doses) },
                    onUndoSkipAll = { viewModel.unskipAllDoses(doses) },
                    onRescheduleAll = { newTime -> viewModel.rescheduleAllDoses(doses, newTime) }
                )
            }

            else -> {
                UnresolvedDoseBatchBottomSheet(
                    doses = doses,
                    selectedDate = uiState.selectedDate.toKotlinLocalDate(),
                    onDismiss = { viewModel.selectBatchDoses(null) },
                    onTakeAllOnTime = {
                        viewModel.takeAllAtTime(
                            doses,
                            doses.first().scheduledTime.atDate(uiState.selectedDate.toKotlinLocalDate())
                        )
                    },
                    onTakeAllNow = { viewModel.takeAllNow(doses) },
                    onTakeAllAtTime = { takenAt -> viewModel.takeAllAtTime(doses, takenAt) },
                    onSkipAll = { viewModel.skipAllDoses(doses) },
                    onRescheduleAll = { newDateTime ->
                        viewModel.rescheduleAllDoses(
                            doses = doses,
                            newTime = newDateTime
                        )
                    }
                )
            }
        }
    }
}