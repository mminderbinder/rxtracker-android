package com.example.rxtracker.ui.home.components

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.composables.core.DragIndication
import com.composables.core.ModalBottomSheet
import com.composables.core.Scrim
import com.composables.core.Sheet
import com.composables.core.SheetDetent
import com.composables.core.rememberModalBottomSheetState
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
import com.example.rxtracker.utils.formatQuantity
import com.example.rxtracker.utils.resolveFormIcon
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
    val state = rememberModalBottomSheetState(initialDetent = SheetDetent.FullyExpanded)

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

    LaunchedEffect(dose.doseNotes) {
        notes = dose.doseNotes
    }

    LaunchedEffect(state.currentDetent) {
        if (state.currentDetent == SheetDetent.Hidden) onDismiss()
    }

    ModalBottomSheet(state = state) {
        Scrim(
            scrimColor = Color.Black.copy(alpha = 0.3f),
            enter = fadeIn(),
            exit = fadeOut()
        )
        Sheet(
            modifier = Modifier
                .statusBarsPadding()
                .padding(
                    WindowInsets.navigationBars
                        .only(WindowInsetsSides.Horizontal)
                        .asPaddingValues()
                )
                .shadow(4.dp, RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                .background(MaterialTheme.colorScheme.surfaceContainerLow)
                .fillMaxWidth()
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {

                // Drag handle
                DragIndication(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 22.dp)
                        .background(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                            shape = RoundedCornerShape(100)
                        )
                        .width(32.dp)
                        .height(4.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                painter = painterResource(id = resolveFormIcon(dose.form)),
                                contentDescription = dose.form,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = dose.name,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (dose.strength.isNotBlank()) {
                                Text(
                                    text = "${dose.strength} ·",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            TextButton(
                                onClick = { showQuantityDialog = true },
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text(
                                    text = formatQuantity(dose.quantity, dose.form),
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = FontWeight.Medium,
                                        textDecoration = TextDecoration.Underline
                                    )
                                )
                            }
                        }
                        Text(
                            text = "Scheduled ${dose.scheduledTime.format(timeFormatter)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        if (isTaken && dose.takenAt != null) {
                            Text(
                                text = "Taken at ${
                                    dose.takenAt.toLocalTime().format(timeFormatter)
                                }",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Medium
                                ),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    val (chipColor, chipTextColor, chipLabel) = when (dose.status) {
                        DoseStatus.TAKEN -> Triple(
                            MaterialTheme.colorScheme.primaryContainer,
                            MaterialTheme.colorScheme.onPrimaryContainer,
                            "Taken"
                        )

                        DoseStatus.SKIPPED -> Triple(
                            MaterialTheme.colorScheme.surfaceVariant,
                            MaterialTheme.colorScheme.onSurfaceVariant,
                            "Skipped"
                        )

                        DoseStatus.LATE -> Triple(
                            MaterialTheme.colorScheme.errorContainer,
                            MaterialTheme.colorScheme.onErrorContainer,
                            "Late"
                        )

                        DoseStatus.NOT_LOGGED -> Triple(
                            MaterialTheme.colorScheme.surfaceVariant,
                            MaterialTheme.colorScheme.onSurfaceVariant,
                            "Not Logged"
                        )

                        DoseStatus.PENDING -> Triple(Color.Unspecified, Color.Unspecified, "")
                    }
                    if (chipLabel.isNotEmpty()) {
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = chipColor
                        ) {
                            Text(
                                text = chipLabel,
                                style = MaterialTheme.typography.labelSmall,
                                color = chipTextColor,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

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
                                modifier = Modifier.weight(1f),
                                colors = if (!isSkipped) ButtonDefaults.outlinedButtonColors(
                                    contentColor = MaterialTheme.colorScheme.error
                                ) else ButtonDefaults.outlinedButtonColors(),
                                border = if (!isSkipped) BorderStroke(
                                    1.dp, MaterialTheme.colorScheme.error
                                ) else ButtonDefaults.outlinedButtonBorder
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

                Spacer(modifier = Modifier
                    .navigationBarsPadding()
                    .height(16.dp))
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
                state.targetDetent = SheetDetent.Hidden
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

@Composable
private fun SheetActionRow(
    icon: ImageVector,
    label: String,
    sublabel: String? = null,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val iconTint = if (enabled)
        MaterialTheme.colorScheme.onSurfaceVariant
    else
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
    val textColor = if (enabled)
        MaterialTheme.colorScheme.onSurface
    else
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)

    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable(enabled = enabled, onClick = onClick)
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 14.dp)
    ) {
        Surface(
            shape = CircleShape,
            color = if (enabled)
                MaterialTheme.colorScheme.surfaceContainerHigh
            else
                MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.38f),
            modifier = Modifier.size(40.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = textColor
            )
            if (sublabel != null) {
                Text(
                    text = sublabel,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (enabled)
                        MaterialTheme.colorScheme.onSurfaceVariant
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f)
                )
            }
        }
    }
}