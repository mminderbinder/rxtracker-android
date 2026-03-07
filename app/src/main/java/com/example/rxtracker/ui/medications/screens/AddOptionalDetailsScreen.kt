package com.example.rxtracker.ui.medications.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.example.rxtracker.ui.medications.components.dialogs.QuantityDialog
import com.example.rxtracker.ui.theme.RXTrackerTheme
import java.time.format.DateTimeFormatter

private val dateFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOptionalDetailsScreen(
    viewModel: AddMedicationsViewModel,
    onComplete: () -> Unit,
) {
    val uiState = viewModel.uiState
    val med = uiState.medicationInfo
    val opt = uiState.optionalDetails

    var showDoseQuantityDialog by remember { mutableStateOf(false) }
    var showRefillThresholdDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                text = med.selectionSummary,
                style = MaterialTheme.typography.titleSmall
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Is there anything else?",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Text(
                text = "Notifications",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            HorizontalDivider()
            ToggleRow(
                label = "Reminders",
                checked = opt.remindersEnabled,
                onCheckedChange = { viewModel.updateRemindersEnabled(it) }
            )
            HorizontalDivider()
            ToggleRow(
                label = "Refill reminder",
                checked = opt.refillReminderEnabled,
                onCheckedChange = { viewModel.updateRefillReminderEnabled(it) }
            )

            AnimatedVisibility(visible = opt.refillReminderEnabled) {
                Column {
                    HorizontalDivider()
                    DetailRow(
                        label = "Current dose count",
                        value = opt.doseCount?.toString() ?: "30",
                        onClick = { showDoseQuantityDialog = true }
                    )
                    HorizontalDivider()
                    DetailRow(
                        label = "Remind me when",
                        value = opt.refillThreshold?.let { "$it left" } ?: "10 left",
                        onClick = { showRefillThresholdDialog = true }
                    )
                }
            }

            HorizontalDivider()

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Notes",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            OutlinedTextField(
                value = opt.instructions ?: "",
                onValueChange = { viewModel.updateInstructions(it.ifBlank { null }) },
                label = { Text("Instructions") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        Button(
            onClick = { onComplete() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text("Complete")
        }
    }

    if (showDoseQuantityDialog) {
        QuantityDialog(
            initialQuantity = opt.doseCount?.toDouble() ?: 30.0,
            title = "Doses Left",
            min = 0.25,
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
            initialQuantity = opt.refillThreshold?.toDouble() ?: 10.0,
            title = "Select quantity",
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