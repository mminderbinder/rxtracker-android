package com.example.rxtracker.ui.medications

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.rxtracker.data.Medication
import com.example.rxtracker.ui.medications.components.MedicationSearchBar
import com.example.rxtracker.ui.theme.RXTrackerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMedicationScreen(
    navController: NavController
) {
    var selectedMedication by remember { mutableStateOf<Medication?>(null) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Medication") },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigateUp() }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            MedicationSearchBar(
                onMedicationSelected = { medication ->
                    selectedMedication = medication
                }
            )
            Spacer(modifier = Modifier.height(16.dp))

            selectedMedication?.let { medication ->
                Text("Selected: ${medication.name}")
                Text("Brand: ${medication.brand}")
                Text("Amount: ${medication.amount}")
                Text("Form: ${medication.form}")
            }
        }
    }
}


@Preview
@Composable
fun AddMedicationScreenPreview() {
    RXTrackerTheme {
        AddMedicationScreen(navController = rememberNavController())
    }
}
