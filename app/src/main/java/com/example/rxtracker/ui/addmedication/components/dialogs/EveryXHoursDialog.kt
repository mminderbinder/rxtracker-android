package com.example.rxtracker.ui.addmedication.components.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.rxtracker.ui.theme.RXTrackerTheme

@Composable
fun EveryXHoursDialog(
    onDismiss: () -> Unit,
    onConfirm: (hours: Int) -> Unit
) {
    SingleDialog(
        title = "Select hour interval",
        itemCount = 12,
        initialIndex = 5,
        itemLabel = { index ->
            val hours = index + 1
            "$hours ${if (hours == 1) "hour" else "hours"}"
        },
        onDismiss = onDismiss,
        onConfirm = { onConfirm(it + 1) }
    )
}


@Preview(showBackground = true)
@Composable
fun EveryXHoursDialogPreview() {
    RXTrackerTheme {
        EveryXHoursDialog(
            onDismiss = {},
            onConfirm = {}
        )
    }
}