package com.example.expensemanagementsystem.screens.common

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.appcompat.view.ContextThemeWrapper
import androidx.compose.runtime.MutableState
import com.example.expensemanagementsystem.R
import com.example.expensemanagementsystem.utils.DateFormatter
import com.example.expensemanagementsystem.utils.stringToDate
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@SuppressLint("SuspiciousIndentation")
fun getDatePicker(
    startDate: String = "",
    isWeekMaxDate: Boolean? = false,
    context: Context,
    birthDate: MutableState<String>,
    showFutureDate: Boolean = false,
    endTime: String = "",
    hour: String = "-1",
    onDateSelect: () -> Unit = {}
): DatePickerDialog {

    val mYear: Int
    val mMonth: Int
    val mDay: Int

    // Initializing a Calendar
    val mCalendar = Calendar.getInstance()

    // Fetching current year, month and day
    mYear = mCalendar.get(Calendar.YEAR)
    mMonth = mCalendar.get(Calendar.MONTH)
    mDay = mCalendar.get(Calendar.DAY_OF_MONTH)

    mCalendar.time = Date()

    val mDatePickerDialog = DatePickerDialog(
        context, ContextThemeWrapper(context, R.style.CalenderViewCustom).themeResId,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
//            mDate.value = "$mDayOfMonth/${mMonth + 1}/$mYear"
            birthDate.value = "$mDayOfMonth/${mMonth + 1}/$mYear"
            onDateSelect()
        }, mYear, mMonth, mDay
    )


    if (isWeekMaxDate == true) {
        val startDateMilli = DateFormatter.getTimeMils(
            startDate,
            DateFormatter.DD_MM_YYYY,
            0
        )
        val adjustedTimeAsMs = startDateMilli.toLong() + (1000 * 60 * 60 * 24 * 7)

        mDatePickerDialog.datePicker.maxDate = adjustedTimeAsMs
    }

    if (!showFutureDate) {
        mDatePickerDialog.datePicker.maxDate = Date().time
    } else {
        // Show date after today
        if (endTime.isNotEmpty()) {
            // For endtime
            if (hour == "23") {
                mDatePickerDialog.datePicker.minDate = (endTime.toLong() + 86400000)
            } else {
                mDatePickerDialog.datePicker.minDate = (endTime.toLong())
            }

        } else
            mDatePickerDialog.datePicker.minDate = (System.currentTimeMillis())
    }


    if (birthDate.value.isNotEmpty()) {
        val date = stringToDate(birthDate.value)
        if (date != null) {
            val cal = Calendar.getInstance()
            cal.time = date
            val day = cal[Calendar.DAY_OF_MONTH]
            val month = cal[Calendar.MONTH]
            val year = cal[Calendar.YEAR]

            mDatePickerDialog.datePicker.updateDate(year, month, day)
        }
    }

    return mDatePickerDialog
}


fun getTimeMils(date: String, currentFormat: String): String {
    val calendar = Calendar.getInstance()
    val oldDateFormat = SimpleDateFormat(currentFormat, Locale.getDefault())
    val dateObj = oldDateFormat.parse(date)
    calendar.time = dateObj!!
//        calendar.add(Calendar.MONTH, 1)
//    calendar.set(Calendar.HOUR_OF_DAY)

    return calendar.timeInMillis.toString()
}