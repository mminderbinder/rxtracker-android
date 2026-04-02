package com.example.rxtracker.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.ChevronRight
import com.composables.icons.lucide.Lucide
import com.example.rxtracker.data.models.DoseStatus
import com.example.rxtracker.data.models.ScheduledDoseWithMedication
import com.example.rxtracker.ui.theme.LocalExtendedColorScheme
import com.example.rxtracker.ui.theme.RXTrackerTheme
import com.example.rxtracker.utils.getFormattedTime
import com.example.rxtracker.utils.today
import kotlinx.datetime.LocalTime
import kotlin.math.min

@Composable
fun ResolvedDoseRow(
    dose: ScheduledDoseWithMedication,
    onTap: () -> Unit,
    modifier: Modifier = Modifier
) {
    val extendedColorScheme = LocalExtendedColorScheme.current

    val (dotColor: Color, timeLabel) = when (dose.status) {
        DoseStatus.TAKEN -> Pair(
            extendedColorScheme.success.color,
            dose.resolvedAt?.let { "Taken ${getFormattedTime(it.time)}" } ?: "Taken"
        )
        DoseStatus.SKIPPED -> Pair(
            MaterialTheme.colorScheme.onSurfaceVariant,
            dose.resolvedAt?.let { "Skipped ${getFormattedTime(it.time)}" } ?: "Skipped"
        )
        else -> Pair(Color.Transparent, "")
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp)
            .clickable(onClick = onTap)
            .padding(horizontal = 4.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(dotColor, CircleShape)
        )
        Text(
            text = buildString {
                append(dose.name)
                if (dose.strength.isNotBlank()) append(" ${dose.strength}")
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = timeLabel,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1
        )
        Icon(
            imageVector = Lucide.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ResolvedDoseRowPreview() {
    RXTrackerTheme {
        ResolvedDoseRow(
            dose = ScheduledDoseWithMedication(
                id = 1,
                medicationId = 1,
                scheduledDate = today(),
                scheduledTime = LocalTime(12, 0),
                quantity = 2.0,
                status = DoseStatus.PENDING,
                resolvedAt = null,
                name = "Paracetamol",
                strength = "500mg",
                form = "Tablet",
                doseNotes = null
            ),
            onTap = {}
        )
    }
}