package com.example.rxtracker.data.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.rxtracker.data.repository.ScheduledDoseRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.time.LocalDate

class MarkNotLoggedWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val repository: ScheduledDoseRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        repository.markPastPendingAsNotLogged(LocalDate.now())
        return Result.success()
    }
}