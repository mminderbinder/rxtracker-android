package com.example.rxtracker.ui.medications.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rxtracker.data.models.IntakeTime
import com.example.rxtracker.ui.medications.AddMedicationsUiState
import com.example.rxtracker.ui.medications.AddMedicationsViewModel
import com.example.rxtracker.ui.medications.components.DetailRow
import com.example.rxtracker.ui.medications.components.ToggleRow
import com.example.rxtracker.ui.medications.components.dialogs.QuantityDialog
import com.example.rxtracker.ui.theme.RXTrackerTheme
import com.example.rxtracker.utils.formLabel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOptionalDetailsScreen(
    viewModel: AddMedicationsViewModel,
    onComplete: () -> Unit,
) {
    val uiState = viewModel.uiState

    AddOptionalDetailsContent(
        uiState = uiState,
        onComplete = { viewModel.save(onComplete) },
        onUpdateReminders = { viewModel.updateRemindersEnabled(it) },
        onUpdateRefillReminder = { viewModel.updateRefillReminderEnabled(it) },
        onUpdateDoseCount = { viewModel.updateDoseCount(it) },
        onUpdateRefillThreshold = { viewModel.updateRefillThreshold(it) },
        onUpdateIntakeTime = { viewModel.updateIntakeTime(it) },
        onUpdateInstructions = { viewModel.updateInstructions(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOptionalDetailsContent(
    uiState: AddMedicationsUiState,
    onComplete: () -> Unit,
    onUpdateReminders: (Boolean) -> Unit,
    onUpdateRefillReminder: (Boolean) -> Unit,
    onUpdateDoseCount: (Int) -> Unit,
    onUpdateRefillThreshold: (Int) -> Unit,
    onUpdateIntakeTime: (IntakeTime?) -> Unit,
    onUpdateInstructions: (String?) -> Unit,
) {
    val med = uiState.medicationInfo
    val opt = uiState.optionalDetails

    var showDoseQuantityDialog by remember { mutableStateOf(false) }
    var showRefillThresholdDialog by remember { mutableStateOf(false) }
    var intakeDropdownExpanded by remember { mutableStateOf(false) }

    val effectiveDoseCount = opt.totalQuantity
    val effectiveRefillThreshold = opt.refillThreshold

    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(16.dp)
                    .imePadding()
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
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                HorizontalDivider()
                ToggleRow(
                    label = "Reminders",
                    checked = opt.remindersEnabled,
                    onCheckedChange = onUpdateReminders
                )
                HorizontalDivider()
                ToggleRow(
                    label = "Refill reminder",
                    checked = opt.refillReminderEnabled,
                    onCheckedChange = onUpdateRefillReminder
                )

                AnimatedVisibility(visible = opt.refillReminderEnabled) {
                    Column {
                        HorizontalDivider()
                        DetailRow(
                            label = "Current count",
                            value = effectiveDoseCount?.toString() ?: "Not set",
                            onClick = { showDoseQuantityDialog = true }
                        )
                        HorizontalDivider()
                        DetailRow(
                            label = "Remind me when",
                            value = effectiveRefillThreshold?.let { "$it left" } ?: "Not set",
                            onClick = { showRefillThresholdDialog = true }
                        )
                    }
                }

                HorizontalDivider()

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Intake Timing",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                ExposedDropdownMenuBox(
                    expanded = intakeDropdownExpanded,
                    onExpandedChange = { intakeDropdownExpanded = it }
                ) {
                    OutlinedTextField(
                        value = opt.intakeTime?.label ?: "Not specified",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = intakeDropdownExpanded)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                    )
                    ExposedDropdownMenu(
                        expanded = intakeDropdownExpanded,
                        onDismissRequest = { intakeDropdownExpanded = false }
                    ) {
                        IntakeTime.entries.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option.label) },
                                onClick = {
                                    onUpdateIntakeTime(option)
                                    intakeDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Notes",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = opt.instructions ?: "",
                    onValueChange = { onUpdateInstructions(it.ifBlank { null }) },
                    label = { Text("Additional notes") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged {
                            if (it.isFocused) {
                                coroutineScope.launch {
                                    delay(300)
                                    scrollState.animateScrollTo(scrollState.maxValue)
                                }
                            }
                        },
                    minLines = 3,
                    maxLines = 5
                )
            }

            Button(
                onClick = onComplete,
                enabled = !uiState.isSaving,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text("Complete")
            }
        }

        if (uiState.isSaving) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.32f))
                    .clickable(enabled = false) { },
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }

    if (showDoseQuantityDialog) {
        QuantityDialog(
            initialQuantity = (effectiveDoseCount ?: 30).toDouble(),
            title = "${formLabel(med.form).replaceFirstChar(Char::titlecase)} left",
            min = 0.25,
            max = 500.0,
            onDismiss = { showDoseQuantityDialog = false },
            onConfirm = { newQty ->
                onUpdateDoseCount(newQty.toInt())
                showDoseQuantityDialog = false
            }
        )
    }

    if (showRefillThresholdDialog) {
        QuantityDialog(
            initialQuantity = (effectiveRefillThreshold ?: 10).toDouble(),
            title = "Refill Reminder At",
            min = 1.0,
            max = 30.0,
            onDismiss = { showRefillThresholdDialog = false },
            onConfirm = { newQty ->
                onUpdateRefillThreshold(newQty.toInt())
                showRefillThresholdDialog = false
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AddOptionalDetailsScreenPreview() {
    RXTrackerTheme {
        AddOptionalDetailsContent(
            uiState = AddMedicationsUiState(),
            onComplete = {},
            onUpdateReminders = {},
            onUpdateRefillReminder = {},
            onUpdateDoseCount = {},
            onUpdateRefillThreshold = {},
            onUpdateIntakeTime = {},
            onUpdateInstructions = {}
        )
    }
}