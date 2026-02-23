package com.example.rxtracker.data.models

import java.time.LocalTime

data class DoseTime(
    val time: LocalTime,
    val quantity: Double = 1.0
)
