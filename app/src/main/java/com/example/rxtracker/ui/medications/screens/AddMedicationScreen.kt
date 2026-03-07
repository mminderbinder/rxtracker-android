package com.example.rxtracker.ui.medications.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.rxtracker.data.repository.PrescribableRepository
import com.example.rxtracker.ui.medications.AddMedicationsViewModel
import com.example.rxtracker.ui.medications.MedicationSearchViewModel
import com.example.rxtracker.ui.medications.components.MedicationSearchBar
import com.example.rxtracker.ui.theme.RXTrackerTheme

@Composable
fun AddMedicationScreen(
    searchViewModel: MedicationSearchViewModel = hiltViewModel(),
    addViewModel: AddMedicationsViewModel,
    onContinue: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        MedicationSearchBar(
            viewModel = searchViewModel,
            onMedicationSelected = { medication ->
                addViewModel.updateMedicationInfo(
                    name = medication.brand,
                    strength = medication.amount,
                    form = medication.form
                )
                onContinue()
            },
            onManualEntry = { typedName ->
                addViewModel.updateMedicationInfo(name = typedName, strength = "", form = "")
                onContinue()
            }
        )
    }
}