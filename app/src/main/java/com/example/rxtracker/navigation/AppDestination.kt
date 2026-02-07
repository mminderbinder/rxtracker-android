package com.example.rxtracker.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.composables.icons.lucide.*
import com.example.rxtracker.navigation.AppDestination.Appointments
import com.example.rxtracker.navigation.AppDestination.Home
import com.example.rxtracker.navigation.AppDestination.Medications
import com.example.rxtracker.navigation.AppDestination.Reminders


sealed class AppDestination(
    val route: String,
    val title: String,
    val icon: ImageVector? = null
) {
    object Home : AppDestination(
        route = "home",
        title = "Home",
        icon = Lucide.House
    )

    object Medications : AppDestination(
        route = "medications",
        title = "Meds",
        icon = Lucide.PillBottle
    )

    object Reminders : AppDestination(
        route = "reminders",
        title = "Reminders",
        icon = Lucide.AlarmClock
    )

    object Appointments : AppDestination(
        route = "appointments",
        title = "Appointments",
        icon = Lucide.CalendarFold
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

    object AddMedication : AppDestination(
        route = "add_medication",
        title = "Add Medication"
    )
}


val bottomNavDestinations = listOf(
    Home,
    Medications,
    Reminders,
    Appointments
)