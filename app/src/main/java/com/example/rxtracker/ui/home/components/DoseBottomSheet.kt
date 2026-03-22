package com.example.rxtracker.ui.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.composables.core.SheetDetent
import com.composables.icons.lucide.Check
import com.composables.icons.lucide.Clock
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Pencil
import com.composables.icons.lucide.Timer
import com.composables.icons.lucide.X
import com.example.rxtracker.data.models.DoseStatus
import com.example.rxtracker.data.models.ScheduledDoseWithMedication
import com.example.rxtracker.ui.shared.NotesDialog
import com.example.rxtracker.ui.shared.QuantityDialog
import com.example.rxtracker.ui.shared.TimeSelectionDialog
import com.example.rxtracker.ui.theme.RXTrackerTheme
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

private val timeFormatter = DateTimeFormatter.ofPattern("h:mm a")

@Composable
fun DoseBottomSheet(
    dose: ScheduledDoseWithMedication,
    selectedDate: LocalDate,
    onDismiss: () -> Unit,
    onTakeOnTime: () -> Unit,
    onTakeNow: () -> Unit,
    onTakeAtTime: (LocalDateTime) -> Unit,
    onUntake: () -> Unit,
    onSkip: () -> Unit,
    onUnskip: () -> Unit,
    onPostpone: (LocalDateTime) -> Unit,
    onQuantityChange: (Double) -> Unit,
    onNotesChange: (String?) -> Unit
) {
    val today = LocalDate.now()
    val isToday = selectedDate == today
    val isPastDate = selectedDate.isBefore(today)
    val isFutureDate = selectedDate.isAfter(today)

    val isTaken = dose.status == DoseStatus.TAKEN
    val isSkipped = dose.status == DoseStatus.SKIPPED
    val isNotLogged = dose.status == DoseStatus.NOT_LOGGED

    var showQuantityDialog by remember { mutableStateOf(false) }
    var showPostponeDialog by remember { mutableStateOf(false) }
    var showTakeAtTimeDialog by remember { mutableStateOf(false) }
    var showNotesDialog by remember { mutableStateOf(false) }
    var notes by remember { mutableStateOf(dose.doseNotes) }

    BaseBottomSheet(onDismiss = onDismiss) { state ->

        SingleDoseSheetHeader(
            dose = dose,
            onQuantityTap = { showQuantityDialog = true }
        )

        SheetActionRow(
            icon = Lucide.Pencil,
            label = if (notes.isNullOrBlank()) "Add notes" else "Edit notes",
            onClick = { showNotesDialog = true },
            modifier = Modifier.padding(top = 4.dp)
        )
        if (!notes.isNullOrBlank()) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 4.dp)
            ) {
                Text(
                    text = notes!!,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

        // Actions — today
        if (isToday) {
            when {
                isTaken -> {
                    SheetActionRow(
                        icon = Lucide.X,
                        label = "Undo",
                        onClick = {
                            onUntake()
                            state.targetDetent = SheetDetent.Hidden
                        }
                    )
                    SheetActionRow(
                        icon = Lucide.X,
                        label = "Skip",
                        onClick = {
                            onSkip()
                            state.targetDetent = SheetDetent.Hidden
                        }
                    )
                }

                isSkipped -> {
                    SheetActionRow(
                        icon = Lucide.X,
                        label = "Unskip",
                        onClick = {
                            onUnskip()
                            state.targetDetent = SheetDetent.Hidden
                        }
                    )
                }

                else -> {
                    SheetActionRow(
                        icon = Lucide.Check,
                        label = "Take on time",
                        sublabel = dose.scheduledTime.format(timeFormatter),
                        onClick = {
                            onTakeOnTime()
                            state.targetDetent = SheetDetent.Hidden
                        }
                    )
                    SheetActionRow(
                        icon = Lucide.Clock,
                        label = "Take now",
                        onClick = {
                            onTakeNow()
                            state.targetDetent = SheetDetent.Hidden
                        }
                    )
                    SheetActionRow(
                        icon = Lucide.Pencil,
                        label = "Input time",
                        onClick = { showTakeAtTimeDialog = true }
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (!isTaken) {
                    OutlinedButton(
                        onClick = {
                            if (isSkipped) onUnskip() else onSkip()
                            state.targetDetent = SheetDetent.Hidden
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(if (isSkipped) "Unskip" else "Skip")
                    }
                }
                if (!isTaken && !isSkipped) {
                    OutlinedButton(
                        onClick = { showPostponeDialog = true },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Lucide.Timer,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Reschedule")
                    }
                }
            }
        }

        if (isPastDate) {
            when {
                isTaken -> {
                    SheetActionRow(
                        icon = Lucide.X,
                        label = "Undo",
                        sublabel = "Will revert to Not Logged",
                        onClick = {
                            onUntake()
                            state.targetDetent = SheetDetent.Hidden
                        }
                    )
                    SheetActionRow(
                        icon = Lucide.X,
                        label = "Skip",
                        onClick = {
                            onSkip()
                            state.targetDetent = SheetDetent.Hidden
                        }
                    )
                }

                isSkipped -> {
                    SheetActionRow(
                        icon = Lucide.X,
                        label = "Unskip",
                        sublabel = "Will revert to Not Logged",
                        onClick = {
                            onUnskip()
                            state.targetDetent = SheetDetent.Hidden
                        }
                    )
                }

                isNotLogged -> {
                    SheetActionRow(
                        icon = Lucide.Pencil,
                        label = "Mark as taken",
                        sublabel = "Specify time",
                        onClick = { showTakeAtTimeDialog = true }
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                onSkip()
                                state.targetDetent = SheetDetent.Hidden
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Mark as skipped")
                        }
                    }
                }

                else -> Unit
            }
        }

        if (isFutureDate) {
            Text(
                text = "No actions available for future doses",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
            )
        }
    }

    if (showTakeAtTimeDialog) {
        TimeSelectionDialog(
            startTime = dose.scheduledTime,
            title = if (isPastDate) "Select time taken" else "Input time taken",
            onDismiss = { showTakeAtTimeDialog = false },
            onConfirm = { hour, minute ->
                val takenAt = LocalDateTime.of(selectedDate, LocalTime.of(hour, minute))
                onTakeAtTime(takenAt)
                showTakeAtTimeDialog = false
            }
        )
    }

    if (showPostponeDialog) {
        TimeSelectionDialog(
            startTime = dose.scheduledTime,
            title = "Reschedule to",
            onDismiss = { showPostponeDialog = false },
            onConfirm = { hour, minute ->
                val newTime = LocalDateTime.of(selectedDate, LocalTime.of(hour, minute))
                onPostpone(newTime)
                showPostponeDialog = false
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
fun DoseBottomSheetPreview() {
    RXTrackerTheme {
        DoseBottomSheet(
            dose = ScheduledDoseWithMedication(
                id = 1,
                medicationId = 1,
                scheduledDate = LocalDate.now(),
                scheduledTime = LocalDate.now().atTime(12, 0).toLocalTime(),
                quantity = 2.0,
                status = DoseStatus.PENDING,
                takenAt = null,
                name = "Paracetamol",
                strength = "500mg",
                form = "Tablet",
                doseNotes = null
            ),
            selectedDate = LocalDate.now(),
            onDismiss = {},
            onTakeOnTime = {},
            onTakeNow = {},
            onTakeAtTime = {},
            onUntake = {},
            onSkip = {},
            onUnskip = {},
            onPostpone = {},
            onQuantityChange = {},
            onNotesChange = {}
        )
    }
}