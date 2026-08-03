package com.example.scoretask.timer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scoretask.repository.TaskRepository
import com.example.scoretask.timer.contract.TimerIntent
import com.example.scoretask.timer.contract.TimerState
import com.example.scoretask.model.SessionStatus
import com.example.scoretask.model.TaskSessionEntity
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TimerViewModel(
    val repo: TaskRepository,
    val extraTimeMs: Int,
    val isExtraTime: Boolean,
    val sessionId: Long
) : ViewModel() {

    //   public val intentChannel = Channel<TimerIntent>(Channel.UNLIMITED)

    // private val _stateIntent = MutableStateFlow(TimerIntent())
    // val stateIntent = _stateIntent.asStateFlow()

    private val _state = MutableStateFlow(TimerState())
    val state = _state.asStateFlow()

    private var currentSessionId: Long = 0L

    val extraMs = extraTimeMs * 60 * 1000L

    init {


        _state.update { currentState ->
            currentState.copy(
                isExtraTime = isExtraTime,
                selectExtraTime = extraTimeMs,
                idSession = sessionId,
                currentTime = if (isExtraTime) extraMs else 0L,
                totalTime = if (isExtraTime) extraMs else 0L

            )
        }

        getTaskExpectTime()

    }

    fun onIntent(intent: TimerIntent) {

        viewModelScope.launch() {
            //_state.collect {intent->
            when (intent) {

                is TimerIntent.StartTimer -> startNewSession(
                    intent.templateId,
                    intent.estimateMs
                )//startTimer(intent.templateId)
                is TimerIntent.PauseTimer -> pauseTimer(intent.templateId)
                is TimerIntent.ResetTimer -> resetTimer(intent.templateId)

                TimerIntent.EndSessionClicked -> {

                    val currentlyRunning = _state.value.isRunning

                    // 1. إذا كان التايمر يعمل، نقوم بإيقافه موقتاً
                    if (currentlyRunning) {
                        pauseTimer(_state.value.idTask)
                    }

                    // نفتح الـ Dialog فوراً عبر الـ State
                    _state.update {
                        it.copy(
                            showEndSessionDialog = true,
                            wasRunningBeforeDialog = currentlyRunning
                        )
                    }
                }

                TimerIntent.DismissDialog -> {
                    val shouldResume = _state.value.wasRunningBeforeDialog

                    if (shouldResume) {
                        startTimer(state.value.idTask)
                    }
                    // نغلق الـ Dialog
                    _state.update {
                        it.copy(
                            showEndSessionDialog = false,
                            wasRunningBeforeDialog = false
                        )
                    }
                }

                TimerIntent.ConfirmFinishEarly -> {
                    _state.update {
                        it.copy(
                            showEndSessionDialog = false,
                            wasRunningBeforeDialog = false
                        )
                    }
                    finishSession(SessionStatus.FINISHED) // 🟢 تحديث بنجاح
                }

                TimerIntent.ConfirmGiveUp -> {
                    _state.update {
                        it.copy(
                            showEndSessionDialog = false,
                            wasRunningBeforeDialog = false
                        )
                    }
                    finishSession(SessionStatus.CANCELED)  // 🔴 تحديث كـ استسلام
                }


                // لو عندكِ أكشنز تانية بتغطيها هنا...
                else -> {}
            }
        }
    }


    private var timerJob: Job? = null

    private fun startTimer(templateId: Long) {

        if (_state.value.isRunning) return


        if (_state.value.isExtraTime && _state.value.currentTime <= 0L) {
            // val extraTimeMs = _state.value.extraTimeFromMunToMill // أو selectExtraTime.toLong() * 60 * 1000L
            _state.update {
                it.copy(
                    currentTime = extraMs,
                    totalTime = extraMs
                )
            }
        }

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
                finishSession(SessionStatus.FINISHED)
            }
        }

    }


    private fun finishSession(status: SessionStatus) {
        timerJob?.cancel() // تأمين لإيقاف الـ Job
        val actualFocusedTime = _state.value.totalTime - _state.value.currentTime
        _state.update {
            it.copy(
                status = status,
                currentTime = 0L,
                value = 0f
            )


        }


        viewModelScope.launch {
            if (currentSessionId != 0L) {

                repo.completeSession(
                    currentSessionId,
                    status,
                    System.currentTimeMillis(),
                    actualFocusedTime
                )


            }
        }
    }




    private fun pauseTimer(templateId: Long) {
        timerJob?.cancel()

        _state.update {
            it.copy(status = SessionStatus.PAUSED)
        }

        viewModelScope.launch {
            if (currentSessionId != 0L) {
                repo.updateSessionState(
                    currentSessionId,
                    SessionStatus.PAUSED,
                    System.currentTimeMillis()
                )


            }
        }
    }


    private fun resetTimer(templateId: Long) {
        timerJob?.cancel()
        _state.value = TimerState()

        viewModelScope.launch {
            if (currentSessionId != 0L) {
                // نحدث الجلسة الحالية لتصبح مكتملة ونحسب المدة الفعلية
                repo.updateSessionState(
                    currentSessionId,
                    SessionStatus.IDLE,
                    System.currentTimeMillis()
                )
                /* repo.updateSession(
                     TaskSessionEntity(
                         id = currentSessionId,
                         taskTemplateId = templateId,
                         status = SessionStatus.IDLE

                     )
                 )*/
                // نصفر الـ ID لكي نقوم بعمل Insert جديد في المرة القادمة
                currentSessionId = 0L
            }
        }

    }




    private fun getTaskExpectTime() {
        viewModelScope.launch {
            try {
                // 🎯 1. نقرأ أول لستة تطلع من الداتا بيز ونقفل خيط الاستماع فوراً بـ .first()
                val allTasksList = repo.getAllTasks().first()

                // 🎯 2. ناخد آخر عنصر تم حفظه في اللستة (الأحدث) باستخدام .lastOrNull() على اللستة
                val latestTask = allTasksList.firstOrNull()

                if (latestTask != null) {
                    val taskDuration = latestTask.defaultEstimateMs

                    val idTask = latestTask.id


                    _state.update { currentState ->
                        val newCurrentTime = if (currentState.isExtraTime) extraMs else taskDuration
                        val newTotalTime = if (currentState.isExtraTime) extraMs else taskDuration
                        currentState.copy(
                            totalTime = newCurrentTime,//taskDuration,
                            currentTime = newTotalTime, //taskDuration,
                            idTask = idTask,

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


    fun startNewSession(templateId: Long, estimateMs: Long) {
        if (_state.value.isRunning) return

        // حماية: لو الوقت صفر لا نفعل شيء
        if (_state.value.currentTime <= 0L) return

        viewModelScope.launch {
            if (currentSessionId == 0L) {
                // 1. حالة جلسة جديدة كلياً: نقوم بإنشائها وحفظها في قاعدة البيانات أولاً
                val newSession = TaskSessionEntity(
                    taskTemplateId = templateId,
                    originalEstimateMs = estimateMs,
                    status = SessionStatus.RUNNING,
                    actualDurationMs = estimateMs,

                    startedAt = System.currentTimeMillis()
                )
                currentSessionId = repo.insertSession(newSession)

                _state.update {
                    it.copy(

                        idSession = currentSessionId
                    )
                }

            } else {
                // 2. حالة استئناف جلسة موجودة (Resume): نحدث حالتها فقط في قاعدة البيانات
                //  repo.updateSession(currentSessionId, SessionStatus.RUNNING)

                repo.updateSession(
                    TaskSessionEntity(
                        taskTemplateId = templateId,

                        status = SessionStatus.IDLE
                    )
                )


            }

            // 3. بعد إتمام عمليات قاعدة البيانات بأمان، نبدأ التايمر الفعلي في الـ UI
            startTimer(templateId)

        }
    }


}