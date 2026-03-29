package com.example.rxtracker.utils

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

// Now / Today

fun now(): LocalDateTime =
    Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

fun today(): LocalDate = now().date
fun currentTime(): LocalTime = now().time

// LocalTime arithmetic

fun LocalTime.plusHours(hours: Int): LocalTime =
    LocalTime.fromSecondOfDay((toSecondOfDay() + hours * 3600) % 86400)

fun LocalTime.minusHours(hours: Int): LocalTime =
    LocalTime.fromSecondOfDay(((toSecondOfDay() - hours * 3600) % 86400 + 86400) % 86400)

fun LocalTime.plusMinutes(minutes: Int): LocalTime =
    LocalTime.fromSecondOfDay((toSecondOfDay() + minutes * 60) % 86400)

fun LocalTime.minusMinutes(minutes: Int): LocalTime =
    LocalTime.fromSecondOfDay(((toSecondOfDay() - minutes * 60) % 86400 + 86400) % 86400)

// LocalDate arithmetic

fun LocalDate.plusDays(days: Int): LocalDate = plus(days, DateTimeUnit.DAY)
fun LocalDate.minusDays(days: Int): LocalDate = minus(days, DateTimeUnit.DAY)
fun LocalDate.plusWeeks(weeks: Int): LocalDate = plus(weeks, DateTimeUnit.WEEK)
fun LocalDate.minusWeeks(weeks: Int): LocalDate = minus(weeks, DateTimeUnit.WEEK)
fun LocalDate.plusMonths(months: Int): LocalDate = plus(months, DateTimeUnit.MONTH)
fun LocalDate.minusMonths(months: Int): LocalDate = minus(months, DateTimeUnit.MONTH)

// LocalDate helpers

fun LocalDate.isToday(): Boolean = this == today()
fun LocalDate.isPast(): Boolean = this < today()
fun LocalDate.isFuture(): Boolean = this > today()
fun LocalDate.atTime(time: LocalTime): LocalDateTime = LocalDateTime(this, time)