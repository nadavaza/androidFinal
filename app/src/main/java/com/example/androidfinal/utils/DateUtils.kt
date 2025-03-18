package com.example.androidfinal.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {
    fun formatTimestamp(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) // ✅ Format: dd/MM/yyyy
        return sdf.format(Date(timestamp)) // ✅ Convert to Date and format
    }
}