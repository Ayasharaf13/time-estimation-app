package db

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.scoretask.model.SessionStatus
import com.example.scoretask.model.TaskSessionEntity
import com.example.scoretask.model.TaskTemplateEntity
import kotlinx.coroutines.flow.Flow


interface LocalSource {

    suspend fun insertTask(task: TaskTemplateEntity): Long
    suspend fun updateTask(task: TaskTemplateEntity)
    suspend fun archiveTask(taskId: Long)
    suspend fun restoreTask(taskId: Long)
    fun getAllTasks(): Flow<List<TaskTemplateEntity>>
    suspend fun getTaskById(taskId: Long): TaskTemplateEntity?
    suspend fun isTitleExists(title: String): Boolean

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
        status:List<SessionStatus>
    ): Flow<Long>

    suspend fun updateSessionState(
        sessionId: Long,
        status: SessionStatus,
        completedAt: Long
    )

    fun getDailyEstimationAccuracy(
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

}