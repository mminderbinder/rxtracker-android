package com.example.rxtracker.navigation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.example.rxtracker.navigation.AppDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(
    navController: NavController,
    title: String = "RXTracker",
    content: @Composable (PaddingValues) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                actions = {
                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menu")
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
        }
    ) { innerPadding ->
        content(innerPadding)
    }
}