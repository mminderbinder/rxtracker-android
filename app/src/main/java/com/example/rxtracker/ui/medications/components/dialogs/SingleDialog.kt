package com.example.rxtracker.ui.medications.components.dialogs

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.rxtracker.ui.medications.components.DialogWheelPicker
import com.example.rxtracker.ui.theme.RXTrackerTheme
import com.swmansion.kmpwheelpicker.rememberWheelPickerState

@Composable
fun SingleDialog(
    title: String,
    itemCount: Int,
    initialIndex: Int,
    itemLabel: (Int) -> String,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    val state = rememberWheelPickerState(
        itemCount = itemCount,
        initialIndex = initialIndex
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                DialogWheelPicker(
                    state = state,
                    itemLabel = itemLabel,
                    modifier = Modifier.height(150.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(onClick = { onConfirm(state.index) }) {
                        Text("OK")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SingleDialogPreview() {
    RXTrackerTheme {
        SingleDialog(
            title = "Times per day",
            itemCount = 7,
            initialIndex = 0,
            itemLabel = { "7 Days" },
            onConfirm = {},
            onDismiss = {}
        )
    }
}