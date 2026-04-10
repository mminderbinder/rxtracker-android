package com.example.rxtracker.ui.medication.add.components

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
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.ChevronRight
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Search
import com.composables.icons.lucide.X
import com.example.rxtracker.data.models.Prescribable
import com.example.rxtracker.ui.medication.add.MedicationSearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicationSearchBar(
    viewModel: MedicationSearchViewModel,
    onMedicationSelected: (Prescribable) -> Unit,
    onManualEntry: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var query by remember { mutableStateOf("") }

    LaunchedEffect(query) {
        viewModel.search(query)
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
                viewModel.isLoading -> Box(
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
            items(viewModel.searchResults) { prescription ->
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