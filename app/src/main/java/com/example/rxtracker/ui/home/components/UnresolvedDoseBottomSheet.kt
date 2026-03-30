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
import com.composables.icons.lucide.Check
import com.composables.icons.lucide.Clock
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Pencil
import com.composables.icons.lucide.X
import com.example.rxtracker.data.models.DoseStatus
import com.example.rxtracker.data.models.ScheduledDoseWithMedication
import com.example.rxtracker.ui.shared.NotesDialog
import com.example.rxtracker.ui.shared.QuantityDialog
import com.example.rxtracker.ui.shared.TimeSelectionDialog
import com.example.rxtracker.ui.theme.RXTrackerTheme
import com.example.rxtracker.utils.getFormattedTime
import com.example.rxtracker.utils.now
import com.example.rxtracker.utils.today
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

@Composable
fun UnresolvedDoseBottomSheet(
    dose: ScheduledDoseWithMedication,
    selectedDate: LocalDate,
    onDismiss: () -> Unit,
    onTakeOnTime: () -> Unit,
    onTakeNow: () -> Unit,
    onTakeAtTime: (LocalDateTime) -> Unit,
    onSkip: () -> Unit,
    onReschedule: (LocalTime) -> Unit,
    onQuantityChange: (Double) -> Unit,
    onNotesChange: (String?) -> Unit
) {
    val now = now()
    val scheduledTime = LocalDateTime(date = dose.scheduledDate, time = dose.scheduledTime)

    val state = rememberModalBottomSheetState(initialDetent = SheetDetent.FullyExpanded)

    var showQuantityDialog by remember { mutableStateOf(false) }
    var showRescheduleDialog by remember { mutableStateOf(false) }
    var showTakeAtTimeDialog by remember { mutableStateOf(false) }
    var showNotesDialog by remember { mutableStateOf(false) }
    var notes by remember { mutableStateOf(dose.doseNotes) }

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

        Spacer(modifier = Modifier.height(4.dp))
        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))


        SheetActionRow(
            icon = Lucide.Clock,
            label = "Take now",
            onClick = {
                onTakeNow()
                sheetState.targetDetent = SheetDetent.Hidden
            }
        )

        if (now > scheduledTime) {
            SheetActionRow(
                icon = Lucide.Check,
                label = "Take on time",
                sublabel = getFormattedTime(dose.scheduledTime),
                onClick = {
                    onTakeOnTime()
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

    if (showTakeAtTimeDialog) {
        TimeSelectionDialog(
            startTime = dose.scheduledTime,
            onConfirm = { hour, minute ->
                onTakeAtTime(LocalDateTime(selectedDate, LocalTime(hour, minute)))
                showTakeAtTimeDialog = false
                state.targetDetent = SheetDetent.Hidden
            },
            onDismiss = { showTakeAtTimeDialog = false }
        )
    }

    if (showRescheduleDialog) {
        TimeSelectionDialog(
            startTime = dose.scheduledTime,
            onConfirm = { hour, minute ->
                onReschedule(LocalTime(hour, minute))
                showRescheduleDialog = false
                state.targetDetent = SheetDetent.Hidden
            },
            onDismiss = { showRescheduleDialog = false }
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

@Preview
@Composable
fun UnresolvedDoseBottomSheetPreview() {
    RXTrackerTheme {
        UnresolvedDoseBottomSheet(
            dose = ScheduledDoseWithMedication(
                id = 1,
                medicationId = 1,
                scheduledDate = today(),
                scheduledTime = LocalTime(12, 0),
                quantity = 2.0,
                status = DoseStatus.PENDING,
                resolvedAt = null,
                name = "Paracetamol",
                strength = "500mg",
                form = "Tablet",
                doseNotes = null
            ),
            selectedDate = today(),
            onDismiss = {},
            onTakeOnTime = {},
            onTakeNow = {},
            onTakeAtTime = {},
            onQuantityChange = {},
            onNotesChange = {},
            onSkip = {},
            onReschedule = {}
        )
    }
}
