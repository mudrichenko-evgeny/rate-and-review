package com.mudrichenkoevgeny.core.common.util

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

fun Int.formatToNumberWithSpaces(): String {
    val symbols = DecimalFormatSymbols(Locale.getDefault()).apply { groupingSeparator = ' ' }
    val formatter = DecimalFormat("#,###", symbols)
    return formatter.format(this)
}