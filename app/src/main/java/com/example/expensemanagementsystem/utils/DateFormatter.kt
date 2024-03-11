package com.example.expensemanagementsystem.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

object DateFormatter {

    const val DD_MM_YYYY = "dd/MM/yyyy"
    fun getTimeMils(date: String, currentFormat: String, hour:Int): String {
        val calendar = Calendar.getInstance()
        val oldDateFormat = SimpleDateFormat(currentFormat, Locale.getDefault())
        val dateObj = oldDateFormat.parse(date)
        calendar.time = dateObj!!
        calendar.set(Calendar.HOUR_OF_DAY, hour)

        return calendar.timeInMillis.toString()
    }

    fun format(
        timeStamp: String, format: String, locale: Locale? = null, isUTC: Boolean? = false
    ): String {
        val calendar = Calendar.getInstance()
        val newDateFormat = SimpleDateFormat(format, locale ?: Locale.getDefault())
        try {
            calendar.timeInMillis = timeStamp.toLong()
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        if (isUTC == true) newDateFormat.timeZone = TimeZone.getTimeZone("UTC")
        return newDateFormat.format(calendar.time)
    }

}


fun formatDate(timeStamp: String? = null, defaultFormat: String = DateFormatter.DD_MM_YYYY): String {
    return if(!timeStamp?.trim().isNullOrEmpty()){
        DateFormatter.format(timeStamp?.trim().toString(), defaultFormat)
    }else "-"
}