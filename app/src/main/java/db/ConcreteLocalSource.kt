package db

import android.annotation.SuppressLint
import android.content.Context
import com.example.scoretask.model.TaskTemplateEntity
import kotlinx.coroutines.flow.Flow


class ConcreteLocalSource : LocalSource {


    val context: Context
    var taskTemplateDao: TaskTemplateDao


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


}