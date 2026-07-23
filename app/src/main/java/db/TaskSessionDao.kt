package db

import androidx.room.Dao
import androidx.room.Insert
import com.example.scoretask.model.SessionStatus
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.scoretask.model.TaskSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskSessionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: TaskSessionEntity): Long

    // 2. تحديث بيانات جلسة موجودة بالفعل (الـ Room بيعرفها من الـ primary key "session_id")
    @Update
    suspend fun updateSession(session: TaskSessionEntity)

    // 3. الاستعلام التلقائي اللحظي اللي كتبناه
    @Query("""
        SELECT COUNT(*) FROM task_sessions
        WHERE status = :status 
        AND created_at >= :startOfDay 
        AND created_at <= :endOfDay
    """)

    fun getSessionCountForDay(
        startOfDay: Long,
        endOfDay: Long,
        status: SessionStatus = SessionStatus.IDLE
    ): Flow<Int>

    @Query("""
        UPDATE task_sessions 
        SET status = :status, completed_at = :completedAt , actual_duration_ms = :actualDuration
        WHERE session_id = :sessionId
    """)
    suspend fun completeSession(
        sessionId: Long,
        status: SessionStatus,
        completedAt: Long,
        actualDuration: Long

    )

    @Query("""
        UPDATE task_sessions 
        SET status = :status, completed_at = :completedAt 
        WHERE session_id = :sessionId
    """)
    suspend fun updateSessionState(
        sessionId: Long,
        status: SessionStatus,
        completedAt: Long
    )


    @Query("""
    SELECT COALESCE(SUM(actual_duration_ms), 0) 
    FROM task_sessions
    WHERE status IN (:status)
    AND created_at >= :startOfDay 
    AND created_at <= :endOfDay
""")
    fun getTotalFocusTimeForDay(
        startOfDay: Long,
        endOfDay: Long,
        status:  List<SessionStatus>
    ): Flow<Long>





    @Query("""
        SELECT 
            CASE 
                -- 1. الحماية من القسمة على صفر
                WHEN SUM(actual_duration_ms) = 0 THEN 0.0
                
                -- 2. إذا استغرق المستخدم وقتاً أطول من المستهدف (تأخير)
                WHEN SUM(actual_duration_ms) > SUM(original_estimate_ms + added_extension_ms)
                THEN (CAST(SUM(original_estimate_ms + added_extension_ms) AS REAL) / SUM(actual_duration_ms)) * 100
                
                -- 3. إذا أنهى المهمة في وقتها أو أسرع
                ELSE (CAST(SUM(actual_duration_ms) AS REAL) / SUM(original_estimate_ms + added_extension_ms)) * 100
            END
        FROM task_sessions
        WHERE status = :status 
        AND created_at >= :startOfDay 
        AND created_at <= :endOfDay
    """)
    fun getEstimationAccuracy(
        startOfDay: Long,
        endOfDay: Long,
        status: SessionStatus = SessionStatus.FINISHED
    ): Flow<Double>


    @Query("""
    SELECT COALESCE(SUM(actual_duration_ms), 0) 
    FROM task_sessions
    WHERE status IN (:status)
""")
    fun getTotalFocusTimeAllTime(
        status: List<SessionStatus>
    ): Flow<Long>


    @Query("""
    SELECT COUNT(*) FROM task_sessions
    WHERE status = :status
""")
    fun getAllTimeSessionCount(
        status: SessionStatus = SessionStatus.FINISHED
    ): Flow<Long>
}


