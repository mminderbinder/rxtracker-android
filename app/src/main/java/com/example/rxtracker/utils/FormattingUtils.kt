package com.example.rxtracker.utils

import com.kizitonwose.calendar.core.Week
import com.kizitonwose.calendar.core.yearMonth
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.Month
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

fun YearMonth.displayText(): String {
    return "${this.month.displayText()} ${this.year}"
}

fun Month.displayText(): String {
    return getDisplayName(TextStyle.FULL, Locale.ENGLISH)
}

fun DayOfWeek.displayText(): String {
    return getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
}

fun getWeekPageTitle(week: Week): String {
    val firstDate = week.days.first().date
    val lastDate = week.days.last().date
    return when {
        firstDate.yearMonth == lastDate.yearMonth -> {
            firstDate.yearMonth.displayText()
        }

        firstDate.year == lastDate.year -> {
            "${firstDate.month.displayText()} - ${lastDate.yearMonth.displayText()}"
        }

        else -> {
            "${firstDate.yearMonth.displayText()} - ${lastDate.yearMonth.displayText()}"
        }
    }
}

fun getFormattedTime(time: LocalTime): String {
    val timeFormatter = DateTimeFormatter.ofPattern("h:mm a")
    return time.format(timeFormatter)
}

fun getFormattedDate(date: LocalDate): String {
    val dateFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy")
    return date.format(dateFormatter)
}