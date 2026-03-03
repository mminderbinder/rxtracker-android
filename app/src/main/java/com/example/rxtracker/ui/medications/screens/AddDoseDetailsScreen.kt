package com.example.rxtracker.ui.medications.screens

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
import com.example.rxtracker.ui.medications.components.dialogs.DateSelectionDialog
import com.example.rxtracker.ui.medications.components.dialogs.QuantityDialog
import com.example.rxtracker.ui.medications.components.dialogs.TimeSelectionDialog
import com.example.rxtracker.ui.medications.components.dialogs.doseLabel
import com.example.rxtracker.ui.theme.RXTrackerTheme
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")
private val dateFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDoseDetailsScreen(
    viewModel: AddMedicationsViewModel,
    onContinue: () -> Unit,
) {
    val uiState = viewModel.uiState

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var showQuantityDialog by remember { mutableStateOf(false) }

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
            text = "When do you start?",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        HorizontalDivider()
        DetailRow(
            label = "Start Date",
            value = if (uiState.startDate == LocalDate.now()) "Today" else uiState.startDate.format(
                dateFormatter
            ),
            onClick = { showDatePicker = true }
        )

        HorizontalDivider()
        DetailRow(
            label = "Earliest dose time",
            value = uiState.startTime.format(timeFormatter),
            onClick = { showTimePicker = true }
        )

        HorizontalDivider()
        DetailRow(
            label = "Initial dose quantity",
            value = doseLabel(uiState.quantity),
            onClick = { showQuantityDialog = true }
        )

        HorizontalDivider()

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { onContinue() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Continue")
        }
    }

    if (showTimePicker) {
        TimeSelectionDialog(
            startTime = uiState.startTime,
            onConfirm = { hour, minute ->
                viewModel.updateStartTime(LocalTime.of(hour, minute))
                showTimePicker = false
            },
            onDismiss = { showTimePicker = false }
        )
    }
    if (showDatePicker) {
        DateSelectionDialog(
            startDate = uiState.startDate,
            onConfirm = { millis ->
                viewModel.updateStartDate(
                    Instant.ofEpochMilli(millis)
                        .atZone(ZoneId.of("UTC"))
                        .toLocalDate(),
                )
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false }
        )
    }
    if (showQuantityDialog) {
        QuantityDialog(
            initialQuantity = uiState.quantity,
            title = "Dose Quantity",
            min = 0.5,
            max = 20.0,
            onDismiss = { showQuantityDialog = false },
            onConfirm = { newQty ->
                viewModel.updateQuantity(newQty)
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
            viewModel = AddMedicationsViewModel(),
            onContinue = {}
        )
    }
}