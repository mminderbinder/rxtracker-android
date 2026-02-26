package com.example.rxtracker.ui.medications.components.dialogs

import androidx.compose.runtime.Composable

@Composable
fun MultipleDailyDialog(
    onDismiss: () -> Unit,
    onConfirm: (timesPerDay: Int) -> Unit
) {
    SingleDialog(
        title = "Times per day",
        itemCount = 9,
        initialIndex = 0,
        itemLabel = { index ->
            val times = index + 2
            "$times times"
        },
        onDismiss = onDismiss,
        onConfirm = { onConfirm(it + 2) }
    )
}