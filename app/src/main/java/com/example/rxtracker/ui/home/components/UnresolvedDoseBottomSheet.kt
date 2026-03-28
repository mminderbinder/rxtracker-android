package com.example.rxtracker.ui.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import com.composables.icons.lucide.Redo
import com.composeunstyled.Text
import com.example.rxtracker.data.models.DoseStatus
import com.example.rxtracker.data.models.ScheduledDoseWithMedication
import com.example.rxtracker.ui.shared.DateTimeWheelPicker
import com.example.rxtracker.ui.shared.NotesDialog
import com.example.rxtracker.ui.shared.QuantityDialog
import com.example.rxtracker.ui.theme.RXTrackerTheme
import com.example.rxtracker.utils.getFormattedTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.time.Clock

@Composable
fun UnresolvedDoseBottomSheet(
    dose: ScheduledDoseWithMedication,
    onDismiss: () -> Unit,
    onTakeOnTime: () -> Unit,
    onTakeNow: () -> Unit,
    onTakeAtTime: (LocalDateTime) -> Unit,
    onSkip: () -> Unit,
    onReschedule: (LocalDateTime) -> Unit,
    onQuantityChange: (Double) -> Unit,
    onNotesChange: (String?) -> Unit
) {
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    val state = rememberModalBottomSheetState(initialDetent = SheetDetent.FullyExpanded)

    var showQuantityDialog by remember { mutableStateOf(false) }
    var showRescheduleDialog by remember { mutableStateOf(false) }
    var showTakeAtTimeDialog by remember { mutableStateOf(false) }
    var showNotesDialog by remember { mutableStateOf(false) }
    var notes by remember { mutableStateOf(dose.doseNotes) }

    LaunchedEffect(dose.doseNotes) {
        notes = dose.doseNotes
    }

    BaseBottomSheet(state = state, onDismiss = onDismiss) { state ->

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

        SheetActionRow(
            icon = Lucide.Clock,
            label = "Take now",
            onClick = {
                onTakeNow()
                state.targetDetent = SheetDetent.Hidden
            }
        )
        SheetActionRow(
            icon = Lucide.Check,
            label = "Take on time",
            sublabel = getFormattedTime(dose.scheduledTime),
            onClick = {
                onTakeOnTime()
                state.targetDetent = SheetDetent.Hidden
            }
        )
        SheetActionRow(
            icon = Lucide.Pencil,
            label = "Add time taken",
            onClick = { showTakeAtTimeDialog = true }
        )

        Spacer(modifier = Modifier.height(4.dp))
        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = {
                    onSkip()
                    state.targetDetent = SheetDetent.Hidden
                },
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Lucide.Redo,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Skip")
            }
            OutlinedButton(
                onClick = { showRescheduleDialog = true },
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Lucide.AlarmClock,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Reschedule")
            }
        }
    }

    if (showTakeAtTimeDialog) {
        DateTimeWheelPicker(
            startDateTime = now,
            maxDateTime = now,
            title = "Taken at",
            onDismiss = { showTakeAtTimeDialog = false },
            onConfirm = { dt ->
                onTakeAtTime(dt.toJavaLocalDateTime())
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
                onReschedule(dt.toJavaLocalDateTime())
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

@Preview
@Composable
fun UnresolvedDoseBottomSheetPreview() {
    RXTrackerTheme {
        UnresolvedDoseBottomSheet(
            dose = ScheduledDoseWithMedication(
                id = 1,
                medicationId = 1,
                scheduledDate = LocalDate.now(),
                scheduledTime = LocalDate.now().atTime(12, 0).toLocalTime(),
                quantity = 2.0,
                status = DoseStatus.PENDING,
                resolvedAt = null,
                rescheduledDate = null,
                name = "Paracetamol",
                strength = "500mg",
                form = "Tablet",
                doseNotes = null
            ),
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