package com.example.rxtracker.data.models

enum class Frequency(val label: String) {
    ONCE_DAILY("Once a day"),
    MULTIPLE_DAILY("Multiple times a day"),
    AS_NEEDED("As needed"),
    EVERY_X_HOURS("Every X hours"),
    EVERY_X_DAYS("Every X days"),
    SPECIFIC_WEEKDAYS("Specific weekdays"),
    CYCLIC("Cyclic (intake days, rest days)")
}