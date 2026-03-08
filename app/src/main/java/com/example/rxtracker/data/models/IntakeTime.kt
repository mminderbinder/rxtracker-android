package com.example.rxtracker.data.models

enum class IntakeTime(val label: String) {
    BEFORE_MEAL("Before a meal"),
    WITH_MEAL("With a meal"),
    AFTER_MEAL("After a meal"),
    NONE("Doesn't matter")
}