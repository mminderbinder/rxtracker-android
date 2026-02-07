package com.example.rxtracker.ui.medications

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rxtracker.ui.theme.RXTrackerTheme

@Composable
fun AddFrequencyScreen(
    viewModel: MedicationsViewModel,
    onContinue: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val medicationData = viewModel.medicationData

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Medication: ${medicationData.name}")
        Text("Strength: ${medicationData.strength}")
        Text("Form: ${medicationData.form}")
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