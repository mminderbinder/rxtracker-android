package com.example.rxtracker.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.number

fun java.time.LocalDate.toKotlinx(): LocalDate =
    LocalDate(year, monthValue, dayOfMonth)

fun LocalDate.toJava(): java.time.LocalDate =
    java.time.LocalDate.of(year, month.number, day)

fun java.time.LocalTime.toKotlinx(): LocalTime =
    LocalTime(hour, minute, second, nano)

fun LocalTime.toJava(): java.time.LocalTime =
    java.time.LocalTime.of(hour, minute, second, nanosecond)

fun java.time.LocalDateTime.toKotlinx(): LocalDateTime =
    LocalDateTime(year, monthValue, dayOfMonth, hour, minute, second, nano)

fun LocalDateTime.toJava(): java.time.LocalDateTime =
    java.time.LocalDateTime.of(year, month.number, day, hour, minute, second, nanosecond)