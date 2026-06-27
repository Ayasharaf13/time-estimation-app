package com.example.scoretask.utilities

import androidx.room.TypeConverter
import com.example.scoretask.model.SessionStatus


class Converters {

    @TypeConverter
    fun fromStatus(status: SessionStatus): String {
        return status.name
    }

    @TypeConverter
    fun toStatus(value: String): SessionStatus {
        return SessionStatus.valueOf(value)
    }
}