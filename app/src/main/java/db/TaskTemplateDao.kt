package db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.scoretask.model.TaskTemplateEntity
import kotlinx.coroutines.flow.Flow

@Dao
    interface TaskTemplateDao {


        // ------------------------
        // Insert
        // ------------------------

        @Insert(onConflict = OnConflictStrategy.ABORT)
        suspend fun insertTask(task: TaskTemplateEntity): Long


        // ------------------------
        // Update
        // ------------------------

        @Update
        suspend fun updateTask(task: TaskTemplateEntity)


        // ------------------------
        // Archive (Soft Delete)
        // ------------------------

        @Query("""
        UPDATE task_templates
        SET is_archived = 1
        WHERE task_id = :taskId
    """)
        suspend fun archiveTask(taskId: Long)


        // ------------------------
        // Restore Archived Task
        // ------------------------

        @Query("""
        UPDATE task_templates
        SET is_archived = 0
        WHERE task_id = :taskId
    """)
        suspend fun restoreTask(taskId: Long)


        // ------------------------
        // Display Home Screen
        // ------------------------

        @Query("""
        SELECT *
        FROM task_templates
        WHERE is_archived = 0
        ORDER BY created_at DESC
    """)
        fun getAllTasks(): Flow<List<TaskTemplateEntity>>


        // ------------------------
        // Get One Task
        // ------------------------

        @Query("""
        SELECT *
        FROM task_templates
        WHERE task_id = :taskId
    """)
        suspend fun getTaskById(taskId: Long): TaskTemplateEntity?


    @Query("DELETE FROM task_templates WHERE task_id = :taskId")
    suspend fun deleteTaskById(taskId: Long): Int

        // ------------------------
        // Check Duplicate Title
        // ------------------------

        @Query("""
        SELECT EXISTS(
            SELECT 1
            FROM task_templates
            WHERE title = :title
            AND is_archived = 0
        )
    """)
        suspend fun isTitleExists(title: String): Boolean
    }



