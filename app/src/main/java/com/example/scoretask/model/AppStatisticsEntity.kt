package com.example.scoretask.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "app_statistics")
data class AppStatisticsEntity(

    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int = 1,

    @ColumnInfo(name = "total_completed_tasks")
    val totalCompletedTasks: Long = 0,

    @ColumnInfo(name = "total_focus_time_ms")
    val totalFocusTimeMs: Long = 0,

    @ColumnInfo(name = "last_updated")
    val lastUpdated: Long = System.currentTimeMillis()
)
