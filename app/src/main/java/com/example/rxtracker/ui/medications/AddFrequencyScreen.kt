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
    val medicationData = viewModel.userMedication
    var selectedFrequency by remember { mutableStateOf<Frequency?>(null) }
    var frequencyDetails by remember { mutableStateOf<FrequencyDetails?>(null) }

    var showMultipleDailyDialog by remember { mutableStateOf(false) }
    var showEveryXHoursDialog by remember { mutableStateOf(false) }
    var showEveryXDaysDialog by remember { mutableStateOf(false) }
    var showSpecificWeekdaysDialog by remember { mutableStateOf(false) }
    var showCyclicDialog by remember { mutableStateOf(false) }

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
            onClick = {
                selectedFrequency?.let { type ->
                    frequencyDetails?.let { details ->
                        viewModel.updateFrequency(type, details)
                    }
                }
                onContinue()
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = selectedFrequency != null && frequencyDetails != null
        ) {
            Text("Continue")
        }
    }
    if (showMultipleDailyDialog) {
        MultipleDailyDialog(
            onDismiss = { showMultipleDailyDialog = false },
            onConfirm = { timesPerDay ->
                frequencyDetails = FrequencyDetails.MultipleTimes(timesPerDay)
                showMultipleDailyDialog = false
            }
        )
    }
    if (showEveryXHoursDialog) {
        EveryXHoursDialog(
            onDismiss = { showEveryXHoursDialog = false },
            onConfirm = { hours ->
                frequencyDetails = FrequencyDetails.EveryXHours(hours)
                showEveryXHoursDialog = false
            }
        )
    }
    if (showEveryXDaysDialog) {
        EveryXDaysDialog(
            onDismiss = { showEveryXDaysDialog = false },
            onConfirm = { days ->
                frequencyDetails = FrequencyDetails.EveryXDays(days)
                showEveryXDaysDialog = false
            }
        )
    }
    if (showSpecificWeekdaysDialog) {
        WeekdaysDialog(
            onDismiss = { showSpecificWeekdaysDialog = false },
            onConfirm = { days ->
                frequencyDetails = FrequencyDetails.SpecificWeekdays(days)
                showSpecificWeekdaysDialog = false
            }
        )
    }
    if (showCyclicDialog) {
        CyclicDialog(
            onDismiss = { showCyclicDialog = false },
            onConfirm = { intakeDays, pauseDays ->
                frequencyDetails = FrequencyDetails.Cyclic(intakeDays, pauseDays)
                showCyclicDialog = false
            }
        )
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