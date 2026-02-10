package com.example.rxtracker.data.models

import kotlinx.datetime.LocalTime

data class DoseTimes(
    val time: LocalTime,
    val quantity: Int
)
