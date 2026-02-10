package com.example.rxtracker.ui.medications

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rxtracker.data.models.Frequency
import com.example.rxtracker.data.models.FrequencyDetails
import com.example.rxtracker.ui.medications.components.FrequencyOption
import com.example.rxtracker.ui.medications.components.dialogs.CyclicDialog
import com.example.rxtracker.ui.medications.components.dialogs.EveryXDaysDialog
import com.example.rxtracker.ui.medications.components.dialogs.EveryXHoursDialog
import com.example.rxtracker.ui.medications.components.dialogs.MultipleDailyDialog
import com.example.rxtracker.ui.medications.components.dialogs.WeekdaysDialog
import com.example.rxtracker.ui.theme.RXTrackerTheme

@Composable
fun AddFrequencyScreen(
    viewModel: MedicationsViewModel,
    onContinue: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val medicationData = viewModel.medicationData
    var selectedFrequency by remember { mutableStateOf<Frequency?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var frequencyDetails by remember { mutableStateOf<FrequencyDetails?>(null) }

    LaunchedEffect(selectedFrequency) {
        showDialog = when (selectedFrequency) {
            Frequency.MULTIPLE_DAILY,
            Frequency.EVERY_X_HOURS,
            Frequency.EVERY_X_DAYS,
            Frequency.SPECIFIC_WEEKDAYS,
            Frequency.CYCLIC -> true
            else -> false
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Medication: ${medicationData.name}")
        Text("Strength: ${medicationData.strength}")
        Text("Form: ${medicationData.form}")

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "How often do you take this medication?",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Frequency.entries.forEach { frequencyType ->
            FrequencyOption(
                label = frequencyType.label,
                selected = selectedFrequency == frequencyType,
                onClick = {
                    selectedFrequency = frequencyType
                    when (frequencyType) {
                        Frequency.ONCE_DAILY -> frequencyDetails = FrequencyDetails.OnceDaily
                        Frequency.AS_NEEDED -> frequencyDetails = FrequencyDetails.AsNeeded
                        else -> {}
                    }
                }
            )
            HorizontalDivider()
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                selectedFrequency?.let {

                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = selectedFrequency != null && frequencyDetails != null
        ) {
            Text("Continue")
        }
    }
    if (showDialog) {
        when (selectedFrequency) {
            Frequency.MULTIPLE_DAILY -> {
                MultipleDailyDialog(
                    onDismiss = {
                        showDialog = false
                        selectedFrequency = null
                    },
                    onConfirm = { timesPerDay ->
                        frequencyDetails = FrequencyDetails.MultipleTimes(timesPerDay)
                        showDialog = false
                    }
                )
            }

            Frequency.EVERY_X_HOURS -> {
                EveryXHoursDialog(
                    onDismiss = {
                        showDialog = false
                        selectedFrequency = null
                    },
                    onConfirm = { hours ->
                        frequencyDetails = FrequencyDetails.EveryXHours(hours)
                        showDialog = false
                    }
                )
            }

            Frequency.EVERY_X_DAYS -> {
                EveryXDaysDialog(
                    onDismiss = {
                        showDialog = false
                        selectedFrequency = null
                    },
                    onConfirm = { days ->
                        frequencyDetails = FrequencyDetails.EveryXDays(days)
                        showDialog = false
                    }
                )
            }

            Frequency.SPECIFIC_WEEKDAYS -> {
                WeekdaysDialog(
                    onDismiss = {
                        showDialog = false
                        selectedFrequency = null
                    },
                    onConfirm = { days ->
                        frequencyDetails = FrequencyDetails.SpecificWeekdays(days)
                        showDialog = false
                    }
                )
            }
            Frequency.CYCLIC -> {
                CyclicDialog(
                    onDismiss = {
                        showDialog = false
                        selectedFrequency = null
                    },
                    onConfirm = { intakeDays, pauseDays ->
                        frequencyDetails = FrequencyDetails.Cyclic(intakeDays, pauseDays)
                        showDialog = false
                    }
                )
            }
            else -> {}
        }
    }
}


@Suppress("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun AddFrequencyScreenPreview() {
    RXTrackerTheme {
        AddFrequencyScreen(
            viewModel = MedicationsViewModel(),
            onContinue = {}
        )
    }
}