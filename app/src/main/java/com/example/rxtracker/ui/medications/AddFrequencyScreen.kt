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
import com.example.rxtracker.ui.medications.components.FrequencyOption
import com.example.rxtracker.ui.theme.RXTrackerTheme

enum class FrequencyType(val label: String) {
    ONCE_DAILY("Once a day"),
    MULTIPLE_DAILY("Multiple times a day"),
    AS_NEEDED("As needed"),
    EVERY_X_HOURS("Every X hours"),
    EVERY_X_DAYS("Every X days"),
    SPECIFIC_WEEKDAYS("Specific weekdays"),
    CYCLIC("Cyclic (intake days, rest days)")
}

@Composable
fun AddFrequencyScreen(
    viewModel: MedicationsViewModel,
    onContinue: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val medicationData = viewModel.medicationData
    var selectedFrequency by remember { mutableStateOf<FrequencyType?>(null) }

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

        FrequencyType.entries.forEach { frequencyType ->
            FrequencyOption(
                label = frequencyType.label,
                selected = selectedFrequency == frequencyType,
                onClick = { selectedFrequency = frequencyType}
            )
            HorizontalDivider()
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick =  {
                // TODO: handle navigation
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = false
        ) {
            Text("Continue")
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