package com.mudrichenkoevgeny.core.common.util

import java.util.Locale

fun Int.convertMinutesToDurationTime(): String {
    val hours = this / 60
    val minutes = this % 60
    return String.format(Locale.getDefault(), "%02d:%02d", hours, minutes)
}