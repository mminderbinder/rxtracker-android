package com.example.rxtracker.ui.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Minus
import com.composables.icons.lucide.Plus
import com.example.rxtracker.ui.theme.RXTrackerTheme

private const val STEP = 1.0

@Composable
fun QuantityCounter(
    quantity: Double,
    onQuantityChange: (Double) -> Unit,
    modifier: Modifier = Modifier,
    min: Double,
    max: Double,
) {
    var textValue by remember(quantity) {
        mutableStateOf(
            if (quantity % 1.0 == 0.0) quantity.toInt().toString()
            else quantity.toString()
        )
    }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        FilledIconButton(
            onClick = {
                val newQty = (quantity - STEP).coerceAtLeast(min)
                textValue = if (newQty % 1.0 == 0.0) newQty.toInt().toString()
                else newQty.toString()
                onQuantityChange(newQty)
            },
            enabled = quantity > min,
            shape = CircleShape,
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            )
        ) {
            Icon(
                imageVector = Lucide.Minus,
                contentDescription = "Decrease quantity"
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
                if (parsed != null && parsed in min..max) {
                    onQuantityChange(parsed)
                }
            },
            modifier = Modifier.width(100.dp),
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                textAlign = TextAlign.Center
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Done
            ),
            singleLine = true
        )
        FilledIconButton(
            onClick = {
                val newQty = (quantity + STEP).coerceAtMost(max)
                textValue = if (newQty % 1.0 == 0.0) newQty.toInt().toString()
                else newQty.toString()
                onQuantityChange(newQty)
            },
            enabled = quantity < max,
            shape = CircleShape,
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            )
        ) {
            Icon(
                imageVector = Lucide.Plus,
                contentDescription = "Increase quantity"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuantityCounterPreview() {
    RXTrackerTheme {
        QuantityCounter(
            quantity = 2.0,
            onQuantityChange = {},
            min = 0.5,
            max = 20.0,
        )
    }
}