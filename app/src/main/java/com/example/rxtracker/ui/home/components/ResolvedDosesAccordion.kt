package com.example.rxtracker.ui.home.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntSize
import com.example.rxtracker.data.models.ScheduledDoseWithMedication
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

@Composable
fun ResolvedDosesAccordion(
    modifier: Modifier = Modifier,
    doses: List<ScheduledDoseWithMedication>,
    selectedDate: LocalDate,
    onTap: (ScheduledDoseWithMedication) -> Unit,
    onSelectAll: (() -> Unit)? = null
) {
    var expanded by remember { mutableStateOf(false) }

    val sortedDoses = remember(doses) {
        doses.sortedBy { it.resolvedAt ?: LocalDateTime(selectedDate, it.scheduledTime) }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        DoseTimeHeader(
            title = "Resolved (${doses.size})",
            onSelectAll = if (doses.size > 1) onSelectAll else null,
            expanded = expanded,
            modifier = Modifier.clickable { expanded = !expanded }
        )

        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically(
                spring(
                    stiffness = Spring.StiffnessMediumLow,
                    visibilityThreshold = IntSize.VisibilityThreshold
                )
            ),
            exit = shrinkVertically()
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                sortedDoses.forEach { dose ->
                    ResolvedDoseRow(
                        dose = dose,
                        onTap = { onTap(dose) }
                    )
                }
            }
        }
    }
}