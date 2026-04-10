package com.example.rxtracker.ui.medication.add.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.rxtracker.data.models.Frequency
import com.example.rxtracker.data.models.FrequencyDetails
import com.example.rxtracker.ui.medication.add.AddMedicationsViewModel
import com.example.rxtracker.ui.medication.add.components.FrequencyOption
import com.example.rxtracker.ui.medication.add.components.dialogs.CyclicDialog
import com.example.rxtracker.ui.medication.add.components.dialogs.EveryXDaysDialog
import com.example.rxtracker.ui.medication.add.components.dialogs.EveryXHoursDialog
import com.example.rxtracker.ui.medication.add.components.dialogs.MultipleDailyDialog
import com.example.rxtracker.ui.medication.add.components.dialogs.WeekdaysDialog

@Composable
fun AddFrequencyScreen(
    viewModel: AddMedicationsViewModel,
    onContinue: () -> Unit,
) {
    val uiState = viewModel.uiState

    val med = uiState.medicationInfo
    val freq = uiState.frequency

    var showMultipleDailyDialog by remember { mutableStateOf(false) }
    var showEveryXHoursDialog by remember { mutableStateOf(false) }
    var showEveryXDaysDialog by remember { mutableStateOf(false) }
    var showSpecificWeekdaysDialog by remember { mutableStateOf(false) }
    var showCyclicDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = med.selectionSummary,
            style = MaterialTheme.typography.titleSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "How often do you take this medication?",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Frequency.entries.forEach { frequencyType ->
            FrequencyOption(
                label = frequencyType.label,
                selected = freq.type == frequencyType,
                onClick = {
                    when (frequencyType) {
                        Frequency.ONCE_DAILY -> viewModel.updateFrequency(
                            frequencyType,
                            FrequencyDetails.OnceDaily
                        )

                        Frequency.AS_NEEDED -> viewModel.updateFrequency(
                            frequencyType,
                            FrequencyDetails.AsNeeded
                        )

                        Frequency.MULTIPLE_DAILY -> showMultipleDailyDialog = true
                        Frequency.EVERY_X_HOURS -> showEveryXHoursDialog = true
                        Frequency.EVERY_X_DAYS -> showEveryXDaysDialog = true
                        Frequency.SPECIFIC_WEEKDAYS -> showSpecificWeekdaysDialog = true
                        Frequency.CYCLIC -> showCyclicDialog = true
                    }
                }
            )
            HorizontalDivider()
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onContinue,
            modifier = Modifier.fillMaxWidth(),
            enabled = freq.type != null && freq.details != null
        ) {
            Text("Continue")
        }
    }
    if (showMultipleDailyDialog) {
        MultipleDailyDialog(
            onDismiss = { showMultipleDailyDialog = false },
            onConfirm = { timesPerDay ->
                viewModel.updateFrequency(
                    type = Frequency.MULTIPLE_DAILY,
                    details = FrequencyDetails.MultipleTimes(timesPerDay)
                )
                showMultipleDailyDialog = false
            }
        )
    }
    if (showEveryXHoursDialog) {
        EveryXHoursDialog(
            onDismiss = { showEveryXHoursDialog = false },
            onConfirm = { hours ->
                viewModel.updateFrequency(
                    type = Frequency.EVERY_X_HOURS,
                    details = FrequencyDetails.EveryXHours(hours)
                )
                showEveryXHoursDialog = false
            }
        )
    }
    if (showEveryXDaysDialog) {
        EveryXDaysDialog(
            onDismiss = { showEveryXDaysDialog = false },
            onConfirm = { days ->
                viewModel.updateFrequency(
                    type = Frequency.EVERY_X_DAYS,
                    details = FrequencyDetails.EveryXDays(days)
                )
                showEveryXDaysDialog = false
            }
        )
    }
    if (showSpecificWeekdaysDialog) {
        WeekdaysDialog(
            onDismiss = { showSpecificWeekdaysDialog = false },
            onConfirm = { days ->
                viewModel.updateFrequency(
                    type = Frequency.SPECIFIC_WEEKDAYS,
                    details = FrequencyDetails.SpecificWeekdays(days)
                )
                showSpecificWeekdaysDialog = false
            }
        )
    }
    if (showCyclicDialog) {
        CyclicDialog(
            onDismiss = { showCyclicDialog = false },
            onConfirm = { intakeDays, pauseDays ->
                viewModel.updateFrequency(
                    type = Frequency.CYCLIC,
                    details = FrequencyDetails.Cyclic(intakeDays, pauseDays)
                )
                showCyclicDialog = false
            }
        )
    }
}


//@Suppress("ViewModelConstructorInComposable")
//@Preview(showBackground = true)
//@Composable
//fun AddFrequencyScreenPreview() {
//    RXTrackerTheme {
//        AddFrequencyScreen(
//            viewModel = AddMedicationsViewModel(),
//            onContinue = {}
//        )
//    }
//}