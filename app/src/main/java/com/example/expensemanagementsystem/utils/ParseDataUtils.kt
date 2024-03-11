package com.example.expensemanagementsystem.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun stringToDate(date: String): Date? {
    val format = SimpleDateFormat("dd/MM/yyyy",Locale.getDefault())
    try {
        val date: Date = format.parse(date)
        return date
        System.out.println(date)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return null
}