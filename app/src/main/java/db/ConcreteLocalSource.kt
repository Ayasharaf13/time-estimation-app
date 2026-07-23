package db

import android.annotation.SuppressLint
import android.content.Context
import com.example.scoretask.model.SessionStatus
import com.example.scoretask.model.TaskSessionEntity
import com.example.scoretask.model.TaskTemplateEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.util.Calendar


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

    override fun getEstimationAccuracy(
        startOfDay: Long,
        endOfDay: Long,
        status: SessionStatus
    ): Flow<Double> {
        return  taskSessionDao. getEstimationAccuracy(startOfDay,endOfDay,status)
    }

    override fun getTotalFocusTimeAllTime(status: List<SessionStatus>): Flow<Long> {
        return taskSessionDao.getTotalFocusTimeAllTime(status)
    }


    override fun getAllTimeSessionCount(status: SessionStatus): Flow<Long> {

        return taskSessionDao.getAllTimeSessionCount(status)
    }

    override fun getDayChartPoints(): Flow<List<Number>> {

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val startOfDay = calendar.timeInMillis

        // 6 ساعات بالمللي ثانية
        val sixHoursMs = 6 * 60 * 60 * 1000L

        // 1. Morning (00:00 - 06:00)
        val p1Flow = taskSessionDao.getEstimationAccuracy(startOfDay, startOfDay + sixHoursMs)
        // 2. Afternoon (06:00 - 12:00)
        val p2Flow = taskSessionDao.getEstimationAccuracy(startOfDay + sixHoursMs, startOfDay + (2 * sixHoursMs))
        // 3. Evening (12:00 - 18:00)
        val p3Flow = taskSessionDao.getEstimationAccuracy(startOfDay + (2 * sixHoursMs), startOfDay + (3 * sixHoursMs))
        // 4. Night (18:00 - 24:00)
        val p4Flow = taskSessionDao.getEstimationAccuracy(startOfDay + (3 * sixHoursMs), startOfDay + (4 * sixHoursMs))

        // دمج الـ Flows الأربعة في قائمة واحدة للشارت
        return combine(p1Flow, p2Flow, p3Flow, p4Flow) { p1, p2, p3, p4 ->
            listOf(
                p1 ?: 0.0,
                p2 ?: 0.0,
                p3 ?: 0.0,
                p4 ?: 0.0
            )
        }
    }

    override fun getWeekChartPoints(): Flow<List<Number>> {

        val calendar = Calendar.getInstance().apply {
            firstDayOfWeek = Calendar.SATURDAY // بداية الأسبوع السبت
            set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val dayMs = 24 * 60 * 60 * 1000L
        val startOfSaturday = calendar.timeInMillis

        // إنشاء Flow لكل يوم من الأيام السبعة
        val dayFlows = (0..6).map { dayIndex ->
            val dayStart = startOfSaturday + (dayIndex * dayMs)
            val dayEnd = dayStart + dayMs - 1
            taskSessionDao.getEstimationAccuracy(dayStart, dayEnd)
        }

        // دمج السبعة Flows في قائمة واحدة
        return combine(dayFlows) { accuracies ->
            accuracies.map { (it as? Double) ?: 0.0 }
        }
    }

    override fun getMonthChartPoints(): Flow<List<Number>> {

        val calendar = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, 1) // بداية الشهر الحالي
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val startOfMonth = calendar.timeInMillis
        val weekMs = 7 * 24 * 60 * 60 * 1000L

        val weekFlows = (0..3).map { weekIndex ->
            val weekStart = startOfMonth + (weekIndex * weekMs)
            val weekEnd = weekStart + weekMs - 1
            taskSessionDao.getEstimationAccuracy(weekStart, weekEnd)
        }

        return combine(weekFlows) { accuracies ->
            accuracies.map { (it as? Double) ?: 0.0 }
        }
    }


}