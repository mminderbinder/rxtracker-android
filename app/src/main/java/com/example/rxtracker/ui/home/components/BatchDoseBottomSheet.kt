package com.example.rxtracker.ui.home.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composables.core.SheetDetent
import com.composables.icons.lucide.Check
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.X
import com.example.rxtracker.data.models.DoseStatus
import com.example.rxtracker.data.models.ScheduledDoseWithMedication
import java.time.LocalDate

@Composable
fun BatchDoseBottomSheet(
    doses: List<ScheduledDoseWithMedication>,
    selectedDate: LocalDate,
    onDismiss: () -> Unit,
    onTakeAll: () -> Unit,
    onSkipAll: () -> Unit,
    onUndoAll: () -> Unit
) {
    val today = LocalDate.now()
    val isToday = selectedDate == today
    val isPastDate = selectedDate.isBefore(today)

    val allTaken = doses.all { it.status == DoseStatus.TAKEN }
    val allSkipped = doses.all { it.status == DoseStatus.SKIPPED }
    val allNotLogged = doses.all { it.status == DoseStatus.NOT_LOGGED }
    val anyPending = doses.any {
        it.status == DoseStatus.PENDING || it.status == DoseStatus.LATE
    }

    BaseBottomSheet(onDismiss = onDismiss) { state ->

        BatchDoseSheetHeader(doses = doses)

        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

        if (isToday) {
            when {
                allTaken -> {
                    SheetActionRow(
                        icon = Lucide.X,
                        label = "Undo all",
                        onClick = {
                            onUndoAll()
                            state.targetDetent = SheetDetent.Hidden
                        }
                    )
                    SheetActionRow(
                        icon = Lucide.X,
                        label = "Skip all",
                        onClick = {
                            onSkipAll()
                            state.targetDetent = SheetDetent.Hidden
                        }
                    )
                }

                allSkipped -> {
                    SheetActionRow(
                        icon = Lucide.X,
                        label = "Undo all",
                        onClick = {
                            onUndoAll()
                            state.targetDetent = SheetDetent.Hidden
                        }
                    )
                }

                anyPending -> {
                    SheetActionRow(
                        icon = Lucide.Check,
                        label = "Take all",
                        onClick = {
                            onTakeAll()
                            state.targetDetent = SheetDetent.Hidden
                        }
                    )
                    SheetActionRow(
                        icon = Lucide.X,
                        label = "Skip all",
                        onClick = {
                            onSkipAll()
                            state.targetDetent = SheetDetent.Hidden
                        }
                    )
                }
            }
        }

        if (isPastDate) {
            when {
                allTaken -> {
                    SheetActionRow(
                        icon = Lucide.X,
                        label = "Undo all",
                        sublabel = "Will revert to Not Logged",
                        onClick = {
                            onUndoAll()
                            state.targetDetent = SheetDetent.Hidden
                        }
                    )
                    SheetActionRow(
                        icon = Lucide.X,
                        label = "Skip all",
                        onClick = {
                            onSkipAll()
                            state.targetDetent = SheetDetent.Hidden
                        }
                    )
                }

                allSkipped -> {
                    SheetActionRow(
                        icon = Lucide.X,
                        label = "Undo all",
                        sublabel = "Will revert to Not Logged",
                        onClick = {
                            onUndoAll()
                            state.targetDetent = SheetDetent.Hidden
                        }
                    )
                }

                allNotLogged -> {
                    SheetActionRow(
                        icon = Lucide.X,
                        label = "Skip all",
                        onClick = {
                            onSkipAll()
                            state.targetDetent = SheetDetent.Hidden
                        }
                    )
                }
            }
        }
    }
}