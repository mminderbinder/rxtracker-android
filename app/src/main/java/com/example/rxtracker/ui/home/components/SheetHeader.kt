package com.example.rxtracker.ui.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Pencil
import com.example.rxtracker.data.models.DoseStatus
import com.example.rxtracker.data.models.ScheduledDoseWithMedication
import com.example.rxtracker.utils.formatQuantity
import com.example.rxtracker.utils.getFormattedTime


@Composable
fun SingleDoseSheetHeader(
    dose: ScheduledDoseWithMedication,
    onQuantityTap: () -> Unit,
    onEditTap: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isTaken = dose.status == DoseStatus.TAKEN

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

        DoseStatus.RESCHEDULED -> Triple(
            MaterialTheme.colorScheme.surfaceVariant,
            MaterialTheme.colorScheme.onSurfaceVariant,
            "Rescheduled"
        )

        DoseStatus.PENDING -> Triple(Color.Unspecified, Color.Unspecified, "")
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = dose.name,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
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
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (dose.strength.isNotBlank()) {
                    Text(
                        text = "${dose.strength} ·",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                TextButton(
                    onClick = onQuantityTap,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = formatQuantity(dose.quantity, dose.form),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium,
                            textDecoration = TextDecoration.Underline
                        )
                    )
                }
            }
            Text(
                text = "Scheduled ${getFormattedTime(dose.scheduledTime)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (isTaken && dose.resolvedAt != null) {
                Text(
                    text = "Taken at ${getFormattedTime(dose.resolvedAt.time)}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            modifier = Modifier.size(40.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                IconButton(onClick = onEditTap) {
                    Icon(
                        imageVector = Lucide.Pencil,
                        contentDescription = "Edit notes",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun BatchDoseSheetHeader(
    doses: List<ScheduledDoseWithMedication>,
    modifier: Modifier = Modifier
) {
    val scheduledTime = doses.first().scheduledTime

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "${doses.size} doses at ${getFormattedTime(scheduledTime)}",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        doses.forEach { dose ->
            Text(
                text = "· ${dose.name} ${dose.strength}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
