package com.example.rxtracker.navigation.topbar

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.composables.icons.lucide.EllipsisVertical
import com.composables.icons.lucide.Lucide
import com.example.rxtracker.R
import com.example.rxtracker.navigation.AppDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(navController: NavController) {
    var expanded by remember { mutableStateOf(false) }
    val darkTheme = isSystemInDarkTheme()

    val containerColor = if (darkTheme) MaterialTheme.colorScheme.primaryContainer
    else MaterialTheme.colorScheme.primary

    val contentColor = if (darkTheme) MaterialTheme.colorScheme.onPrimaryContainer
    else MaterialTheme.colorScheme.onPrimary

    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.prescription_24),
                    contentDescription = null,
                    tint = contentColor
                )
                Text("RxTracker")

            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = containerColor,
            titleContentColor = contentColor,
            actionIconContentColor = contentColor
        ),
        actions = {
            IconButton(onClick = { expanded = true }) {
                Icon(Lucide.EllipsisVertical, contentDescription = "Menu")
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Settings") },
                    onClick = {
                        navController.navigate(AppDestination.Settings.route)
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("About") },
                    onClick = {
                        navController.navigate(AppDestination.About.route)
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Privacy Policy") },
                    onClick = {
                        navController.navigate(AppDestination.PrivacyPolicy.route)
                        expanded = false
                    }
                )
            }
        }
    )
}