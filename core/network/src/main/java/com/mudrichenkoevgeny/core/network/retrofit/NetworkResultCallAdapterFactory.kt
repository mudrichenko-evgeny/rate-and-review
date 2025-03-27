package com.mudrichenkoevgeny.core.network.retrofit

import com.mudrichenkoevgeny.core.network.result.RestApiResult
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class NetworkResultCallAdapterFactory : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != Call::class.java) {
            return null
        }

        val callType = getParameterUpperBound(0, returnType as ParameterizedType)
        if (getRawType(callType) != RestApiResult::class.java) {
            return null
        }

        val resultType = getParameterUpperBound(0, callType as ParameterizedType)
        return NetworkResultCallAdapter(resultType)
    }

    private class NetworkResultCallAdapter(
        private val resultType: Type
    ) : CallAdapter<Type, Call<RestApiResult<Type>>> {

        override fun responseType(): Type = resultType

        override fun adapt(call: Call<Type>): Call<RestApiResult<Type>> {
            return NetworkResultCall(call)
        }

    }

    private class NetworkResultCall<T : Any>(
        private val delegate: Call<T>
    ) : Call<RestApiResult<T>> {

        override fun enqueue(callback: Callback<RestApiResult<T>>) {
            delegate.enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null) {
                            callback.onResponse(
                                this@NetworkResultCall,
                                Response.success(RestApiResult.Success(body))
                            )
                        } else {
                            callback.onResponse(
                                this@NetworkResultCall,
                                Response.success(
                                    RestApiResult.Error.EmptyBody(code = response.code())
                                )
                            )
                        }
                    } else {
                        val error = response.errorBody()?.string()
                        callback.onResponse(
                            this@NetworkResultCall,
                            Response.success(
                                RestApiResult.Error.Response(
                                    message = error,
                                    code = response.code()
                                )
                            )
                        )
                    }
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    val error = if (t.message == RestApiResult.Error.InternetNotAvailable.EXCEPTION_MESSAGE) {
                        RestApiResult.Error.InternetNotAvailable
                    } else {
                        RestApiResult.Error.Exception(message = t.message)
                    }
                    callback.onResponse(
                        this@NetworkResultCall,
                        Response.success(error)
                    )
                }
            })
        }

        override fun execute(): Response<RestApiResult<T>> = throw NotImplementedError()
        override fun clone(): Call<RestApiResult<T>> = NetworkResultCall(delegate.clone())
        override fun request(): Request = delegate.request()
        override fun timeout(): Timeout = delegate.timeout()
        override fun isExecuted(): Boolean = delegate.isExecuted
        override fun isCanceled(): Boolean = delegate.isCanceled
        override fun cancel() { delegate.cancel() }
    }
}