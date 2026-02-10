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
        itemCount = 90,
        initialIndex = 2,
        itemLabel = { index ->
            val days = index + 1
            "$days ${if (days == 1) "day" else "days"}"
        },
        onDismiss = onDismiss,
        onConfirm = { onConfirm(it + 1) }
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