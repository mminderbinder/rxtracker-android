package com.example.rxtracker.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.*
import com.example.rxtracker.ui.home.components.CalendarDay
import com.example.rxtracker.ui.theme.RXTrackerTheme
import com.example.rxtracker.utils.getWeekPageTitle
import com.example.rxtracker.utils.rememberFirstVisibleWeekAfterScroll
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {
    val currentDate = remember { LocalDate.now() }
    val startDate = remember { currentDate.minusDays(500) }
    val endDate = remember { currentDate.plusDays(500) }
    var selection by remember { mutableStateOf(currentDate) }
    val coroutineScope = rememberCoroutineScope()

    val state = rememberWeekCalendarState(
        startDate = startDate,
        endDate = endDate,
        firstVisibleWeekDate = currentDate
    )
    val visibleWeek = rememberFirstVisibleWeekAfterScroll(state)

    LaunchedEffect(visibleWeek) {
        val weekContainsToday = visibleWeek.days.any { it.date == currentDate }
        selection = if (weekContainsToday) {
            currentDate
        } else {
            visibleWeek.days.first().date
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = getWeekPageTitle(visibleWeek),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f)
            )
            TextButton(
                onClick = {
                    selection = currentDate
                    coroutineScope.launch {
                        state.animateScrollToWeek(currentDate)
                    }
                }
            ) {
                Icon(Lucide.CalendarCheck, contentDescription = "Scroll to today")
                Spacer(modifier = Modifier.width(4.dp))
                Text("Today")
            }
        }

        Spacer(modifier = Modifier.padding(8.dp))

        WeekCalendar(
            state = state,
            dayContent = { day ->
                CalendarDay(
                    date = day.date,
                    isSelected = selection == day.date,
                    onClick = { date ->
                        if (selection != date) {
                            selection = date
                        }
                    }
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    RXTrackerTheme {
        HomeScreen()
    }
}