package com.example.rxtracker.ui.shared

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.rxtracker.ui.theme.RXTrackerTheme
import com.example.rxtracker.ui.theme.primaryLight
import dev.darkokoa.datetimewheelpicker.WheelDatePicker
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock

@Composable
fun DateWheelPicker(
    startDate: LocalDate,
    minDate: LocalDate? = null,
    onConfirm: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedDate by remember { mutableStateOf(startDate) }

    BaseDialog(
        title = "Select date",
        onConfirm = { onConfirm(selectedDate) },
        onDismiss = onDismiss
    ) {
        WheelDatePicker(
            startDate = startDate,
            minDate = minDate ?: LocalDate(1900, 1, 1),
            onSnappedDate = { snappedDate -> selectedDate = snappedDate },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DateWheelPickerPreview() {
    RXTrackerTheme {
        DateWheelPicker(
            startDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
            onConfirm = {},
            onDismiss = {}
        )
    }
}