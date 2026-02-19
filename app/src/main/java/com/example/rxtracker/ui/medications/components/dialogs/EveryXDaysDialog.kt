package com.example.rxtracker.ui.medications.components.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.rxtracker.ui.theme.RXTrackerTheme

@Composable
fun EveryXDaysDialog(
    onDismiss: () -> Unit,
    onConfirm: (days: Int) -> Unit
) {
    SingleDialog(
        title = "Every how many days?",
        itemCount = 89,
        initialIndex = 0,
        friction = 4f,
        itemLabel = { index ->
            val days = index + 2
            "$days days"
        },
        onDismiss = onDismiss,
        onConfirm = { onConfirm(it + 2) }
    )
}

@Preview(showBackground = true)
@Composable
fun EveryXDaysDialogPreview() {
    RXTrackerTheme {
        EveryXDaysDialog(
            onDismiss = {},
            onConfirm = {}
        )
    }
}