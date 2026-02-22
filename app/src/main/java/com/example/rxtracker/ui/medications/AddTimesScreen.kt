package com.example.rxtracker.ui.medications

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rxtracker.ui.medications.components.AddTimeEntry
import com.example.rxtracker.ui.theme.RXTrackerTheme
import java.time.LocalTime

@Composable
fun AddTimesScreen(
    viewModel: MedicationsViewModel,
    onContinue: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val medicationData = viewModel.medicationEntity
    var times by remember { mutableStateOf(listOf(LocalTime.of(8, 0))) }

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
            text = "When do you take this medication?",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(times) { index, time ->
                AddTimeEntry(
                    time = time,
                    onTimeChange = { newTime ->
                        times = times.toMutableList().also { it[index] = newTime }
                    },
                    onRemove = {
                        times = times.toMutableList().also { it.removeAt(index) }
                    },
                    showRemove = times.size > 1
                )
            }
            item {
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = { times = times + LocalTime.of(8, 0) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("+ Add Another time")
                }
            }
        }
        Button(
            onClick = {
                // TODO: update dose times
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = times.isNotEmpty()
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
            viewModel = MedicationsViewModel(),
            onContinue = {},
        )
    }
}