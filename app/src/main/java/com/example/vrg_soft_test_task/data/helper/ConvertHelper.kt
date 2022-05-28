package com.example.vrg_soft_test_task.data.helper

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.util.*
import java.util.concurrent.TimeUnit

@SuppressLint("NewApi")
fun convertToGmt(dateTime: Double): String {
    val timeToLong = dateTime.toLong()
    val localTime = Instant.ofEpochSecond(timeToLong)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()

    val format = SimpleDateFormat("yyyy-MM-dd")
    val past = format.parse(localTime.toString())
    val now = Date()

    val minutesL = TimeUnit.MILLISECONDS.toMinutes(now.time - past.time)
    val hoursL = TimeUnit.MILLISECONDS.toHours(now.time - past.time)

    val minutes = TimeUnit.MILLISECONDS.toMinutes(now.time - past.time).toString() + " minutes ago"
    val hours = TimeUnit.MILLISECONDS.toHours(now.time - past.time).toString() + " hours ago"
    val days = TimeUnit.MILLISECONDS.toDays(now.time - past.time).toString() + " days ago"

    return if (minutesL < 59 && hoursL == 0L)
        minutes
    else if (minutesL > 59 && hoursL < 24)
        hours
    else
        days
}