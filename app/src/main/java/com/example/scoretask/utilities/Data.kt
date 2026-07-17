package com.example.scoretask.utilities

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar
import java.util.TimeZone


object DateTimeUtils {



    fun getStartOfDayMillis(): Long {
        val calendar = Calendar.getInstance(TimeZone.getDefault()).apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar.timeInMillis
    }

    // يعمل بأمان على API 21 وأقل (حتى أقدم إصدارات الأندرويد) دون الحاجة لـ Desugaring
    fun getEndOfDayMillis(): Long {
        val calendar = Calendar.getInstance(TimeZone.getDefault()).apply {
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }
        return calendar.timeInMillis
    }

   /* @RequiresApi(Build.VERSION_CODES.O)
    fun getStartOfDayMillis(): Long {
        return LocalDate.now()
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli() // هيرجع مثلاً الساعة 12:00 AM بالملي ثانية للنهاردة
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getEndOfDayMillis(): Long {
        return LocalDate.now()
            .atTime(23, 59, 59, 999_000_000)
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli() // هيرجع الساعة 11:59:59 PM بالملي ثانية للنهاردة
    }*/
}