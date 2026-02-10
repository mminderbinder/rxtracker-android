package com.example.rxtracker.ui.medications.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.dp
import com.swmansion.kmpwheelpicker.WheelPicker
import com.swmansion.kmpwheelpicker.WheelPickerState
import kotlin.math.abs

@Composable
fun DialogWheelPicker(
    state: WheelPickerState,
    itemLabel: (Int) -> String,
    modifier: Modifier = Modifier
) {
    WheelPicker(
        state = state,
        bufferSize = 3,
        modifier = modifier,
        window = {
            Box(
                Modifier.background(
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                    MaterialTheme.shapes.small
                )
            )
        }
    ) { index ->
        Text(
            text = itemLabel(index),
            modifier = Modifier
                .padding(16.dp, 8.dp)
                .graphicsLayer {
                    alpha = (3 - abs(state.value - index)).coerceIn(0f, 1f)
                },
            color = lerp(
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.onSurfaceVariant,
                abs(state.value - index).coerceIn(0f, 1f)
            )
        )
    }
}