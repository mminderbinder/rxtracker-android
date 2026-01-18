package com.example.rxtracker

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Medication
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.rxtracker.AppDestination.Appointments
import com.example.rxtracker.AppDestination.Home
import com.example.rxtracker.AppDestination.Medications
import com.example.rxtracker.AppDestination.Reminders


sealed class AppDestination(
    val route: String,
    val title: String,
    val icon: ImageVector? = null
) {
    object Home : AppDestination(
        route = "home",
        title = "Home",
        icon = Icons.Filled.Home
    )

    object Medications : AppDestination(
        route = "medications",
        title = "Meds",
        icon = Icons.Filled.Medication
    )

    object Reminders : AppDestination(
        route = "reminders",
        title = "Reminders",
        icon = Icons.Filled.Alarm
    )

    object Appointments : AppDestination(
        route = "appointments",
        title = "Appointments",
        icon = Icons.Filled.CalendarToday
    )

    object Settings : AppDestination(
        route = "settings",
        title = "Settings"
    )

    object About : AppDestination(
        route = "about",
        title = "About"
    )

    object PrivacyPolicy : AppDestination(
        route = "privacy_policy",
        title = "Privacy Policy"
    )
}


val bottomNavDestinations = listOf(
    Home,
    Medications,
    Reminders,
    Appointments
)