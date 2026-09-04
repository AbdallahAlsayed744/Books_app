package com.hyperdesign.presentation.error
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.hyperdesign.books_app.common.presentation.R
import com.hyperdesign.domain.error.AppError
import com.hyperdesign.domain.error.AppErrorException

@Composable
fun AppError.asMessage(): String = when (this) {
    AppError.Network -> stringResource(R.string.error_no_connection)
    AppError.Timeout -> stringResource(R.string.error_timeout)
    AppError.Unauthorized -> stringResource(R.string.error_unauthorized)
    AppError.NotFound -> stringResource(R.string.error_not_found)
    AppError.Database -> stringResource(R.string.error_database)
    is AppError.Http -> stringResource(R.string.error_server, code)
    is AppError.Unknown -> stringResource(R.string.error_generic)
}

@Composable
fun Throwable?.toUserMessage(fallback: String): String =
    (this as? AppErrorException)?.error?.asMessage() ?: fallback
