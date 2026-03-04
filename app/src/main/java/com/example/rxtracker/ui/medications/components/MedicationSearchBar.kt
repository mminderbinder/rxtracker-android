package com.example.rxtracker.ui.medications.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.ChevronRight
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Search
import com.composables.icons.lucide.X
import com.example.rxtracker.data.Medication
import com.example.rxtracker.data.repos.MedicationRepository
import com.example.rxtracker.ui.theme.RXTrackerTheme
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicationSearchBar(
    onMedicationSelected: (Medication) -> Unit,
    onManualEntry: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var query by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf<List<Medication>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        MedicationRepository.ensureLoaded(context)
    }

    LaunchedEffect(query) {
        if (query.length >= 2) {
            isLoading = true
            delay(300)
            searchResults = MedicationRepository.search(query)
            isLoading = false
        } else {
            searchResults = emptyList()
            isLoading = false
        }
    }

    OutlinedTextField(
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
        ),
        shape = RoundedCornerShape(100),
        placeholder = { Text("Search medications...") },
        leadingIcon = {
            Icon(
                imageVector = Lucide.Search,
                contentDescription = null,
                modifier = Modifier.minimumInteractiveComponentSize(),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.66f)
            )
        },
        trailingIcon = {
            when {
                isLoading -> Box(
                    modifier = Modifier.size(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                }

                query.isNotBlank() -> IconButton(onClick = { query = "" }) {
                    Icon(
                        imageVector = Lucide.X,
                        contentDescription = "Clear",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        },
        value = query,
        onValueChange = { query = it },
        singleLine = true,
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
        modifier = modifier
            .fillMaxWidth()
            .minimumInteractiveComponentSize()
    )

    if (query.isNotBlank()) {
        LazyColumn(contentPadding = WindowInsets.ime.asPaddingValues()) {
            item {
                ListItem(
                    headlineContent = { Text(query) },
                    supportingContent = {
                        Text(
                            text = "Tap to add this medication if not found",
                            color = MaterialTheme.colorScheme.primary
                        )
                    },
                    trailingContent = {
                        Icon(
                            imageVector = Lucide.ChevronRight,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    modifier = Modifier.clickable {
                        onManualEntry(query)
                        query = ""
                    }
                )
                HorizontalDivider()
            }
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
                    trailingContent = {
                        Icon(
                            imageVector = Lucide.ChevronRight,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    modifier = Modifier.clickable {
                        onMedicationSelected(prescription)
                        query = ""
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
            onMedicationSelected = {},
            onManualEntry = {}
        )
    }
}