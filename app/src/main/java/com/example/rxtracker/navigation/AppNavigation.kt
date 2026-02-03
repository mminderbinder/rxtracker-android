package com.example.rxtracker.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.rxtracker.navigation.components.MainScaffold
import com.example.rxtracker.navigation.components.SecondaryScaffold
import com.example.rxtracker.ui.appointments.AppointmentsScreen
import com.example.rxtracker.ui.home.HomeScreen
import com.example.rxtracker.ui.medications.AddMedicationScreen
import com.example.rxtracker.ui.medications.MedicationsScreen
import com.example.rxtracker.ui.menu.about.AboutScreen
import com.example.rxtracker.ui.menu.privacy.PrivacyPolicyScreen
import com.example.rxtracker.ui.menu.settings.SettingsScreen
import com.example.rxtracker.ui.reminders.RemindersScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentRoute = currentDestination?.route

    val showBottomNav = currentRoute in listOf(
        AppDestination.Home.route,
        AppDestination.Medications.route,
        AppDestination.Reminders.route,
        AppDestination.Appointments.route
    )

    Scaffold(
        bottomBar = {
            if (showBottomNav) {
                NavigationBar {
                    bottomNavDestinations.forEach { destination ->
                        NavigationBarItem(
                            icon = {
                                destination.icon?.let {
                                    Icon(imageVector = it, contentDescription = destination.title)
                                }
                            },
                            label = { Text(destination.title) },
                            selected = currentDestination?.hierarchy?.any {
                                it.route == destination.route
                            } == true,
                            onClick = {
                                navController.navigate(destination.route)
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppDestination.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(AppDestination.Home.route) {
                HomeScreen(
                    onNavigateToAddMedication = {
                        navController.navigate(AppDestination.AddMedication.route)
                    }
                )
            }
            composable(AppDestination.Medications.route) {
                MainScaffold(navController, "Medications") {
                    MedicationsScreen(modifier = Modifier.padding(it))
                }
            }
            composable(AppDestination.Reminders.route) {
                MainScaffold(navController, "Reminders") {
                    RemindersScreen(modifier = Modifier.padding(it))
                }
            }
            composable(AppDestination.Appointments.route) {
                MainScaffold(navController, "Appointments") {
                    AppointmentsScreen(modifier = Modifier.padding(it))
                }
            }
            composable(AppDestination.Settings.route) {
                SecondaryScaffold(navController, "Settings") {
                    SettingsScreen(modifier = Modifier.padding(it))
                }
            }
            composable(AppDestination.About.route) {
                SecondaryScaffold(navController, "About") {
                    AboutScreen(modifier = Modifier.padding(it))
                }
            }
            composable(AppDestination.PrivacyPolicy.route) {
                SecondaryScaffold(navController, "Privacy Policy") {
                    PrivacyPolicyScreen(modifier = Modifier.padding(it))
                }
            }
            composable(AppDestination.AddMedication.route) {
                SecondaryScaffold(navController, "Add Medication") {
                    AddMedicationScreen(modifier = Modifier.padding(it))
                }
            }
        }
    }
}