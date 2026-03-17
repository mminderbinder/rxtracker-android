package com.example.rxtracker.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Plus
import com.example.rxtracker.data.models.Frequency
import com.example.rxtracker.navigation.topbar.MainTopBar
import com.example.rxtracker.navigation.topbar.SecondaryTopBar
import com.example.rxtracker.ui.appointments.AppointmentsScreen
import com.example.rxtracker.ui.home.HomeScreen
import com.example.rxtracker.ui.home.HomeViewModel
import com.example.rxtracker.ui.medications.AddMedicationsViewModel
import com.example.rxtracker.ui.medications.MedicationSearchViewModel
import com.example.rxtracker.ui.medications.screens.AddDoseDetailsScreen
import com.example.rxtracker.ui.medications.screens.AddFrequencyScreen
import com.example.rxtracker.ui.medications.screens.AddMedicationScreen
import com.example.rxtracker.ui.medications.screens.AddOptionalDetailsScreen
import com.example.rxtracker.ui.medications.screens.AddTimesScreen
import com.example.rxtracker.ui.medications.screens.MedicationsScreen
import com.example.rxtracker.ui.menu.about.AboutScreen
import com.example.rxtracker.ui.menu.privacy.PrivacyPolicyScreen
import com.example.rxtracker.ui.menu.settings.SettingsScreen
import com.example.rxtracker.ui.reminders.RemindersScreen

private val mainRoutes = listOf(
    AppDestination.Home.route,
    AppDestination.Medications.route,
    AppDestination.Reminders.route,
    AppDestination.Appointments.route
)

private val secondaryRoutes = listOf(
    AppDestination.Settings.route,
    AppDestination.About.route,
    AppDestination.PrivacyPolicy.route,
    AppDestination.AddMedication.route,
    AppDestination.AddFrequency.route,
    AppDestination.AddDoseDetails.route,
    AppDestination.AddTimes.route,
    AppDestination.AddOptionalDetails.route
)

private val addMedicationRoutes = listOf(
    AppDestination.AddMedication.route,
    AppDestination.AddFrequency.route,
    AppDestination.AddDoseDetails.route,
    AppDestination.AddTimes.route,
    AppDestination.AddOptionalDetails.route
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentRoute = currentDestination?.route
    val snackbarHostState = remember { SnackbarHostState() }

    val isInAddFlow = currentRoute in addMedicationRoutes
    val showBottomNav = !isInAddFlow
    val showFab = currentRoute == AppDestination.Home.route

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            when (currentRoute) {
                in mainRoutes -> MainTopBar(navController)
                in secondaryRoutes -> SecondaryTopBar(
                    title = AppDestination.fromRoute(currentRoute)?.title ?: "",
                    navController = navController
                )
            }
        },
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
        },
        floatingActionButton = {
            if (showFab) {
                FloatingActionButton(
                    onClick = { navController.navigate(AppDestination.AddMedication.route) }
                ) {
                    Icon(imageVector = Lucide.Plus, contentDescription = "Add medication")
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppDestination.Home.route,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(AppDestination.Home.route) {
                val viewModel: HomeViewModel = hiltViewModel()
                HomeScreen(viewModel)
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
            navigation(
                startDestination = AppDestination.AddMedication.route,
                route = "add_medication_flow"
            ) {
                composable(AppDestination.AddMedication.route) { navBackStackEntry ->
                    val parentEntry = remember(navBackStackEntry) {
                        navController.getBackStackEntry("add_medication_flow")
                    }
                    val addViewModel: AddMedicationsViewModel = hiltViewModel(parentEntry)
                    val searchViewModel: MedicationSearchViewModel = hiltViewModel()

                    AddMedicationScreen(
                        searchViewModel = searchViewModel,
                        addViewModel = addViewModel,
                        onContinue = {
                            navController.navigate(AppDestination.AddFrequency.route)
                        }
                    )
                }
                composable(AppDestination.AddFrequency.route) { navBackStackEntry ->
                    val parentEntry = remember(navBackStackEntry) {
                        navController.getBackStackEntry("add_medication_flow")
                    }
                    val viewModel: AddMedicationsViewModel = hiltViewModel(parentEntry)

                    AddFrequencyScreen(
                        viewModel = viewModel,
                        onContinue = {
                            if (viewModel.uiState.frequency.type == Frequency.AS_NEEDED) {
                                navController.navigate(AppDestination.AddOptionalDetails.route)
                            } else {
                                navController.navigate(AppDestination.AddDoseDetails.route)
                            }
                        }
                    )
                }
                composable(AppDestination.AddDoseDetails.route) { navBackStackEntry ->
                    val parentEntry = remember(navBackStackEntry) {
                        navController.getBackStackEntry("add_medication_flow")
                    }
                    val viewModel: AddMedicationsViewModel = hiltViewModel(parentEntry)

                    AddDoseDetailsScreen(
                        viewModel = viewModel,
                        onContinue = {
                            if (viewModel.requiresTimesScreen()) {
                                navController.navigate(AppDestination.AddTimes.route)
                            } else {
                                navController.navigate(AppDestination.AddOptionalDetails.route)
                            }
                        }
                    )
                }
                composable(AppDestination.AddTimes.route) { navBackStackEntry ->
                    val parentEntry = remember(navBackStackEntry) {
                        navController.getBackStackEntry("add_medication_flow")
                    }
                    val viewModel: AddMedicationsViewModel = hiltViewModel(parentEntry)

                    AddTimesScreen(
                        viewModel = viewModel,
                        onContinue = {
                            navController.navigate(AppDestination.AddOptionalDetails.route)
                        },
                        snackbarHostState = snackbarHostState
                    )
                }
                composable(AppDestination.AddOptionalDetails.route) { navBackStackEntry ->
                    val parentEntry = remember(navBackStackEntry) {
                        navController.getBackStackEntry("add_medication_flow")
                    }
                    val viewModel: AddMedicationsViewModel = hiltViewModel(parentEntry)

                    AddOptionalDetailsScreen(
                        viewModel = viewModel,
                        onComplete = {
                            navController.popBackStack(
                                route = "add_medication_flow",
                                inclusive = true
                            )
                        }
                    )
                }
            }
        }
    }
}