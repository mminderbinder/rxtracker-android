package com.example.rxtracker.ui.medication.add.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.rxtracker.R
import com.example.rxtracker.ui.medication.add.AddMedicationsViewModel
import com.example.rxtracker.ui.medication.add.components.DetailRow
import com.example.rxtracker.ui.shared.DateSelectionDialog
import com.example.rxtracker.ui.shared.QuantityDialog
import com.example.rxtracker.ui.shared.TimeSelectionDialog
import com.example.rxtracker.utils.formatQuantity
import com.example.rxtracker.utils.getFormattedDate
import com.example.rxtracker.utils.getFormattedTime
import com.example.rxtracker.utils.today
import kotlinx.datetime.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDoseDetailsScreen(
    viewModel: AddMedicationsViewModel,
    onContinue: () -> Unit,
) {
    val uiState = viewModel.uiState
    val med = uiState.medicationInfo
    val dose = uiState.doseDetails

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var showQuantityDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = med.selectionSummary,
            style = MaterialTheme.typography.titleSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "When do you start?",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        HorizontalDivider()
        DetailRow(
            label = "Start Date",
            value = if (dose.startDate == today()) "Today" else getFormattedDate(dose.startDate),
            onClick = { showDatePicker = true }
        )

        HorizontalDivider()
        DetailRow(
            label = "Earliest dose time",
            value = getFormattedTime(dose.startTime),
            onClick = { showTimePicker = true }
        )

        HorizontalDivider()
        DetailRow(
            label = "Dose Quantity",
            value = formatQuantity(dose.quantity, med.form),
            onClick = { showQuantityDialog = true }
        )
        HorizontalDivider()

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            painter = painterResource(id = R.drawable.rx),
            contentDescription = null,
            modifier = Modifier
                .size(120.dp)
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { onContinue() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Continue")
        }
    }

    if (showTimePicker) {
        TimeSelectionDialog(
            startTime = dose.startTime,
            onConfirm = { hour, minute ->
                viewModel.updateDoseDetails(time = LocalTime(hour, minute))
                showTimePicker = false
            },
            onDismiss = { showTimePicker = false }
        )
    }
    if (showDatePicker) {
        DateSelectionDialog(
            startDate = dose.startDate,
            onConfirm = { date ->
                viewModel.updateStartDate(date)
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false }
        )
    }
    if (showQuantityDialog) {
        QuantityDialog(
            initialQuantity = dose.quantity,
            title = "Select quantity",
            min = 0.25,
            max = 999.0,
            onDismiss = { showQuantityDialog = false },
            onConfirm = { newQty ->
                viewModel.updateDoseDetails(quantity = newQty)
                showQuantityDialog = false
            }
        )
    }
}

//@Suppress("ViewModelConstructorInComposable")
//@Preview(showBackground = true)
//@Composable
//fun AddDoseDetailsScreenPreview() {
//    RXTrackerTheme {
//        AddDoseDetailsScreen(
//            viewModel = AddMedicationsViewModel(),
//            onContinue = {}
//        )
//    }
//}