package db

import android.annotation.SuppressLint
import android.content.Context
import com.example.scoretask.model.SessionStatus
import com.example.scoretask.model.TaskSessionEntity
import com.example.scoretask.model.TaskTemplateEntity
import kotlinx.coroutines.flow.Flow


class ConcreteLocalSource : LocalSource {


    val context: Context
    var taskTemplateDao: TaskTemplateDao
    var taskSessionDao: TaskSessionDao


    companion object {
        @SuppressLint("StaticFieldLeak")
        private var localsource: ConcreteLocalSource? = null

        fun getInstance(con: Context): ConcreteLocalSource {
            if (localsource == null) {
                localsource = ConcreteLocalSource(con)
            }
            return localsource as ConcreteLocalSource
        }

    }

    private constructor (con: Context) {

        this.context = con
        val db: AppDatabase = AppDatabase.getInstance(context.applicationContext)
        taskTemplateDao = db.taskTemplateDao()
        taskSessionDao = db.taskSessionDao()


    }

    override suspend fun insertTask(task: TaskTemplateEntity): Long {
     return taskTemplateDao.insertTask(task)
    }

    override suspend fun updateTask(task: TaskTemplateEntity) {
      taskTemplateDao.updateTask(task)
    }

    override suspend fun archiveTask(taskId: Long) {
     taskTemplateDao.archiveTask(taskId)
    }

    override suspend fun restoreTask(taskId: Long) {
      taskTemplateDao.restoreTask(taskId)
    }

    override fun getAllTasks(): Flow<List<TaskTemplateEntity>>  {
       return taskTemplateDao.getAllTasks()
    }

    override suspend fun getTaskById(taskId: Long): TaskTemplateEntity? {
     return  taskTemplateDao.getTaskById(taskId)
    }

    override suspend fun isTitleExists(title: String): Boolean {
       return taskTemplateDao.isTitleExists(title)
    }

    override suspend fun deleteTaskById(taskId: Long): Int {
      return taskTemplateDao.deleteTaskById(taskId)
    }

    override suspend fun insertSession(session: TaskSessionEntity): Long {
        return taskSessionDao.insertSession(session)
    }

    override suspend fun updateSession(session: TaskSessionEntity) {
     taskSessionDao.updateSession(session)
    }

    override fun getSessionCountForDay(
        startOfDay: Long,
        endOfDay: Long,
        status: SessionStatus
    ): Flow<Int> {
       return taskSessionDao.getSessionCountForDay(startOfDay,endOfDay,status)
    }

    override suspend fun completeSession(
        sessionId: Long,
        status: SessionStatus,
        completedAt: Long,
        actualDuration: Long
    ) {
        taskSessionDao.completeSession(sessionId,status,completedAt,actualDuration)
    }



    override fun getTotalFocusTimeForDay(
        startOfDay: Long,
        endOfDay: Long,
        status: List<SessionStatus>
    ): Flow<Long> {
       return  taskSessionDao.getTotalFocusTimeForDay(startOfDay,endOfDay,status)
    }

    override suspend fun updateSessionState(
        sessionId: Long,
        status: SessionStatus,
        completedAt: Long
    ) {
        taskSessionDao.updateSessionState(sessionId,status,completedAt)
    }

    override fun getDailyEstimationAccuracy(
        startOfDay: Long,
        endOfDay: Long,
        status: SessionStatus
    ): Flow<Double> {
        return  taskSessionDao. getDailyEstimationAccuracy(startOfDay,endOfDay,status)
    }

    override fun getTotalFocusTimeAllTime(status: List<SessionStatus>): Flow<Long> {
        return taskSessionDao.getTotalFocusTimeAllTime(status)
    }

    override fun getAllTimeSessionCount(status: SessionStatus): Flow<Long> {

        return taskSessionDao.getAllTimeSessionCount(status)
    }


}