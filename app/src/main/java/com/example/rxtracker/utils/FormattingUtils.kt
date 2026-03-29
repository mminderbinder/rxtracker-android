package com.example.rxtracker.utils

import com.kizitonwose.calendar.core.Week
import com.kizitonwose.calendar.core.yearMonth
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import java.time.DayOfWeek
import java.time.Month
import java.time.YearMonth
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
    val format = LocalTime.Format {
        amPmHour(padding = Padding.NONE)
        char(':')
        minute()
        char(' ')
        amPmMarker("AM", "PM")
    }
    return time.format(format)
}

fun getFormattedDate(date: LocalDate): String {
    val format = LocalDate.Format {
        monthName(MonthNames.ENGLISH_ABBREVIATED)
        char(' ')
        day()
        chars(", ")
        year()
    }
    return date.format(format)
}