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
import com.composables.icons.lucide.Check
import com.composables.icons.lucide.Clock
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Pencil
import com.composables.icons.lucide.X
import com.example.rxtracker.data.models.ScheduledDoseWithMedication
import com.example.rxtracker.ui.shared.TimeSelectionDialog
import com.example.rxtracker.utils.getFormattedTime
import com.example.rxtracker.utils.now
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

@Composable
fun UnresolvedDoseBatchBottomSheet(
    doses: List<ScheduledDoseWithMedication>,
    selectedDate: LocalDate,
    onDismiss: () -> Unit,
    onTakeAllOnTime: () -> Unit,
    onTakeAllNow: () -> Unit,
    onTakeAllAtTime: (LocalDateTime) -> Unit,
    onSkipAll: () -> Unit,
    onRescheduleAll: (LocalTime) -> Unit
) {
    val now = now()
    val scheduleTime =
        LocalDateTime(date = doses.first().scheduledDate, time = doses.first().scheduledTime)

    val state = rememberModalBottomSheetState(initialDetent = SheetDetent.FullyExpanded)

    var showRescheduleDialog by remember { mutableStateOf(false) }
    var showTakeAtTimeDialog by remember { mutableStateOf(false) }

    BaseBottomSheet(state = state, onDismiss = onDismiss) { sheetState ->

        BatchDoseSheetHeader(doses = doses)

        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
        Spacer(modifier = Modifier.height(4.dp))


        SheetActionRow(
            icon = Lucide.Clock,
            label = "Take all now",
            onClick = {
                onTakeAllNow()
                sheetState.targetDetent = SheetDetent.Hidden
            }
        )

        if (now > scheduleTime) {
            SheetActionRow(
                icon = Lucide.Check,
                label = "Take all on time",
                sublabel = getFormattedTime(doses.first().scheduledTime),
                onClick = {
                    onTakeAllOnTime()
                    sheetState.targetDetent = SheetDetent.Hidden
                }
            )
        }

        SheetActionRow(
            icon = Lucide.Pencil,
            label = "Set time taken",
            onClick = { showTakeAtTimeDialog = true }
        )

        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

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