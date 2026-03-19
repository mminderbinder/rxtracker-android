package com.example.rxtracker.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.rxtracker.R
import com.example.rxtracker.ui.home.components.CalendarDay
import com.example.rxtracker.ui.home.components.DoseCard
import com.example.rxtracker.utils.getWeekPageTitle
import com.example.rxtracker.utils.rememberFirstVisibleWeekAfterScroll
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val timeFormatter = DateTimeFormatter.ofPattern("h:mm a")

@Composable
fun HomeScreen(
    viewModel: HomeViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    val currentDate = remember { LocalDate.now() }
    val startDate = remember { currentDate.minusDays(500) }
    val endDate = remember { currentDate.plusDays(500) }
    val coroutineScope = rememberCoroutineScope()


    val state = rememberWeekCalendarState(
        startDate = startDate,
        endDate = endDate,
        firstVisibleWeekDate = currentDate
    )

    val visibleWeek = rememberFirstVisibleWeekAfterScroll(state)

    LaunchedEffect(visibleWeek) {
        val weekContainsToday = visibleWeek.days.any { it.date == currentDate }
        viewModel.selectDate(
            if (weekContainsToday) currentDate
            else visibleWeek.days.first().date
        )
    }

    val groupedDoses = uiState.doses.groupBy { it.scheduledTime }

    val showTodayButton = uiState.selectedDate != currentDate ||
            !visibleWeek.days.any { it.date == currentDate }

    Column(
        modifier = Modifier
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
        }

        Spacer(modifier = Modifier.height(8.dp))

        WeekCalendar(
            state = state,
            dayContent = { day ->
                CalendarDay(
                    date = day.date,
                    isSelected = uiState.selectedDate == day.date,
                    onClick = { date ->
                        if (uiState.selectedDate != date) {
                            viewModel.selectDate(date)
                        }
                    }
                )
            }
        )
        AnimatedVisibility(visible = showTodayButton) {
            TextButton(
                onClick = {
                    viewModel.selectDate(currentDate)
                    coroutineScope.launch {
                        state.animateScrollToWeek(currentDate)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.width(4.dp))
                Text("Return to today")
            }
        }
        if (uiState.doses.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(R.drawable.schedule),
                        contentDescription = null,
                        modifier = Modifier.size(80.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "No medications today",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                groupedDoses.forEach { (time, dosesAtTime) ->
                    item {
                        Text(
                            text = time.format(timeFormatter),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(top = 12.dp, bottom = 8.dp)
                        )
                    }
                    items(dosesAtTime) { dose ->
                        DoseCard(
                            dose = dose,
                            selectedDate = uiState.selectedDate,
                            onTap = {/* TODO: launch bottom sheet */ },
                            onToggleTaken = { checked ->
                                if (checked) viewModel.markTaken(dose)
                                else viewModel.unmarkAsTaken(dose)
                            }
                        )
                    }
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun HomeScreenPreview() {
//    RXTrackerTheme {
//        HomeScreen()
//    }
//}