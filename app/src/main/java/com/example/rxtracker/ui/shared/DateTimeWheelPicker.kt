package com.example.rxtracker.ui.shared

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.example.rxtracker.ui.theme.RXTrackerTheme
import dev.darkokoa.datetimewheelpicker.WheelDateTimePicker
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import kotlin.time.Clock

@Composable
fun DateTimeWheelPicker(
    startDateTime: LocalDateTime,
    minDateTime: LocalDateTime? = null,
    maxDateTime: LocalDateTime? = null,
    title: String = "Select date and time",
    onConfirm: (LocalDateTime) -> Unit,
    onDismiss: () -> Unit
) {

    var selectedDateTime by remember { mutableStateOf(startDateTime) }
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
    val yearsRange = when (today.month.number) {
        1 -> today.year - 1..today.year
        12 -> today.year..today.year + 1
        else -> null
    }

    BaseDialog(
        title = title,
        onConfirm = { onConfirm(selectedDateTime) },
        onDismiss = onDismiss
    ) {
        WheelDateTimePicker(
            startDateTime = startDateTime,
            minDateTime = minDateTime ?: LocalDateTime(1900, 1, 1, 0, 0),
            maxDateTime = maxDateTime ?: LocalDateTime(2100, 12, 31, 23, 59),
            yearsRange = yearsRange,
            onSnappedDateTime = { snappedDateTime -> selectedDateTime = snappedDateTime }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DateTimeWheelPickerPreview() {
    RXTrackerTheme {
        DateTimeWheelPicker(
            startDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
            onConfirm = {},
            onDismiss = {}
        )
    }
}