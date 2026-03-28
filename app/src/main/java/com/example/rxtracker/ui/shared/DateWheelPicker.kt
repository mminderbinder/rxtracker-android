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
import androidx.compose.ui.unit.dp
import com.example.rxtracker.ui.theme.RXTrackerTheme
import dev.darkokoa.datetimewheelpicker.WheelDatePicker
import dev.darkokoa.datetimewheelpicker.core.WheelPickerDefaults
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
            textStyle = MaterialTheme.typography.titleMedium,
            textColor = MaterialTheme.colorScheme.onSurface,
            selectorProperties = WheelPickerDefaults.selectorProperties(
                color = MaterialTheme.colorScheme.primaryContainer,
                border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(24.dp)
            ),
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