package com.example.rxtracker.ui.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.composeunstyled.Text
import com.example.rxtracker.data.models.ScheduledDoseWithMedication
import com.example.rxtracker.ui.theme.RXTrackerTheme
import java.time.LocalTime
import java.time.format.DateTimeFormatter

private val timeFormatter = DateTimeFormatter.ofPattern("h:mm a")

@Composable
fun DoseTimeHeader(
    time: LocalTime,
    doses: List<ScheduledDoseWithMedication>,
    onSelectAll: (List<ScheduledDoseWithMedication>) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = time.format(timeFormatter),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        if (doses.size > 1) {
            TextButton(onClick = {onSelectAll(doses)}) {
                Text(
                    text = "SELECT ALL",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun DoseTimeHeaderPreview() {
    RXTrackerTheme {
        DoseTimeHeader(
            time = LocalTime.now(),
            doses = emptyList(),
            onSelectAll = {}
        )
    }
}