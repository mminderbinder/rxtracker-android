package com.example.rxtracker.ui.medications.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rxtracker.data.Medication
import com.example.rxtracker.data.MedicationRepository
import com.example.rxtracker.ui.theme.RXTrackerTheme
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicationSearchBar(
    onMedicationSelected: (Medication) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var query by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var searchResults by remember { mutableStateOf<List<Medication>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isLoading = true
        MedicationRepository.ensureLoaded(context)
        isLoading = false
    }

    LaunchedEffect(query) {
        delay(300)
        searchResults = if (query.length >= 2) {
            MedicationRepository.search(query)
        } else {
            emptyList()
        }
    }

    SearchBar(
        inputField = {
            SearchBarDefaults.InputField(
                query = query,
                onQueryChange = { query = it },
                onSearch = { expanded = false },
                expanded = expanded,
                onExpandedChange = { expanded = it },
                placeholder = { Text("Search medications...") },
                leadingIcon = {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(Icons.Default.Search, contentDescription = null)
                    }
                }
            )
        },
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier.fillMaxWidth()
    ) {
        LazyColumn {
            items(searchResults) { prescription ->
                ListItem(
                    headlineContent = {
                        Text(
                            if (prescription.name != prescription.brand) {
                                "${prescription.name} (${prescription.brand})"
                            } else {
                                prescription.name
                            }
                        )
                    },
                    supportingContent = {
                        Text("${prescription.amount} ${prescription.form}")
                    },
                    modifier = Modifier.clickable {
                        onMedicationSelected(prescription)
                        query = ""
                        expanded = false
                    }
                )
                HorizontalDivider()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MedicationSearchBarPreview() {
    RXTrackerTheme {
        MedicationSearchBar(
            onMedicationSelected = {})
    }
}