package com.example.rxtracker

import android.app.Application
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerFactory
import com.example.rxtracker.data.work.MarkNotLoggedWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class RXTrackerApp : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: WorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        scheduleMarkNotLoggedWorker()
    }

    private fun scheduleMarkNotLoggedWorker() {
        val oneTimeRequest = OneTimeWorkRequestBuilder<MarkNotLoggedWorker>().build()
        WorkManager.getInstance(this).enqueue(oneTimeRequest)

        val request = PeriodicWorkRequestBuilder<MarkNotLoggedWorker>(1, TimeUnit.DAYS)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "mark_not_logged",
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }
}