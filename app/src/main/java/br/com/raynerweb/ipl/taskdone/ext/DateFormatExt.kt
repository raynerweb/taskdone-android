package br.com.raynerweb.ipl.taskdone.ext

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

const val DEFAULT_FORMAT = "dd/MM/yyyy"

private fun getDateFormat(dateFormat: String): DateFormat {
    val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
    formatter.isLenient = false
    return formatter
}

fun Date.format(format: String = DEFAULT_FORMAT): String {
    return getDateFormat(format).format(this)
}

fun String.toDate(format: String = DEFAULT_FORMAT): Date {
    return getDateFormat(format).parse(this)
        ?: throw ParseException("Error on parsing deadline", 0)
}