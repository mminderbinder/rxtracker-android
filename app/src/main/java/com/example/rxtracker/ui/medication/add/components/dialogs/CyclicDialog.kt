package com.example.rxtracker.ui.medication.add.components.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.rxtracker.ui.medication.add.components.DialogWheelPicker
import com.example.rxtracker.ui.theme.RXTrackerTheme
import com.swmansion.kmpwheelpicker.rememberWheelPickerState

@Composable
fun CyclicDialog(
    onDismiss: () -> Unit,
    onConfirm: (intakeDays: Int, pauseDays: Int) -> Unit
) {
    val intakeState = rememberWheelPickerState(
        itemCount = 30,
        initialIndex = 6
    )
    val pauseState = rememberWheelPickerState(
        itemCount = 30,
        initialIndex = 2
    )

    Dialog(onDismissRequest = onDismiss) {
        OutlinedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Intake & Pause Cycle",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            "Intake days",
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Box(
                            modifier = Modifier.height(120.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.8f)
                                    .height(36.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.primaryContainer
                                            .copy(alpha = 0.15f),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                            )
                            DialogWheelPicker(
                                state = intakeState,
                                friction = 4f,
                                itemLabel = { index ->
                                    val days = index + 1
                                    "$days ${if (days == 1) "day" else "days"}"
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            "Pause days",
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Box(
                            modifier = Modifier.height(120.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.8f)
                                    .height(36.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.primaryContainer
                                            .copy(alpha = 0.15f),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                            )
                            DialogWheelPicker(
                                state = pauseState,
                                friction = 4f,
                                itemLabel = { index ->
                                    val days = index + 1
                                    "$days ${if (days == 1) "day" else "days"}"
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    FilledTonalButton(onClick = {
                        onConfirm(intakeState.index + 1, pauseState.index + 1)
                    }) {
                        Text("OK")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CyclicDialogPreview() {
    RXTrackerTheme {
        CyclicDialog(
            onDismiss = {},
            onConfirm = { _, _ -> }
        )
    }
}