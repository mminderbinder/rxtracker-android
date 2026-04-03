package com.example.rxtracker.ui.home.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composables.core.SheetDetent
import com.composables.core.rememberModalBottomSheetState
import com.composables.icons.lucide.AlarmClock
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Pencil
import com.composables.icons.lucide.RotateCcw
import com.composables.icons.lucide.X
import com.example.rxtracker.data.models.DoseStatus
import com.example.rxtracker.data.models.ScheduledDoseWithMedication
import com.example.rxtracker.ui.shared.TimeSelectionDialog
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

@Composable
fun ResolvedDoseBatchBottomSheet(
    doses: List<ScheduledDoseWithMedication>,
    selectedDate: LocalDate,
    onDismiss: () -> Unit,
    onTakeAllAtTime: (LocalDateTime) -> Unit,
    onUndoTakeAll: () -> Unit,
    onSkipAll: () -> Unit,
    onUndoSkipAll: () -> Unit,
    onRescheduleAll: (LocalTime) -> Unit
) {
    val state = rememberModalBottomSheetState(initialDetent = SheetDetent.FullyExpanded)

    var showRescheduleDialog by remember { mutableStateOf(false) }
    var showTakeAtTimeDialog by remember { mutableStateOf(false) }

    val isTaken = doses.first().status == DoseStatus.TAKEN
    val isSkipped = doses.first().status == DoseStatus.SKIPPED

    BaseBottomSheet(state = state, onDismiss = onDismiss) { sheetState ->

        BatchDoseSheetHeader(doses = doses)

        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
        Spacer(modifier = Modifier.height(4.dp))

        when {
            isTaken -> {
                SheetActionRow(
                    icon = Lucide.RotateCcw,
                    label = "Undo take all",
                    onClick = {
                        onUndoTakeAll()
                        sheetState.targetDetent = SheetDetent.Hidden
                    }
                )
                SheetActionRow(
                    icon = Lucide.X,
                    label = "Skip all",
                    onClick = {
                        onSkipAll()
                        sheetState.targetDetent = SheetDetent.Hidden
                    }
                )
                SheetActionRow(
                    icon = Lucide.AlarmClock,
                    label = "Reschedule all",
                    onClick = { showRescheduleDialog = true }
                )
            }
            isSkipped -> {
                SheetActionRow(
                    icon = Lucide.Pencil,
                    label = "Set time",
                    onClick = { showTakeAtTimeDialog = true }
                )
                SheetActionRow(
                    icon = Lucide.RotateCcw,
                    label = "Unskip",
                    onClick = {
                        onUndoSkipAll()
                        sheetState.targetDetent = SheetDetent.Hidden
                    }
                )
                SheetActionRow(
                    icon = Lucide.AlarmClock,
                    label = "Reschedule",
                    onClick = { showRescheduleDialog = true }
                )
            }
            else -> return@BaseBottomSheet
        }
    }
    if (showTakeAtTimeDialog) {
        TimeSelectionDialog(
            startTime = doses.first().scheduledTime,
            onConfirm = { hour, minute ->
                onTakeAllAtTime(LocalDateTime(selectedDate, LocalTime(hour, minute)))
                showTakeAtTimeDialog = false
                state.targetDetent = SheetDetent.Hidden
            },
            onDismiss = { showTakeAtTimeDialog = false }
        )
    }

    if (showRescheduleDialog) {
        TimeSelectionDialog(
            startTime = doses.first().scheduledTime,
            onConfirm = { hour, minute ->
                onRescheduleAll(LocalTime(hour, minute))
                showRescheduleDialog = false
                state.targetDetent = SheetDetent.Hidden
            },
            onDismiss = { showRescheduleDialog = false }
        )
    }
}