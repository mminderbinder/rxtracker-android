package com.example.rxtracker.ui.medications.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Clock
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Trash2
import com.example.rxtracker.ui.medications.components.dialogs.QuantityDialog
import com.example.rxtracker.ui.medications.components.dialogs.doseLabel
import com.example.rxtracker.ui.theme.RXTrackerTheme
import java.time.LocalTime
import java.time.format.DateTimeFormatter

private val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTimeEntry(
    modifier: Modifier = Modifier,
    time: LocalTime,
    quantity: Double,
    onQuantityChange: (Double) -> Unit,
    onTimeChange: (LocalTime) -> Unit,
    onRemove: () -> Unit,
    showTrash: Boolean = true,
    showRemove: Boolean = true,
) {
    var showPicker by remember { mutableStateOf(false) }
    var showQuantityDialog by remember { mutableStateOf(false) }

    OutlinedCard(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Lucide.Clock,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = time.format(timeFormatter),
                style = MaterialTheme.typography.bodyLarge.copy(
                    textDecoration = TextDecoration.Underline
                ),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .weight(1f)
                    .clickable { showPicker = true }
            )
            Text(
                text = doseLabel(quantity),
                style = MaterialTheme.typography.bodyMedium.copy(
                    textDecoration = TextDecoration.Underline
                ),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { showQuantityDialog = true }
            )
            if (showTrash) {
                IconButton(
                    onClick = onRemove,
                    enabled = showRemove
                ) {
                    Icon(
                        imageVector = Lucide.Trash2,
                        contentDescription = "Remove time",
                        tint = if (showRemove) MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                    )
                }
            }
        }
    }
    if (showQuantityDialog) {
        QuantityDialog(
            initialQuantity = quantity,
            title = "Dose Quantity",
            min = 0.5,
            max = 20.0,
            onDismiss = { showQuantityDialog = false },
            onConfirm = { newQty ->
                onQuantityChange(newQty)
                showQuantityDialog = false
            }
        )
    }
    if (showPicker) {
        val pickerState = rememberTimePickerState(
            initialHour = time.hour,
            initialMinute = time.minute,
            is24Hour = false
        )
        AlertDialog(
            onDismissRequest = { showPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    onTimeChange(LocalTime.of(pickerState.hour, pickerState.minute))
                    showPicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showPicker = false }) { Text("Cancel") }
            },
            text = { TimePicker(state = pickerState) }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AddTimesEntryPreview() {
    RXTrackerTheme {
        AddTimeEntry(
            time = LocalTime.now(),
            quantity = 2.0,
            onTimeChange = {},
            onRemove = {},
            onQuantityChange = {})
    }
}