package com.example.rxtracker.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.rxtracker.ui.home.components.CalendarDay
import com.example.rxtracker.ui.theme.RXTrackerTheme
import com.example.rxtracker.utils.getWeekPageTitle
import com.example.rxtracker.utils.rememberFirstVisibleWeekAfterScroll
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val currentDate = remember { LocalDate.now() }
    val startDate = remember { currentDate.minusDays(500) }
    val endDate = remember { currentDate.plusDays(500) }
    var selection by remember { mutableStateOf(currentDate) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        val state = rememberWeekCalendarState(
            startDate = startDate,
            endDate = endDate,
            firstVisibleWeekDate = currentDate
        )
        val visibleWeek = rememberFirstVisibleWeekAfterScroll(state)
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(),
            title = { Text(text = getWeekPageTitle(visibleWeek)) },
        )
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
            },
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