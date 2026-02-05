package com.example.rxtracker.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rxtracker.ui.home.components.CalendarDay
import com.example.rxtracker.ui.theme.RXTrackerTheme
import com.example.rxtracker.utils.getWeekPageTitle
import com.example.rxtracker.utils.rememberFirstVisibleWeekAfterScroll
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToAddMedication: () -> Unit = {}
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

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(),
                title = { Text(text = getWeekPageTitle(visibleWeek)) },
                actions = {
                    TextButton(
                        onClick = {
                            selection = currentDate
                            coroutineScope.launch {
                                state.animateScrollToWeek(currentDate)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Today,
                            contentDescription = "Scroll to today"
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Today")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAddMedication
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add medication"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
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
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    RXTrackerTheme {
        HomeScreen(onNavigateToAddMedication = {})
    }
}