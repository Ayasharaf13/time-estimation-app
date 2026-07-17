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
   // @Query("SELECT COUNT(*) FROM task_sessions WHERE completed_at BETWEEN :startOfDay AND :endOfDay")
    fun getSessionCountForDay(
        startOfDay: Long,
        endOfDay: Long,
        status: SessionStatus = SessionStatus.IDLE
    ): Flow<Int>

}