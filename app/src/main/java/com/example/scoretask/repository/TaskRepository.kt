package com.example.scoretask.repository

import androidx.room.Query
import com.example.scoretask.model.SessionStatus
import com.example.scoretask.model.TaskSessionEntity
import com.example.scoretask.model.TaskTemplateEntity
import kotlinx.coroutines.flow.Flow

interface TaskRepository {


    suspend fun insertTask(task: TaskTemplateEntity): Long
    suspend fun updateTask(task: TaskTemplateEntity)
    suspend fun archiveTask(taskId: Long)
    suspend fun restoreTask(taskId: Long)
    fun getAllTasks(): Flow<List<TaskTemplateEntity>>
    suspend fun getTaskById(taskId: Long): TaskTemplateEntity?
    suspend fun isTitleExists(title: String): Boolean

    suspend fun deleteTasksById(ids: List<Long>): Int


    suspend fun deleteTaskById(taskId: Long): Int
    suspend fun insertSession(session: TaskSessionEntity): Long

    suspend fun updateSession(session: TaskSessionEntity)

    fun getSessionCountForDay(
        startOfDay: Long,
        endOfDay: Long,
        status: SessionStatus = SessionStatus.IDLE
    ): Flow<Int>

    suspend fun completeSession(
        sessionId: Long,
        status: SessionStatus,
        completedAt: Long,
        actualDuration: Long

    )

    fun getTotalFocusTimeForDay(
        startOfDay: Long,
        endOfDay: Long,
        status: List<SessionStatus>
    ): Flow<Long>


    suspend fun updateSessionState(
        sessionId: Long,
        status: SessionStatus,
        completedAt: Long
    )

    fun getEstimationAccuracy(
        startOfDay: Long,
        endOfDay: Long,
        status: SessionStatus = SessionStatus.FINISHED
    ): Flow<Double>


    fun getTotalFocusTimeAllTime(
        status: List<SessionStatus>
    ): Flow<Long>


    fun getAllTimeSessionCount(
        status: SessionStatus = SessionStatus.FINISHED
    ): Flow<Long>


    fun getDayChartPoints(): Flow<List<Number>>
    fun getWeekChartPoints(): Flow<List<Number>>
    fun getMonthChartPoints(): Flow<List<Number>>


    suspend fun addExtensionToSession(
        sessionId: Long,
        addedExtensionMs: Long
    )

    suspend fun adjustActualDurationWithGap(
        sessionId: Long,
        additionalMs: Long,
        status: SessionStatus = SessionStatus.FINISHED,
        completedAt: Long = System.currentTimeMillis()
    )


    @Query("SELECT title FROM task_templates WHERE task_id= :taskId")
    fun getTaskTitleFlow(taskId: Long): Flow<String>

    @Query("UPDATE task_templates SET title = :newTitle WHERE task_id = :taskId")
    suspend fun updateTaskTitle(taskId: Long, newTitle: String)


}