package com.example.rxtracker.ui.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
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
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Composable
fun SingleDoseBottomSheet(
    dose: ScheduledDoseWithMedication,
    selectedDate: LocalDate,
    onDismiss: () -> Unit,
    onTakeOnTime: () -> Unit,
    onTakeNow: () -> Unit,
    onTakeAtTime: (LocalDateTime) -> Unit,
    onUndoTake: () -> Unit,
    onSkip: () -> Unit,
    onUnskip: () -> Unit,
    onReschedule: (LocalDateTime) -> Unit,
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
    var showRescheduleDialog by remember { mutableStateOf(false) }
    var showTakeAtTimeDialog by remember { mutableStateOf(false) }
    var showNotesDialog by remember { mutableStateOf(false) }
    var notes by remember { mutableStateOf(dose.doseNotes) }

    LaunchedEffect(dose.doseNotes) {
        notes = dose.doseNotes
    }

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

        if (isFutureDate) return@BaseBottomSheet

        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

        if (isToday) {
            when {
                isTaken -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        RowOption(
                            icon = Lucide.X,
                            label = "Undo Take",
                            onClick = {
                                onUndoTake()
                                state.targetDetent = SheetDetent.Hidden
                            }
                        )
                        RowOption(
                            icon = Lucide.Pencil,
                            label = "Set time",
                            onClick = { showTakeAtTimeDialog = true }
                        )
                    }
                }

                isSkipped -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        RowOption(
                            icon = Lucide.Check,
                            label = "Take on time",
                            sublabel = getFormattedTime(dose.scheduledTime),
                            onClick = {
                                onTakeOnTime()
                                state.targetDetent = SheetDetent.Hidden
                            }
                        )
                        RowOption(
                            icon = Lucide.Clock,
                            label = "Take now",
                            onClick = {
                                onTakeNow()
                                state.targetDetent = SheetDetent.Hidden
                            }
                        )
                        RowOption(
                            icon = Lucide.Pencil,
                            label = "Set time",
                            onClick = { showTakeAtTimeDialog = true }
                        )
                    }
                }

                else -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        RowOption(
                            icon = Lucide.Check,
                            label = "Take on time",
                            sublabel = getFormattedTime(dose.scheduledTime),
                            onClick = {
                                onTakeOnTime()
                                state.targetDetent = SheetDetent.Hidden
                            }
                        )
                        RowOption(
                            icon = Lucide.Clock,
                            label = "Take now",
                            onClick = {
                                onTakeNow()
                                state.targetDetent = SheetDetent.Hidden
                            }
                        )
                        RowOption(
                            icon = Lucide.Pencil,
                            label = "Set time",
                            onClick = { showTakeAtTimeDialog = true }
                        )
                    }
                }
            }
        }

        if (isPastDate) {
            when {
                isTaken -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        RowOption(
                            icon = Lucide.X,
                            label = "Undo Take",
                            onClick = {
                                onUndoTake()
                                state.targetDetent = SheetDetent.Hidden
                            }
                        )
                        RowOption(
                            icon = Lucide.Pencil,
                            label = "Set time",
                            onClick = { showTakeAtTimeDialog = true }
                        )
                        RowOption(
                            icon = Lucide.X,
                            label = "Skip",
                            onClick = {
                                onSkip()
                                state.targetDetent = SheetDetent.Hidden
                            }
                        )
                    }
                }

                isSkipped -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        RowOption(
                            icon = Lucide.Pencil,
                            label = "Set time",
                            onClick = { showTakeAtTimeDialog = true }
                        )
                        RowOption(
                            icon = Lucide.X,
                            label = "Unskip",
                            onClick = {
                                onUnskip()
                                state.targetDetent = SheetDetent.Hidden
                            }
                        )
                    }
                }

                isNotLogged -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        RowOption(
                            icon = Lucide.X,
                            label = "Skip",
                            onClick = {
                                onSkip()
                                state.targetDetent = SheetDetent.Hidden
                            }
                        )
                        RowOption(
                            icon = Lucide.Pencil,
                            label = "Set time",
                            onClick = { showTakeAtTimeDialog = true }
                        )
                    }
                }
            }
        }

        if (isToday) {
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                when {
                    isTaken -> {
                        OutlinedButton(
                            onClick = { onSkip() },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Skip")
                        }
                        OutlinedButton(
                            onClick = { showRescheduleDialog = true },
                            modifier = Modifier.weight(1f)
                        ) { Text("Reschedule") }
                    }

                    isSkipped -> {
                        OutlinedButton(
                            onClick = {
                                onUnskip()
                                state.targetDetent = SheetDetent.Hidden
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.error
                            ),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
                        ) { Text("Unskip") }
                        OutlinedButton(
                            onClick = { showRescheduleDialog = true },
                            modifier = Modifier.weight(1f)
                        ) { Text("Reschedule") }
                    }

                    else -> {
                        OutlinedButton(
                            onClick = {
                                onSkip()
                                state.targetDetent = SheetDetent.Hidden
                            },
                            modifier = Modifier.weight(1f)
                        ) { Text("Skip") }
                        OutlinedButton(
                            onClick = { showRescheduleDialog = true },
                            modifier = Modifier.weight(1f)
                        ) { Text("Reschedule") }
                    }
                }
            }
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

    if (showRescheduleDialog) {
        TimeSelectionDialog(
            startTime = dose.scheduledTime,
            title = "Reschedule to",
            onDismiss = { showRescheduleDialog = false },
            onConfirm = { hour, minute ->
                val newTime = LocalDateTime.of(selectedDate, LocalTime.of(hour, minute))
                onReschedule(newTime)
                showRescheduleDialog = false
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
fun SingleDoseBottomSheetPreview() {
    RXTrackerTheme {
        SingleDoseBottomSheet(
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
            onUndoTake = {},
            onSkip = {},
            onUnskip = {},
            onReschedule = {},
            onQuantityChange = {},
            onNotesChange = {}
        )
    }
}