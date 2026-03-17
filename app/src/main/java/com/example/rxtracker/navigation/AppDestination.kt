package com.example.rxtracker.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.composables.icons.lucide.History
import com.composables.icons.lucide.House
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.PillBottle
import com.example.rxtracker.navigation.AppDestination.History
import com.example.rxtracker.navigation.AppDestination.Home
import com.example.rxtracker.navigation.AppDestination.Medications

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
        title = "Medications",
        icon = Lucide.PillBottle
    )

    object History : AppDestination(
        route = "history",
        title = "History",
        icon = Lucide.History
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

    object AddFrequency : AppDestination(
        route = "add_frequency",
        title = "Add Frequency"
    )

    object AddDoseDetails : AppDestination(
        route = "add_dose_details",
        title = "Add Dose Details"
    )

    object AddTimes : AppDestination(
        route = "add_times",
        title = "Add Times"
    )

    object AddOptionalDetails : AppDestination(
        route = "add_optional_details",
        title = "Optional Details"
    )

    companion object {
        fun fromRoute(route: String?): AppDestination? = listOf(
            Home, Medications, History,
            Settings, About, PrivacyPolicy, AddMedication,
            AddFrequency, AddDoseDetails, AddTimes, AddOptionalDetails
        ).find { it.route == route }
    }
}

val bottomNavDestinations = listOf(
    Home,
    Medications,
    History
)