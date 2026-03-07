package com.example.rxtracker.data.repository

import android.content.Context
import com.example.rxtracker.data.models.Prescribable
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrescribableRepository @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    private var prescribables: List<Prescribable>? = null
    private val mutex = Mutex()

    suspend fun ensureLoaded() {
        mutex.withLock {
            if (prescribables != null) return

            withContext(Dispatchers.IO) {
                val jsonString = context.assets.open("medications_flat.json")
                    .bufferedReader()
                    .use { it.readText() }

                prescribables = Json.Default.decodeFromString(jsonString)
            }
        }
    }

    suspend fun search(query: String): List<Prescribable> {
        if (query.isBlank()) return emptyList()

        return withContext(Dispatchers.Default) {
            val searchQuery = query.lowercase().trim()

            prescribables?.filter {
                it.name.lowercase().contains(searchQuery) ||
                        it.brand.lowercase().contains(searchQuery)
            }?.take(20) ?: emptyList()
        }
    }
}