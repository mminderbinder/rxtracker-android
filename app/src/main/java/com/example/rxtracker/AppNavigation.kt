package com.example.rxtracker

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.rxtracker.ui.appointments.AppointmentsScreen
import com.example.rxtracker.ui.home.HomeScreen
import com.example.rxtracker.ui.medications.MedicationsScreen
import com.example.rxtracker.ui.reminders.RemindersScreen
import com.example.rxtracker.ui.topmenu.about.AboutScreen
import com.example.rxtracker.ui.topmenu.privacy.PrivacyPolicyScreen
import com.example.rxtracker.ui.topmenu.settings.SettingsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("RXTracker") },
                actions = {
                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Menu"
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(text = "Settings") },
                            onClick = {
                                navController.navigate(AppDestination.Settings.route)
                                expanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(text = "About") },
                            onClick = {
                                navController.navigate(AppDestination.About.route)
                                expanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(text = "Privacy Policy") },
                            onClick = {
                                navController.navigate(AppDestination.PrivacyPolicy.route)
                                expanded = false
                            }
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar() {
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
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppDestination.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(AppDestination.Home.route) {
                HomeScreen()
            }
            composable(AppDestination.Medications.route) {
                MedicationsScreen()
            }
            composable(AppDestination.Reminders.route) {
                RemindersScreen()
            }
            composable(AppDestination.Appointments.route) {
                AppointmentsScreen()
            }
            composable(AppDestination.Settings.route) {
                SettingsScreen()
            }
            composable(AppDestination.About.route) {
                AboutScreen()
            }
            composable(AppDestination.PrivacyPolicy.route) {
                PrivacyPolicyScreen()
            }
        }
    }
}