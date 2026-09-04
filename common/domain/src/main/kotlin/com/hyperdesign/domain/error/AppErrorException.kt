package com.hyperdesign.domain.error

class AppErrorException(
    val error: AppError,
    cause: Throwable? = null,
) : Exception(cause)
