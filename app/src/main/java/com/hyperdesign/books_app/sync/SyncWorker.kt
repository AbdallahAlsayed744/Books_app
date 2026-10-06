package com.hyperdesign.books_app.sync
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.hyperdesign.domain.repository.BookRepository
import com.hyperdesign.domain.result.Outcome

class SyncWorker(
    context: Context,
    params: WorkerParameters,
    private val bookRepository: BookRepository,
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result =
        when (bookRepository.refresh()) {
            is Outcome.Success -> Result.success()
            is Outcome.Failure -> Result.retry()
        }
}
