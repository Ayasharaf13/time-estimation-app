package com.example.scoretask

import com.example.scoretask.model.SessionStatus
import com.example.scoretask.model.TaskSessionEntity
import com.example.scoretask.model.TaskTemplateEntity
import db.LocalSource
import kotlinx.coroutines.flow.Flow


class TaskRepositoryImpl private constructor(
    private val localSource: LocalSource // بنستقبل الـ LocalSource هنا
) : TaskRepository {


    companion object {
        private var instance: TaskRepositoryImpl? = null

        // بنعمل Singleton للـ Repo برضه عشان نضمن إن الأبلكيشن كله بيقرا من مكان واحد
        fun getInstance(localSource: LocalSource): TaskRepositoryImpl {
            if (instance == null) {
                instance = TaskRepositoryImpl(localSource)
            }
            return instance as TaskRepositoryImpl
        }
    }
    override suspend fun insertTask(task: TaskTemplateEntity): Long {
        return localSource.insertTask(task)
    }

    override suspend fun updateTask(task: TaskTemplateEntity) {
        localSource.updateTask(task)
    }

    override suspend fun archiveTask(taskId: Long) {
       localSource.archiveTask(taskId)
    }

    override suspend fun restoreTask(taskId: Long) {
       localSource.restoreTask(taskId)
    }

    override fun getAllTasks(): Flow<List<TaskTemplateEntity>> {
     return localSource.getAllTasks()
    }

    override suspend fun getTaskById(taskId: Long): TaskTemplateEntity? {
        return localSource.getTaskById(taskId)
    }

    override suspend fun isTitleExists(title: String): Boolean {
       return localSource.isTitleExists(title)
    }

    override suspend fun deleteTaskById(taskId: Long): Int {
        return localSource.deleteTaskById(taskId)
    }

    override suspend fun insertSession(session: TaskSessionEntity): Long {
       return localSource.insertSession(session)
    }

    override suspend fun updateSession(session: TaskSessionEntity) {
        localSource.updateSession(session)
    }

    override fun getSessionCountForDay(
        startOfDay: Long,
        endOfDay: Long,
        status: SessionStatus
    ): Flow<Int> {
        return localSource.getSessionCountForDay(startOfDay,endOfDay,status)
    }

    override suspend fun completeSession(
        sessionId: Long,
        status: SessionStatus,
        completedAt: Long,
        actualDuration: Long

    ) {
        localSource.completeSession(sessionId,status,completedAt,actualDuration)
    }

    override fun getTotalFocusTimeForDay(
        startOfDay: Long,
        endOfDay: Long,
        status:List<SessionStatus>
    ): Flow<Long> {
      return localSource.getTotalFocusTimeForDay(startOfDay,endOfDay,status)
    }

    override suspend fun updateSessionState(
        sessionId: Long,
        status: SessionStatus,
        completedAt: Long
    ) {
        localSource.updateSessionState(sessionId,status,completedAt)
    }

    override fun getEstimationAccuracy(
        startOfDay: Long,
        endOfDay: Long,
        status: SessionStatus
    ): Flow<Double> {
        return localSource.getEstimationAccuracy(startOfDay,endOfDay,status)
    }



    override fun getTotalFocusTimeAllTime(status: List<SessionStatus>): Flow<Long> {

        return localSource.getTotalFocusTimeAllTime(status)
    }

    override fun getAllTimeSessionCount(status: SessionStatus): Flow<Long> {

        return localSource.getAllTimeSessionCount(status)
    }

    override fun getDayChartPoints(): Flow<List<Number>> {
        return localSource.getDayChartPoints()
    }

    override fun getWeekChartPoints(): Flow<List<Number>> {
       return localSource.getWeekChartPoints()
    }

    override fun getMonthChartPoints(): Flow<List<Number>> {
        return localSource.getMonthChartPoints()
    }


}