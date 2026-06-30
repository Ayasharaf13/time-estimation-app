package com.example.scoretask

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

    suspend fun deleteTaskById(taskId: Long): Int
}