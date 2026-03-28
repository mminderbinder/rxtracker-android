package com.example.rxtracker.ui.shared

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.example.rxtracker.ui.theme.RXTrackerTheme
import dev.darkokoa.datetimewheelpicker.WheelTimePicker
import dev.darkokoa.datetimewheelpicker.core.WheelPickerDefaults
import kotlinx.datetime.LocalTime

@Composable
fun TimeWheelPicker(
    startTime: LocalTime,
    title: String = "Select time",
    onConfirm: (LocalTime) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedTime by remember { mutableStateOf(startTime) }

    BaseDialog(
        title = title,
        onConfirm = { onConfirm(selectedTime) },
        onDismiss = onDismiss
    ) {
        WheelTimePicker(
            startTime = startTime,
            textStyle = MaterialTheme.typography.titleMedium,
            textColor = MaterialTheme.colorScheme.onSurface,
            size = DpSize(192.dp, 128.dp),
            selectorProperties = WheelPickerDefaults.selectorProperties(
                color = MaterialTheme.colorScheme.primaryContainer,
                border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(24.dp)
            ),
            onSnappedTime = { snappedTime -> selectedTime = snappedTime }
        )
    }
}


@Preview
@Composable
fun TimeWheelPickerPreview() {
    RXTrackerTheme {
        TimeWheelPicker(
            startTime = LocalTime(8, 0),
            onConfirm = {},
            onDismiss = {}
        )
    }
}