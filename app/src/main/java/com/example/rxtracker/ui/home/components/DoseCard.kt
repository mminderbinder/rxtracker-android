package com.example.rxtracker.ui.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rxtracker.data.models.DoseStatus
import com.example.rxtracker.data.models.ScheduledDoseWithMedication
import com.example.rxtracker.ui.theme.RXTrackerTheme
import com.example.rxtracker.utils.formatQuantity
import com.example.rxtracker.utils.resolveFormIcon
import java.time.LocalDate

@Composable
fun DoseCard(
    dose: ScheduledDoseWithMedication,
    selectedDate: LocalDate,
    onTap: () -> Unit,
    onToggleTaken: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val today = LocalDate.now()
    val isFutureDate = selectedDate.isAfter(today)
    val isTaken = dose.status == DoseStatus.TAKEN
    val isSkipped = dose.status == DoseStatus.SKIPPED

    val containerColor = when (dose.status) {
        DoseStatus.TAKEN -> MaterialTheme.colorScheme.secondaryContainer
        DoseStatus.MISSED -> MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f)
        DoseStatus.SKIPPED -> MaterialTheme.colorScheme.surfaceVariant
        DoseStatus.PENDING -> MaterialTheme.colorScheme.surface
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onTap() },
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Form icon — far left
            Image(
                painter = painterResource(id = resolveFormIcon(dose.form)),
                contentDescription = dose.form,
                modifier = Modifier.size(32.dp),
                alpha = if (isSkipped) 0.4f else 1f
            )

            // Medication info — middle
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = dose.name,
                    style = MaterialTheme.typography.bodyLarge,
                    textDecoration = if (isSkipped) TextDecoration.LineThrough else TextDecoration.None,
                    color = if (isSkipped)
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    else
                        MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = buildString {
                        if (dose.strength.isNotBlank()) append("${dose.strength} · ")
                        append(formatQuantity(dose.quantity, dose.form))
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Checkbox — far right
            if (!isFutureDate) {
                Checkbox(
                    checked = isTaken,
                    onCheckedChange = { checked ->
                        if (!isSkipped) onToggleTaken(checked)
                    },
                    enabled = !isSkipped
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DoseCardPreview() {
    RXTrackerTheme {
        DoseCard(
            dose = ScheduledDoseWithMedication(
                id = 1,
                medicationId = 1,
                scheduledDate = LocalDate.now(),
                scheduledTime = LocalDate.now().atTime(12, 0).toLocalTime(),
                quantity = 1.0,
                status = DoseStatus.PENDING,
                takenAt = null,
                name = "Ibuprofen",
                strength = "200mg",
                form = "Tablet"
            ),
            selectedDate = LocalDate.now(),
            onTap = {},
            onToggleTaken = {}
        )
    }
}