package com.mudrichenkoevgeny.core.network.extensions

import okhttp3.Response

fun Response.responseCount(): Int {
    var count = 1
    var currentResponse: Response? = this
    while (currentResponse?.priorResponse != null) {
        count++
        currentResponse = currentResponse.priorResponse
    }
    return count
}