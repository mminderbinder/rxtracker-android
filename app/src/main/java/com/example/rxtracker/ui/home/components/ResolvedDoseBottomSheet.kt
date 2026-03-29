package com.example.rxtracker.ui.home.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
import com.example.rxtracker.ui.shared.DateTimeWheelPicker
import com.example.rxtracker.ui.shared.NotesDialog
import com.example.rxtracker.ui.shared.QuantityDialog
import com.example.rxtracker.ui.theme.RXTrackerTheme
import com.example.rxtracker.utils.now
import com.example.rxtracker.utils.today
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

@Composable
fun ResolvedDoseBottomSheet(
    dose: ScheduledDoseWithMedication,
    onDismiss: () -> Unit,
    onTakeAtTime: (LocalDateTime) -> Unit,
    onUndoTake: () -> Unit,
    onSkip: () -> Unit,
    onUndoSkip: () -> Unit,
    onReschedule: (LocalDateTime) -> Unit,
    onQuantityChange: (Double) -> Unit,
    onNotesChange: (String?) -> Unit
) {
    val now = now()
    val state = rememberModalBottomSheetState(initialDetent = SheetDetent.FullyExpanded)

    var showQuantityDialog by remember { mutableStateOf(false) }
    var showRescheduleDialog by remember { mutableStateOf(false) }
    var showTakeAtTimeDialog by remember { mutableStateOf(false) }
    var showNotesDialog by remember { mutableStateOf(false) }
    var notes by remember { mutableStateOf(dose.doseNotes) }

    val isTaken = dose.status == DoseStatus.TAKEN
    val isSkipped = dose.status == DoseStatus.SKIPPED

    LaunchedEffect(dose.doseNotes) {
        notes = dose.doseNotes
    }

    BaseBottomSheet(state = state, onDismiss = onDismiss) { sheetState ->

        SingleDoseSheetHeader(
            dose = dose,
            onQuantityTap = { showQuantityDialog = true },
            onEditTap = { showNotesDialog = true }
        )

        if (!notes.isNullOrBlank()) {
            Text(
                text = notes!!,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
        Spacer(modifier = Modifier.height(4.dp))

        when {
            isTaken -> {
                SheetActionRow(
                    icon = Lucide.RotateCcw,
                    label = "Undo take",
                    onClick = {
                        onUndoTake()
                        sheetState.targetDetent = SheetDetent.Hidden
                    }
                )
                SheetActionRow(
                    icon = Lucide.X,
                    label = "Skip",
                    onClick = {
                        onSkip()
                        sheetState.targetDetent = SheetDetent.Hidden
                    }
                )
                SheetActionRow(
                    icon = Lucide.AlarmClock,
                    label = "Reschedule",
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
                        onUndoSkip()
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
        DateTimeWheelPicker(
            startDateTime = now,
            maxDateTime = now,
            title = "Taken at",
            onDismiss = { showTakeAtTimeDialog = false },
            onConfirm = { dt ->
                onTakeAtTime(dt)
                showTakeAtTimeDialog = false
                state.targetDetent = SheetDetent.Hidden
            }
        )
    }

    if (showRescheduleDialog) {
        DateTimeWheelPicker(
            startDateTime = now,
            minDateTime = now,
            title = "Reschedule to",
            onDismiss = { showRescheduleDialog = false },
            onConfirm = { dt ->
                onReschedule(dt)
                showRescheduleDialog = false
                state.targetDetent = SheetDetent.Hidden
            }
        )
    }

    if (showNotesDialog) {
        NotesDialog(
            initialNotes = notes,
            onConfirm = { updated ->
                notes = updated
                onNotesChange(updated)
                showNotesDialog = false
            },
            onDismiss = { showNotesDialog = false }
        )
    }

    if (showQuantityDialog) {
        QuantityDialog(
            initialQuantity = dose.quantity,
            title = "Edit quantity",
            min = 0.25,
            max = 20.0,
            onDismiss = { showQuantityDialog = false },
            onConfirm = { newQty ->
                onQuantityChange(newQty)
                showQuantityDialog = false
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ResolvedDoseBottomSheetPreview() {
    RXTrackerTheme {
        ResolvedDoseBottomSheet(
            dose = ScheduledDoseWithMedication(
                id = 1,
                medicationId = 1,
                scheduledDate = today(),
                scheduledTime = LocalTime(12, 0),
                quantity = 2.0,
                status = DoseStatus.SKIPPED,
                resolvedAt = null,
                rescheduledDate = null,
                name = "Paracetamol",
                strength = "500mg",
                form = "Tablet",
                doseNotes = null
            ),
            onDismiss = {},
            onTakeAtTime = {},
            onUndoTake = {},
            onSkip = {},
            onUndoSkip = {},
            onReschedule = {},
            onQuantityChange = {},
            onNotesChange = {}
        )
    }
}
