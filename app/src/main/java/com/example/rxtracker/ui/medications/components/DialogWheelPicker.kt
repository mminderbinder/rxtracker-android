package com.example.rxtracker.ui.medications.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.swmansion.kmpwheelpicker.WheelPicker
import com.swmansion.kmpwheelpicker.WheelPickerState
import kotlin.math.abs

@Composable
fun DialogWheelPicker(
    modifier: Modifier = Modifier,
    state: WheelPickerState,
    itemLabel: (Int) -> String,
    friction: Float = 8f,
) {
    WheelPicker(
        state = state,
        bufferSize = 1,
        friction = friction,
        modifier = modifier,
        window = {
            Column(
                modifier = Modifier.fillMaxWidth(0.6f),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.primary,
                    thickness = 1.dp
                )
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.primary,
                    thickness = 1.dp
                )
            }
        }
    ) { index ->
        Text(
            text = itemLabel(index),
            modifier = Modifier
                .padding(16.dp, 8.dp)
                .graphicsLayer {
                    alpha = (3 - abs(state.value - index)).coerceIn(0f, 1f)
                },
            fontWeight = if (index == state.index) FontWeight.Bold else FontWeight.Normal,
            color = lerp(
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.onSurfaceVariant,
                abs(state.value - index).coerceIn(0f, 1f)
            )
        )
    }
}