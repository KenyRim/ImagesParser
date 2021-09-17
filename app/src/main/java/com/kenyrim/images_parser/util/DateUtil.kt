package com.kenyrim.images_parser.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateUtil {

    private var newDate: String = ""
  //  private var currentDateTime: String = ""

    val patternTime = "HH"
    val patternDate = "yyyy-MM-dd"

    fun getDate(date: Date, dateOrTime: String): String {
        try {
            val fromFormat = SimpleDateFormat("EEE MMM ddd HH:mm:ss z yyyy", Locale.UK)//TODO Date format by system region
            val toFormat = SimpleDateFormat(dateOrTime, Locale.UK)
            val time: Date? = fromFormat.parse(date.toString())
            newDate = toFormat.format(time ?: "")

        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return newDate
    }
}