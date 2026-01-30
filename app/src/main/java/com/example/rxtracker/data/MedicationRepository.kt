package com.example.rxtracker.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

object MedicationRepository {
    private var medications: List<Medication>? = null
    private val mutex = Mutex()

    suspend fun ensureLoaded(context: Context) {
        mutex.withLock {
            if (medications != null) return

            withContext(Dispatchers.IO) {
                val jsonString = context.assets.open("medications_flat.json")
                    .bufferedReader()
                    .use { it.readText() }

                medications = Json.decodeFromString(jsonString)
            }
        }
    }

    suspend fun search(query: String): List<Medication> {
        if (query.isBlank()) return emptyList()

        return withContext(Dispatchers.Default) {
            val searchQuery = query.lowercase().trim()

            medications?.filter {
                it.name.lowercase().contains(searchQuery) ||
                        it.brand.lowercase().contains(searchQuery)
            }?.take(20) ?: emptyList()
        }
    }
}