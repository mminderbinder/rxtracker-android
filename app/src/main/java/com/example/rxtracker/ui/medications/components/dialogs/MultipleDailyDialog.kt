package com.example.rxtracker.ui.medications.components.dialogs

import androidx.compose.runtime.Composable

@Composable
fun MultipleDailyDialog(
    onDismiss: () -> Unit,
    onConfirm: (timesPerDay: Int) -> Unit
) {
    SingleDialog(
        title = "Times per day",
        itemCount = 12,
        initialIndex = 1,
        itemLabel = { index ->
            val times = index + 1
            "$times ${if (times == 1) "time" else "times"}"
        },
        onDismiss = onDismiss,
        onConfirm = { onConfirm(it + 1) }
    )
}