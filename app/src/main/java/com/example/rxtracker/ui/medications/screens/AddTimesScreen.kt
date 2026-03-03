package com.example.rxtracker.ui.medications.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rxtracker.data.models.DoseTime
import com.example.rxtracker.data.models.FrequencyDetails
import com.example.rxtracker.ui.medications.AddMedicationsViewModel
import com.example.rxtracker.ui.medications.components.AddTimeEntry
import com.example.rxtracker.ui.theme.RXTrackerTheme

@Composable
fun AddTimesScreen(
    viewModel: AddMedicationsViewModel,
    snackbarHostState: SnackbarHostState,
    onContinue: () -> Unit,
) {
    val uiState = viewModel.uiState
    var duplicateTimeError by remember { mutableStateOf(false) }

    val isFixed = remember { viewModel.isFixedSchedule() }

    val intervalHours = remember {
        when (val details = uiState.frequencyDetails) {
            is FrequencyDetails.EveryXHours -> details.hours
            else -> 1
        }
    }

    val doseTimes = uiState.doseTimes.ifEmpty {
        viewModel.generateInitialTimes().also { viewModel.updateDoseTimes(it) }
    }

    val canAddTime = remember(doseTimes) {
        val totalHoursFromStart = intervalHours * doseTimes.size
        totalHoursFromStart < 24
    }

    if (duplicateTimeError) {
        LaunchedEffect(duplicateTimeError) {
            snackbarHostState.showSnackbar("Cannot add the same time")
            duplicateTimeError = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "${uiState.name} ${uiState.strength} ${uiState.form}",
            style = MaterialTheme.typography.titleSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "When do you take this medication?",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(doseTimes) { index, doseTime ->
                AddTimeEntry(
                    time = doseTime.time,
                    quantity = doseTime.quantity,
                    onTimeChange = { newTime ->
                        val isDuplicate = doseTimes
                            .filterIndexed { i, _ -> i != index }
                            .any { it.time == newTime }

                        if (!isDuplicate) {
                            viewModel.updateDoseTimes(
                                doseTimes.toMutableList()
                                    .also { it[index] = it[index].copy(time = newTime) }
                                    .sortedBy { it.time })
                        } else {
                            duplicateTimeError = true
                        }
                    },
                    onQuantityChange = { newQty ->
                        viewModel.updateDoseTimes(
                            doseTimes.toMutableList()
                                .also { it[index] = it[index].copy(quantity = newQty) })
                    },
                    onRemove = {
                        viewModel.updateDoseTimes(
                            doseTimes.toMutableList()
                                .also { it.removeAt(index) })
                    },
                    showTrash = !isFixed,
                    showRemove = doseTimes.size > 1
                )
            }
            if (!isFixed) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = {
                            val nextOffset = intervalHours * doseTimes.size
                            viewModel.updateDoseTimes(
                                doseTimes + DoseTime(
                                    time = uiState.startTime.plusHours(nextOffset.toLong()),
                                    quantity = uiState.quantity
                                )
                            )
                            for (dose in doseTimes) {
                                Log.d(
                                    "AddTimesScreen",
                                    "Start time: ${uiState.startTime}, Dose: ${dose.time}"
                                )
                            }
                        },
                        enabled = canAddTime,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("+ Add Another time")
                    }
                }
            }
        }
        Button(
            onClick = {
                onContinue()
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = doseTimes.isNotEmpty()
        ) {
            Text("Continue")
        }
    }
}

@Suppress("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun AddTimesScreenPreview() {
    RXTrackerTheme {
        AddTimesScreen(
            viewModel = AddMedicationsViewModel(),
            onContinue = {},
            snackbarHostState = SnackbarHostState()
        )
    }
}