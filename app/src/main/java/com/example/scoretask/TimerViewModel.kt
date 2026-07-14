package com.example.scoretask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.scoretask.model.SessionStatus

class TimerViewModel( val repo: TaskRepository) : ViewModel () {

    //   public val intentChannel = Channel<TimerIntent>(Channel.UNLIMITED)

    // private val _stateIntent = MutableStateFlow(TimerIntent())
    // val stateIntent = _stateIntent.asStateFlow()

    private val _state = MutableStateFlow(TimerState())
    val state = _state.asStateFlow()


    init {
        getTaskExpectTime()
    }

    fun onIntent(intent: TimerIntent) {

        viewModelScope.launch() {
            //_state.collect {intent->
            when (intent) {

                TimerIntent.StartTimer ->

                    /* _state.update {
                        it.copy(isTimerRunning = true)
                    }*/
                    startTimer()

                TimerIntent.PauseTimer -> pauseTimer()

                TimerIntent.ResetTimer -> resetTimer()


                // لو عندكِ أكشنز تانية بتغطيها هنا...
                else -> {}
            }
        }
    }


    private var timerJob: Job? = null

    private fun startTimer() {

        if (_state.value.isRunning) return


        if (_state.value.currentTime <= 0L) {
            // يمكنك توجيهه للتقفيل مباشرة أو عدم فعل شيء لمنع الـ Bug
           // onTimerSuccessfullyFinished()
            return
        }



        _state.update {

            it.copy(status = SessionStatus.RUNNING)

        }

        timerJob = viewModelScope.launch {

            while (_state.value.currentTime > 0 &&
                _state.value.isRunning
            ) {

                delay(100)

                val newTime =
                    _state.value.currentTime - 100

                _state.update {
                    it.copy(
                        currentTime = newTime,
                        // تأمين الحماية من القسمة على صفر
                        value = if (it.totalTime > 0) newTime / it.totalTime.toFloat() else 0f
                    )
                }

            }



            if (_state.value.currentTime <= 0) {
                onTimerSuccessfullyFinished()
            }
        }

    }


    private fun onTimerSuccessfullyFinished() {
        timerJob?.cancel() // تأمين لإيقاف الـ Job

        _state.update {
            it.copy(
                status = SessionStatus.FINISHED,
                currentTime = 0L,
                value = 0f
            )
        }
    }

    private fun pauseTimer() {
        timerJob?.cancel()

        _state.update {
            it.copy(status = SessionStatus.PAUSED)
        }
    }


        private fun resetTimer() {
            timerJob?.cancel()
            _state.value = TimerState()
        }




/*
    private fun  getTaskExpectTime(){

        viewModelScope.launch (){

           // _state.update { it.copy(isLoading = true) }

            try {
// 🎯 1. نستخدم دالة first() بدلاً من collect لقرأة البيانات "مرة واحدة فقط" وقفل خيط الاستماع
                val latestTasks = repo.getAllTasks().first()

                val taskDuration = latestTasks.firstOrNull()?.defaultEstimateMs ?: 0L
                // نكلم الداتا بيز في الخلفية (مثال لجلب اللستة)
              //  repo.getAllTasks().collect { latestTasks ->
                    // أول ما الداتا ترجع، نحدث اللستة ونقفل اللودينج
                    _state.update {
                        it.copy(

                            totalTime = latestTasks.firstOrNull()?.defaultEstimateMs ?:0L,
                            currentTime = latestTasks.firstOrNull()?.defaultEstimateMs ?:0L

                        )
                    }
              //  }
            } catch (e: Exception) {
                // لو حصل أي خطأ نسيفه جوه الـ State عشان الشاشة تعرضه

                }
            }
        }*/

    private fun getTaskExpectTime() {
        viewModelScope.launch {
            try {
                // 🎯 1. نقرأ أول لستة تطلع من الداتا بيز ونقفل خيط الاستماع فوراً بـ .first()
                val allTasksList = repo.getAllTasks().first()

                // 🎯 2. ناخد آخر عنصر تم حفظه في اللستة (الأحدث) باستخدام .lastOrNull() على اللستة
                val latestTask = allTasksList.firstOrNull()

                if (latestTask != null) {
                    val taskDuration = latestTask.defaultEstimateMs

                    _state.update {
                        it.copy(
                            totalTime = taskDuration,
                            currentTime = taskDuration,
                            value = 1.0f // التايمر يبدأ والدائرة كاملة 100%
                        )
                    }
                } else {
                    // لو الداتا بيز فاضية تماماً نضع وقت افتراضي (مثلاً 5 دقائق) عشان الشاشة متظهرش أصفار
                    _state.update {
                        it.copy(
                            totalTime = 6000L, //5 * 60 * 1000L,
                            currentTime = 6000L, //5 * 60 * 1000L,
                            value = 1.0f
                        )
                    }
                }
            } catch (e: Exception) {
                // في حالة حدوث أي خطأ لا قدر الله
                e.printStackTrace()
            }
        }
    }








}











