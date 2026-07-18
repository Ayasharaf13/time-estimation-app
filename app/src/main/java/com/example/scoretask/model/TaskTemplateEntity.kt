package com.example.scoretask.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "task_templates",
    indices = [
        Index(value = ["title"], unique = true),
        Index(value = ["is_archived"])
    ]
)

data class TaskTemplateEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "task_id")
    val id: Long = 0L,

    @ColumnInfo(name = "title")
    val title: String = "",

    @ColumnInfo(name = "default_estimate_ms")
    val defaultEstimateMs: Long = 0L,



    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "is_archived")
    val isArchived: Boolean = false,

    // Future cloud sync
    @ColumnInfo(name = "uuid")
    val uuid: String? = null,

    @ColumnInfo(name = "last_modified")
    val lastModified: Long = System.currentTimeMillis()
)

val TaskTemplateEntity.totalMinutes: Long
    get() = defaultEstimateMs / (1000 * 60)