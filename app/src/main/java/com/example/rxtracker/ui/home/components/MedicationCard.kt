package com.example.rxtracker.ui.home.components

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.rxtracker.ui.home.HomeViewModel
import com.example.rxtracker.ui.theme.RXTrackerTheme

@Composable
fun MedicationCard(
    viewModel: HomeViewModel
) {

}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun MedicationCardPreview() {
    RXTrackerTheme {
        MedicationCard(
            viewModel = HomeViewModel()
        )
    }
}