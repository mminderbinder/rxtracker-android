package com.example.rxtracker.ui.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.X
import com.example.rxtracker.ui.theme.RXTrackerTheme

@Composable
fun NotesDialog(
    initialNotes: String?,
    onConfirm: (String?) -> Unit,
    onDismiss: () -> Unit
) {
    var text by remember { mutableStateOf(initialNotes ?: "") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Notes",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    placeholder = { Text("Add a note...") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5,
                    trailingIcon = {
                        if (text.isNotBlank()) {
                            IconButton(onClick = { text = "" }) {
                                Icon(
                                    Lucide.X,
                                    contentDescription = "Clear notes",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Spacer(modifier = Modifier.width(8.dp))
                    FilledTonalButton(onClick = { onConfirm(text.ifBlank { null }) }) { Text("OK") }
                }
            }
        }
    }
}


@Preview
@Composable
fun NotesDialogPreview() {
    RXTrackerTheme {
        NotesDialog(
            initialNotes = "",
            onConfirm = {},
            onDismiss = {}
        )
    }
}

