package com.example.rxtracker.ui.addmedication.components.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.composables.icons.lucide.CircleCheck
import com.composables.icons.lucide.Lucide
import com.example.rxtracker.ui.theme.RXTrackerTheme
import kotlinx.datetime.DayOfWeek

@Composable
fun WeekdaysDialog(
    onDismiss: () -> Unit,
    onConfirm: (selectedDays: Set<DayOfWeek>) -> Unit
) {
    var selectedDays by remember { mutableStateOf(setOf<DayOfWeek>()) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Select days",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DayOfWeek.entries.forEach { day ->
                        val isSelected = day in selectedDays
                        OutlinedCard(
                            onClick = {
                                selectedDays =
                                    if (isSelected) selectedDays - day else selectedDays + day
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = if (isSelected) {
                                CardDefaults.outlinedCardColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary,
                                )
                            } else {
                                CardDefaults.outlinedCardColors()
                            },
                            border = if (isSelected) {
                                CardDefaults.outlinedCardBorder()
                                    .copy(brush = SolidColor(MaterialTheme.colorScheme.primary))
                            } else {
                                CardDefaults.outlinedCardBorder()
                            }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = day.name,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                if (isSelected) {
                                    Icon(
                                        Lucide.CircleCheck,
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
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
                    FilledTonalButton(
                        onClick = { onConfirm(selectedDays) },
                        enabled = selectedDays.isNotEmpty()
                    ) {
                        Text("OK")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WeekdaysDialogPreview() {
    RXTrackerTheme {
        WeekdaysDialog(
            onDismiss = {},
            onConfirm = {}
        )
    }
}