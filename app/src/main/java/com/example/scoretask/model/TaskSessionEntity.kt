package com.example.scoretask.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "task_sessions",
    foreignKeys = [
        ForeignKey(
            entity = TaskTemplateEntity::class,
            parentColumns = ["task_id"],
            childColumns = ["task_template_id"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index("task_template_id"),
        Index("status"),
        Index("completed_at")
    ]
)
data class TaskSessionEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "session_id")
    val id: Long = 0L,

    @ColumnInfo(name = "task_template_id")
    val taskTemplateId: Long,//

    // User estimation
    @ColumnInfo(name = "original_estimate_ms")
    val originalEstimateMs: Long = 0L,//

    // Extension requested by the user
    @ColumnInfo(name = "added_extension_ms")
    val addedExtensionMs: Long = 0L,

    // Final duration after finishing
    @ColumnInfo(name = "actual_duration_ms")
    val actualDurationMs: Long = 0L,

    // Timer information
    @ColumnInfo(name = "started_at")
    val startedAt: Long? = null,//

    @ColumnInfo(name = "completed_at")
    val completedAt: Long? = null,

    @ColumnInfo(name = "status")
    val status: SessionStatus = SessionStatus.IDLE,//

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "last_modified")
    val lastModified: Long = System.currentTimeMillis()
)

enum class SessionStatus {
    IDLE,      // التايمر واقف أو لسه معمول له Reset
    RUNNING,   // التايمر شغال وبيعد تنازلي
    PAUSED,    // التايمر موقوف مؤقتاً
    FINISHED   // التايمر انتهى طبيعياً ووصل لـ 00:00
}