package com.example.rxtracker.ui.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rxtracker.data.models.DoseStatus
import com.example.rxtracker.data.models.ScheduledDoseWithMedication
import com.example.rxtracker.ui.theme.RXTrackerTheme
import com.example.rxtracker.utils.formatQuantity
import com.example.rxtracker.utils.resolveFormIcon
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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
    val isNotLogged = dose.status == DoseStatus.NOT_LOGGED
    val isLate = dose.status == DoseStatus.LATE

    val isDimmed = isTaken || isSkipped || isFutureDate
    val contentAlpha = if (isDimmed) 0.5f else 1f
    val textDecoration =
        if (isTaken || isSkipped) TextDecoration.LineThrough else TextDecoration.None

    val formatter = DateTimeFormatter.ofPattern("h:mm a")

    val containerColor = when {
        isTaken || isSkipped -> MaterialTheme.colorScheme.surfaceVariant
        else -> MaterialTheme.colorScheme.surfaceContainerLow
    }

    val border = when {
        isLate -> BorderStroke(1.5.dp, MaterialTheme.colorScheme.error)
        isNotLogged -> BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
        )

        else -> CardDefaults.outlinedCardBorder()
    }

    val statusText = when (dose.status) {
        DoseStatus.TAKEN -> dose.takenAt?.let { "Taken at ${it.format(formatter)}" } ?: "Taken"
        DoseStatus.SKIPPED -> "Skipped"
        DoseStatus.LATE -> "Late"
        DoseStatus.NOT_LOGGED -> "Not Logged"
        DoseStatus.PENDING -> if (isFutureDate) "Upcoming" else "Pending"
    }

    val statusColor = when (dose.status) {
        DoseStatus.TAKEN -> MaterialTheme.colorScheme.primary.copy(alpha = contentAlpha)
        DoseStatus.LATE -> MaterialTheme.colorScheme.error
        DoseStatus.SKIPPED -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = contentAlpha)
        DoseStatus.NOT_LOGGED -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
        DoseStatus.PENDING -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = contentAlpha)
    }

    val showCheckbox = !isFutureDate && !isSkipped && !isNotLogged

    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 80.dp)
            .clickable { onTap() },
        colors = CardDefaults.outlinedCardColors(containerColor = containerColor),
        border = border
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
                tint = if (isLate) MaterialTheme.colorScheme.error
                else MaterialTheme.colorScheme.primary.copy(alpha = contentAlpha)
            )

            // Medication info — middle
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = dose.name,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium,
                        textDecoration = textDecoration,
                    ),
                    color = statusColor,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )

                Text(
                    text = buildString {
                        if (dose.strength.isNotBlank()) append("${dose.strength} · ")
                        append(formatQuantity(dose.quantity, dose.form))
                    },
                    style = MaterialTheme.typography.bodySmall.copy(textDecoration = textDecoration),
                    color = statusColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = statusText,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = statusColor
                )
            }

            if (showCheckbox) {
                Checkbox(
                    checked = isTaken,
                    onCheckedChange = { checked -> onToggleTaken(checked) },
                    colors = if (isLate) CheckboxDefaults.colors(
                        uncheckedColor = MaterialTheme.colorScheme.error,
                        checkmarkColor = MaterialTheme.colorScheme.error
                    ) else CheckboxDefaults.colors()
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