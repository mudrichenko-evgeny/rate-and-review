package com.mudrichenkoevgeny.movierating.result

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.mudrichenkoevgeny.core.network.result.RestApiResult
import com.mudrichenkoevgeny.core.ui.R as UIResources

interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>

    sealed class Failure : Result<Nothing> {
        object InternetNotAvailable : Failure()
        object Unknown : Failure()
    }
}

fun RestApiResult.Error.convertToFailureResult(): Result.Failure {
    return when (this) {
        is RestApiResult.Error.InternetNotAvailable -> Result.Failure.InternetNotAvailable
        is RestApiResult.Error.Response -> Result.Failure.Unknown
        else -> Result.Failure.Unknown
    }
}

@Composable
fun Result.Failure.convertToText(): String {
    return when (this) {
        is Result.Failure.InternetNotAvailable -> stringResource(UIResources.string.error_no_internet)
        else -> stringResource(UIResources.string.error_other)
    }
}
