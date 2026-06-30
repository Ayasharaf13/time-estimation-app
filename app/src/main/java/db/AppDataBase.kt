package db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.scoretask.model.AppStatisticsEntity
import com.example.scoretask.model.TaskSessionEntity
import com.example.scoretask.model.TaskTemplateEntity
import com.example.scoretask.utilities.Converters


@Database(
    entities = [
        TaskTemplateEntity::class,
        TaskSessionEntity::class,
        AppStatisticsEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun taskTemplateDao(): TaskTemplateDao

    abstract fun taskSessionDao(): TaskSessionDao

    abstract fun appStatisticsDao(): AppStatisticsDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {

            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "time_estimation.db"
                )
                    .fallbackToDestructiveMigration(false)
                    .build()

                INSTANCE = instance

                instance
            }
        }
    }
}