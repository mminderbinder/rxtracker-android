package com.example.rxtracker.ui.medications

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rxtracker.ui.medications.components.MedicationSearchBar
import com.example.rxtracker.ui.theme.RXTrackerTheme

@Composable
fun AddMedicationScreen(
    viewModel: MedicationsViewModel,
    onContinue: () -> Unit,
) {
    var medicationName by rememberSaveable { mutableStateOf("") }
    var medicationStrength by rememberSaveable { mutableStateOf("") }
    var medicationForm by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        MedicationSearchBar(
            onMedicationSelected = { medication ->
                medicationName = medication.brand
                medicationStrength = medication.amount
                medicationForm = medication.form
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = medicationName,
            onValueChange = { medicationName = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = medicationStrength,
            onValueChange = { medicationStrength = it },
            label = { Text("Strength") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = medicationForm,
            onValueChange = { medicationForm = it },
            label = { Text("Form") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                viewModel.updateMedicationInfo(
                    medicationName,
                    medicationStrength,
                    medicationForm
                )
                onContinue()
            },
            enabled = medicationName.isNotBlank() &&
                    medicationStrength.isNotBlank()
                    && medicationForm.isNotBlank()
        ) { Text("Continue") }
    }
}

@Suppress("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun AddMedicationScreenPreview() {
    RXTrackerTheme {
        AddMedicationScreen(
            viewModel = MedicationsViewModel(),
            onContinue = {}
        )
    }
}
