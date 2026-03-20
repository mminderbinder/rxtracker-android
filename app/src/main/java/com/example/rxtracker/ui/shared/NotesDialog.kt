package com.example.rxtracker.ui.shared

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.rxtracker.ui.theme.RXTrackerTheme

@Composable
fun NotesDialog(
    initialNotes: String?,
    onConfirm: (String?) -> Unit,
    onDismiss: () -> Unit
) {
    var text by remember { mutableStateOf(initialNotes ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Notes") },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                placeholder = { Text("Add a note...") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5
            )
        },
        confirmButton = {
            FilledTonalButton(onClick = {onConfirm(text.ifBlank { null })}) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
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

