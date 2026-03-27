package com.example.rxtracker.ui.shared

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.example.rxtracker.ui.theme.RXTrackerTheme
import dev.darkokoa.datetimewheelpicker.WheelTimePicker
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