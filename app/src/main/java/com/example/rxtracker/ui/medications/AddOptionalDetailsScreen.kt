package com.example.rxtracker.ui.medications

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.rxtracker.ui.theme.RXTrackerTheme

@Composable
fun AddOptionalDetailsScreen(
    viewModel: MedicationsViewModel,
    onComplete: () -> Unit,
) {

}

@Suppress("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun AddOptionalDetailsScreenPreview() {
    RXTrackerTheme {
        AddOptionalDetailsScreen(
            viewModel = MedicationsViewModel(),
            onComplete = {}
        )
    }
}