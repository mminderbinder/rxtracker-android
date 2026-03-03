package com.example.rxtracker.ui.medications.components.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.rxtracker.ui.medications.components.QuantityCounter

@Composable
fun QuantityDialog(
    initialQuantity: Double,
    title: String,
    min: Double,
    max: Double,
    onDismiss: () -> Unit,
    onConfirm: (Double) -> Unit
) {
    var quantity by remember { mutableDoubleStateOf(initialQuantity) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                QuantityCounter(
                    quantity = quantity,
                    onQuantityChange = { quantity = it },
                    min = min,
                    max = max,
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(quantity) }) { Text("OK") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

fun doseLabel(quantity: Double): String {
    val formatted = if (quantity % 1.0 == 0.0) quantity.toInt().toString()
    else quantity.toString()
    return "$formatted ${if (quantity == 1.0) "dose" else "doses"}"
}