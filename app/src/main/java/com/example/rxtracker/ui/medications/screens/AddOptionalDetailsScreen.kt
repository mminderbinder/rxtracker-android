package com.example.rxtracker.ui.medications.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rxtracker.ui.medications.AddMedicationsViewModel
import com.example.rxtracker.ui.medications.components.DetailRow
import com.example.rxtracker.ui.medications.components.ToggleRow
import com.example.rxtracker.ui.medications.components.dialogs.DateSelectionDialog
import com.example.rxtracker.ui.medications.components.dialogs.QuantityDialog
import com.example.rxtracker.ui.theme.RXTrackerTheme
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val dateFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOptionalDetailsScreen(
    viewModel: AddMedicationsViewModel,
    onComplete: () -> Unit,
) {
    val uiState = viewModel.uiState

    var showDatePicker by remember { mutableStateOf(false) }
    var showDoseQuantityDialog by remember { mutableStateOf(false) }
    var showRefillThresholdDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "${uiState.name} ${uiState.strength} ${uiState.form}",
            style = MaterialTheme.typography.titleSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Is there anything else?",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        HorizontalDivider()
        ToggleRow(
            label = "Reminders",
            checked = uiState.remindersEnabled,
            onCheckedChange = { viewModel.updateRemindersEnabled(it) }
        )

        HorizontalDivider()
        ToggleRow(
            label = "Refill reminder",
            checked = uiState.refillReminderEnabled,
            onCheckedChange = { viewModel.updateRefillReminderEnabled(it) }
        )

        AnimatedVisibility(visible = uiState.refillReminderEnabled) {
            Column {
                HorizontalDivider()
                DetailRow(
                    label = "Current dose count",
                    value = uiState.doseCount?.toString() ?: "30",
                    onClick = { showDoseQuantityDialog = true }
                )

                HorizontalDivider()
                DetailRow(
                    label = "Remind me when",
                    value = uiState.refillThreshold?.let { "$it left" }
                        ?: "10 left",
                    onClick = { showRefillThresholdDialog = true }
                )
            }
        }

        HorizontalDivider()
        DetailRow(
            label = "Treatment end date",
            value = uiState.endDate?.format(dateFormatter) ?: "Forever",
            onClick = { showDatePicker = true }
        )

        HorizontalDivider()

        OutlinedTextField(
            value = uiState.rxNumber ?: "",
            onValueChange = { viewModel.updateRxNumber(it.ifEmpty { null }) },
            label = { Text("Prescription number") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            singleLine = true
        )

        OutlinedTextField(
            value = uiState.instructions ?: "",
            onValueChange = { viewModel.updateInstructions(it.ifBlank { null }) },
            label = { Text("Instructions") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            minLines = 3,
            maxLines = 5
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { onComplete() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Complete")
        }
    }
    if (showDatePicker) {
        DateSelectionDialog(
            startDate = uiState.startDate,
            onConfirm = { millis ->
                viewModel.updateEndDate(
                    Instant.ofEpochMilli(millis)
                        .atZone(ZoneId.of("UTC"))
                        .toLocalDate() ?: LocalDate.now()
                )
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false }
        )
    }
    if (showDoseQuantityDialog) {
        QuantityDialog(
            initialQuantity = uiState.doseCount?.toDouble() ?: 30.0,
            title = "Doses Left",
            min = 0.5,
            max = 500.0,
            onDismiss = { showDoseQuantityDialog = false },
            onConfirm = { newQty ->
                viewModel.updateDoseCount(newQty.toInt())
                showDoseQuantityDialog = false
            }
        )
    }
    if (showRefillThresholdDialog) {
        QuantityDialog(
            initialQuantity = uiState.refillThreshold?.toDouble() ?: 10.0,
            title = "Doses left",
            min = 1.0,
            max = 30.0,
            onDismiss = { showRefillThresholdDialog = false },
            onConfirm = { newQty ->
                viewModel.updateRefillThreshold(newQty.toInt())
                showRefillThresholdDialog = false
            }
        )
    }
}

@Suppress("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun AddOptionalDetailsScreenPreview() {
    RXTrackerTheme {
        AddOptionalDetailsScreen(
            viewModel = AddMedicationsViewModel(),
            onComplete = {}
        )
    }
}