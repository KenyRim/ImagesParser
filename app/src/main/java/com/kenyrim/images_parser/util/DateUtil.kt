package com.kenyrim.images_parser.util

import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateUtil {

    private var newDate: String = ""
    private var currentTime: String = ""

    fun getDate(date: Date): String {
        try {
            val fromFormat = SimpleDateFormat("EEE MMM ddd HH:mm:ss z yyyy", Locale.UK)
            val toFormat = SimpleDateFormat("yyyy-MM-dd", Locale.UK)
            val time: Date? = fromFormat.parse(date.toString())
            Log.e("getDate", currentTime)
            newDate = toFormat.format(time ?: "")

        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return newDate
    }
}