package com.mudrichenkoevgeny.feature.user.result

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.mudrichenkoevgeny.feature.user.R
import com.mudrichenkoevgeny.core.ui.R as UIResources
import com.mudrichenkoevgeny.core.common.util.getArgsFromMessage
import com.mudrichenkoevgeny.core.network.result.RestApiResult

const val ARGUMENT_MASK = "[arg]"

interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>

    sealed class Failure : Result<Nothing> {
        object IncorrectLoginOrPassword : Failure() {
            const val MESSAGE = "Incorrect login or password"
        }
        class UserBlocked(val email: String) : Failure() {
            companion object {
                const val MESSAGE_MASK = "User $ARGUMENT_MASK is blocked"
            }
        }
        object InternetNotAvailable : Failure()
        object Unknown : Failure()
    }
}

fun RestApiResult.Error.convertToFailureResult(): Result.Failure {
    return when (this) {
        is RestApiResult.Error.InternetNotAvailable -> Result.Failure.InternetNotAvailable
        is RestApiResult.Error.Response -> {
            val userBlockedEmail = this.message?.let { message ->
                getArgsFromMessage(
                    message = message,
                    argumentMask = ARGUMENT_MASK,
                    messageMask = Result.Failure.UserBlocked.MESSAGE_MASK
                ).firstOrNull()
            }
            when {
                this.message == Result.Failure.IncorrectLoginOrPassword.MESSAGE -> {
                    Result.Failure.IncorrectLoginOrPassword
                }
                userBlockedEmail != null -> {
                    Result.Failure.UserBlocked(userBlockedEmail)
                }
                else -> Result.Failure.Unknown
            }
        }
        else -> Result.Failure.Unknown
    }
}

@Composable
fun Result.Failure.convertToText(): String {
    return when (this) {
        is Result.Failure.IncorrectLoginOrPassword -> stringResource(R.string.auth_error_incorrect_login_data)
        is Result.Failure.InternetNotAvailable -> stringResource(UIResources.string.error_no_internet)
        is Result.Failure.UserBlocked -> stringResource(R.string.auth_error_user_blocked, this.email)
        else -> stringResource(UIResources.string.error_other)
    }
}
