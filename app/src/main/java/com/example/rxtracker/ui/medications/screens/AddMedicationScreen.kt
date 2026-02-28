package com.example.rxtracker.ui.medications.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rxtracker.ui.medications.AddMedicationsViewModel
import com.example.rxtracker.ui.medications.components.MedicationSearchBar
import com.example.rxtracker.ui.theme.RXTrackerTheme

@Composable
fun AddMedicationScreen(
    viewModel: AddMedicationsViewModel,
    onContinue: () -> Unit,
) {
    val uiState = viewModel.uiState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        MedicationSearchBar(
            onMedicationSelected = { medication ->
                viewModel.updateMedicationInfo(
                    name = medication.brand,
                    strength = medication.amount,
                    form = medication.form
                )
            }
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.name,
            onValueChange = {
                viewModel.updateMedicationInfo(
                    name = it,
                    strength = uiState.strength,
                    form = uiState.form
                )
            },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = uiState.strength,
            onValueChange = {
                viewModel.updateMedicationInfo(
                    name = uiState.name,
                    strength = it,
                    form = uiState.form
                )
            },
            label = { Text("Strength") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = uiState.form,
            onValueChange = {
                viewModel.updateMedicationInfo(
                    name = uiState.name,
                    strength = uiState.strength,
                    form = it
                )
            },
            label = { Text("Form") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onContinue,
            enabled = uiState.name.isNotBlank() &&
                    uiState.strength.isNotBlank()
                    && uiState.form.isNotBlank()
        ) { Text("Continue") }
    }
}

@Suppress("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun AddMedicationScreenPreview() {
    RXTrackerTheme {
        AddMedicationScreen(
            viewModel = AddMedicationsViewModel(),
            onContinue = {}
        )
    }
}
