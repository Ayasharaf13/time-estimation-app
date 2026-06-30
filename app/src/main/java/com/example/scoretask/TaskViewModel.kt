package com.example.scoretask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scoretask.model.TaskTemplateEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consume
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class TaskViewModel (
    private val repository: TaskRepository
): ViewModel  (){


    private val _uiState = MutableStateFlow(TaskUiState())

    // النسخة العامة (المكشوفة للشاشة) غير قابلة للتعديل من الخارج للأمان
    val uiState: StateFlow<TaskUiState> = _uiState.asStateFlow()


    private val _state = MutableStateFlow(TaskCreationState())
    val state = _state.asStateFlow()
    // دي الطريقة المعقدة للمشاريع الضخمة (مش محتاجاها حالياً)
   // public val intentChannel = Channel<TaskIntent>(Channel.UNLIMITED)

    init {


        getAllTasks()
    }

    fun onIntent(intent: TaskIntent) {

        viewModelScope.launch (){
       //_state.collect {intent->
            when (intent) {
                is TaskIntent.TitleChanged -> {

                  _state.update {

                        it.copy(
                            title = intent.title
                        )

                    }

                }

                is TaskIntent.HoursChanged -> {

                    _state.update {

                        it.copy(
                            hours = intent.hours
                        )

                    }

                }

                is TaskIntent.MinutesChanged -> {

                    _state.update {

                        it.copy(
                            minutes = intent.minutes
                        )

                    }

                }





                is TaskIntent.SaveTask -> {
                   saveTask()
                }

                is TaskIntent.DeleteTask -> {
                    deleteTaskById(intent.taskId)
                }

                // لو عندكِ أكشنز تانية بتغطيها هنا...
                else -> {}
            }
        }
        }


    // 1️⃣ بنعدل الدالة عشان تستقبل الـ Entity اللي جاية مع الـ Intent
    private fun saveTask(/*task: TaskTemplateEntity*/) {
        viewModelScope.launch(Dispatchers.IO) {


            // الحفظ بيتم في الـ Background Thread لأعلى أداء
            try {
                // 🟢 أ: نحدث الـ State فوراً لـ Loading (إصدار إشعار للـ UI)
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }

                val state = _state.value

                val hours = state.hours.toLongOrNull() ?: 0L
                val minutes = state.minutes.toLongOrNull() ?: 0L

                val totalTime =
                    hours * 60 * 60 * 1000 +
                            minutes * 60 * 1000

                val task = TaskTemplateEntity(

                    title = state.title,

                  defaultEstimateMs = totalTime

                )

                println(task)

                // 🟢 ب: نكلم الـ Repository عشان يحفظ الكائن جوه الـ Room Database
                repository.insertTask(task)

                // 🎉 ملحوظة ذكية: بما إن getAllTasks() بتراقب الـ Room كـ Flow،
                // أول ما الـ insert يخلص، اللستة في الـ UI هتتحدث أوتوماتيك من غير ما نعمل حاجة هنا!
                _uiState.update { it.copy(isLoading = false) }

            } catch (e: Exception) {
                // 🔴 ج: لو حصل أي خطأ غير متوقع (مثلاً مفيش مساحة في الموبايل أو الكاش ضرب)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.localizedMessage ?: "Error while saving the task"
                    )
                }
            }
        }
    }
    private fun  getAllTasks(){

        viewModelScope.launch (){

            _uiState.update { it.copy(isLoading = true) }

            try {
                // نكلم الداتا بيز في الخلفية (مثال لجلب اللستة)
                repository.getAllTasks().collect { latestTasks ->
                    // أول ما الداتا ترجع، نحدث اللستة ونقفل اللودينج
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            tasksList = latestTasks,
                            errorMessage = null
                        )
                    }
                }
            } catch (e: Exception) {
                // لو حصل أي خطأ نسيفه جوه الـ State عشان الشاشة تعرضه
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = e.localizedMessage)
                }
            }
        }
    }



    private fun deleteTaskById(id: Long) {
        viewModelScope.launch {
            try {
                // نطلب من الـ Room تمسح التاسك بالـ ID بتاعها
                repository.getTaskById(id)


                // تريكة الـ copy(): اللستة هتتحدث أوتوماتيكياً لو شغالين بـ Flow من الـ Room،
                // وممكن نبعت رسالة نجاح خفيفة للمستخدم
                _uiState.update {
                    it.copy(errorMessage = "The task was successfully deleted")
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = "Deletion failed : ${e.localizedMessage}")
                }
            }
        }


    }


}