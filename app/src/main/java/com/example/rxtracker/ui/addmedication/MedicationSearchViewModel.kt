package com.example.rxtracker.ui.addmedication

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rxtracker.data.models.Prescribable
import com.example.rxtracker.data.repository.PrescribableRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedicationSearchViewModel @Inject constructor(
    private val prescribableRepository: PrescribableRepository
) : ViewModel() {

    var searchResults by mutableStateOf<List<Prescribable>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch {
            prescribableRepository.ensureLoaded()
        }
    }

    fun search(query: String) {
        viewModelScope.launch {
            if (query.length < 2) {
                searchResults = emptyList()
                isLoading = false
                return@launch
            }
            isLoading = true
            delay(300)
            searchResults = prescribableRepository.search(query)
            isLoading = false
        }
    }
}