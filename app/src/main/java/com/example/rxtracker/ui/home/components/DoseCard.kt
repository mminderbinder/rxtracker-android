package com.example.rxtracker.ui.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
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
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

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

    val contentAlpha = if (isSkipped) 0.5f else 1f

    val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT)

    val containerColor = when (dose.status) {
        DoseStatus.TAKEN -> MaterialTheme.colorScheme.secondaryContainer
        DoseStatus.MISSED -> MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f)
        DoseStatus.SKIPPED -> MaterialTheme.colorScheme.surfaceVariant
        DoseStatus.PENDING -> MaterialTheme.colorScheme.surface
    }

    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onTap() },
        colors = CardDefaults.outlinedCardColors(containerColor = containerColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                painter = painterResource(id = resolveFormIcon(dose.form)),
                contentDescription = dose.form,
                tint = MaterialTheme.colorScheme.primary.copy(alpha = contentAlpha)
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = dose.name,
                    style = MaterialTheme.typography.bodyLarge,
                    textDecoration = if (isSkipped) TextDecoration.LineThrough else TextDecoration.None,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = contentAlpha)
                )
                Text(
                    text = buildString {
                        if (dose.strength.isNotBlank()) append("${dose.strength} · ")
                        append(formatQuantity(dose.quantity, dose.form))
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = contentAlpha)
                )

                val statusText = when (dose.status) {
                    DoseStatus.TAKEN -> dose.takenAt?.let { "Taken at ${it.format(formatter)}" }
                    DoseStatus.SKIPPED -> "Skipped"
                    DoseStatus.MISSED -> "Missed"
                    DoseStatus.PENDING -> null
                }

                statusText?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = when (dose.status) {
                            DoseStatus.TAKEN -> MaterialTheme.colorScheme.primary
                            DoseStatus.MISSED -> MaterialTheme.colorScheme.error
                            else -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = contentAlpha)
                        }
                    )
                }
            }
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
                form = "Capsule"
            ),
            selectedDate = LocalDate.now(),
            onTap = {},
            onToggleTaken = {}
        )
    }
}