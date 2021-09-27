package com.kenyrim.images_parser.util

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager

object NetworkUtil {

    @SuppressLint("MissingPermission")
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        return connectivityManager?.activeNetworkInfo?.isConnected ?: false
    }
}