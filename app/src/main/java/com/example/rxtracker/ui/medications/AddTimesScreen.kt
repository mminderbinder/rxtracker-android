package com.example.rxtracker.ui.medications

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rxtracker.ui.theme.RXTrackerTheme

@Composable
fun AddTimesScreen(
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
        Text(
            text = "${medicationData.name} ${medicationData.strength} ${medicationData.form}",
            style = MaterialTheme.typography.titleSmall
        )

        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Add Time...",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

@Suppress("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun AddTimesScreenPreview() {
    RXTrackerTheme {
        AddTimesScreen(
            viewModel = MedicationsViewModel(),
            onContinue = {},
        )
    }
}