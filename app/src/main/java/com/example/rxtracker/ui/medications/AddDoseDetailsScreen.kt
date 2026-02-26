package com.example.rxtracker.ui.medications

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rxtracker.ui.medications.components.dialogs.QuantityDialog
import com.example.rxtracker.ui.medications.components.dialogs.pillLabel
import com.example.rxtracker.ui.theme.RXTrackerTheme
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")
private val dateFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy")

@Composable
private fun DetailRow(
    label: String,
    value: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge.copy(
                textDecoration = TextDecoration.Underline
            ),
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDoseDetailsScreen(
    viewModel: MedicationsViewModel,
    onContinue: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val medicationData = viewModel.userMedication

    var selectedDate by remember { mutableStateOf(medicationData.startDate) }
    var selectedTime by remember { mutableStateOf(LocalTime.of(8, 0)) }
    var quantity by remember { mutableDoubleStateOf(1.0) }

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var showQuantityDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "${medicationData.name} ${medicationData.strength} ${medicationData.form}",
            style = MaterialTheme.typography.titleSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "When do you start?",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        HorizontalDivider()
        DetailRow(
            label = "Start Date",
            value = selectedDate.format(dateFormatter),
            onClick = { showDatePicker = true }
        )

        HorizontalDivider()
        DetailRow(
            label = "Earliest dose time",
            value = selectedTime.format(timeFormatter),
            onClick = { showTimePicker = true }
        )

        HorizontalDivider()
        DetailRow(
            label = "Dose quantity",
            value = pillLabel(quantity),
            onClick = { showQuantityDialog = true }
        )

        HorizontalDivider()

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                viewModel.updateDoseDetails(selectedDate, selectedTime, quantity)
                onContinue()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Continue")
        }
    }

    if (showTimePicker) {
        val pickerState = rememberTimePickerState(
            initialHour = selectedTime.hour,
            initialMinute = selectedTime.minute,
            is24Hour = false
        )
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    selectedTime = LocalTime.of(pickerState.hour, pickerState.minute)
                    showTimePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) { Text("Cancel") }
            },
            text = { TimePicker(state = pickerState) }
        )
    }
    if (showDatePicker) {
        val pickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
        )
        AlertDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    pickerState.selectedDateMillis?.let { millis ->
                        selectedDate = Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                    }
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            },
            text = { DatePicker(state = pickerState) }
        )
    }
    if (showQuantityDialog) {
        QuantityDialog(
            initialQuantity = quantity,
            onDismiss = { showQuantityDialog = false },
            onConfirm = { newQty ->
                quantity = newQty
                showQuantityDialog = false
            }
        )
    }
}

@Suppress("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun AddDoseDetailsScreenPreview() {
    RXTrackerTheme {
        AddDoseDetailsScreen(
            viewModel = MedicationsViewModel(),
            onContinue = {}
        )
    }
}