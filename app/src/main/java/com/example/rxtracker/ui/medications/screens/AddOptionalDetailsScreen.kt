package com.example.rxtracker.ui.medications.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.rxtracker.ui.medications.AddMedicationsViewModel
import com.example.rxtracker.ui.theme.RXTrackerTheme

@Composable
fun AddOptionalDetailsScreen(
    viewModel: AddMedicationsViewModel,
    onComplete: () -> Unit,
) {

}

@Suppress("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun AddOptionalDetailsScreenPreview() {
    RXTrackerTheme {
        AddOptionalDetailsScreen(
            viewModel = AddMedicationsViewModel(),
            onComplete = {}
        )
    }
}