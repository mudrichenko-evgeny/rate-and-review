package com.mudrichenkoevgeny.core.network.result

sealed class RestApiResult<out T> {

    data class Success<out T>(val data: T) : RestApiResult<T>()

    sealed class Error() : RestApiResult<Nothing>() {

        object InternetNotAvailable : Error() {
            const val EXCEPTION_MESSAGE: String = "Internet not available"
        }

        @Suppress("unused")
        class Exception(val message: String?) : Error()

        class Response(val code: Int?, val message: String?) : Error()

        class EmptyBody(val code: Int?) : Error()
    }
}