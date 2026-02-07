package com.example.rxtracker.ui.medications.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun QuantityInput(
    quantity: Double,
    onQuantityChange: (Double) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Quantity"
) {
    var textValue by remember(quantity) {
        mutableStateOf(
            if (quantity % 1.0 == 0.0) quantity.toInt().toString()
            else quantity.toString()
        )
    }

    OutlinedTextField(
        value = textValue,
        onValueChange = { newText ->
            val filtered = newText.filter { it.isDigit() || it == '.' }

            if (filtered.count { it == '.' } > 1) return@OutlinedTextField

            if (filtered.startsWith("00")) return@OutlinedTextField

            textValue = filtered

            val parsed = filtered.toDoubleOrNull()
            if (parsed != null) {
                onQuantityChange(parsed)
            }
        },
        modifier = modifier,
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal,
            imeAction = ImeAction.Done
        ),
        singleLine = true
    )
}