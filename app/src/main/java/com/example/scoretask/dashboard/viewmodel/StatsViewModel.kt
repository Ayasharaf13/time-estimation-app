package com.example.scoretask.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scoretask.repository.TaskRepository
import com.example.scoretask.dashboard.contract.StateIntent
import com.example.scoretask.dashboard.contract.StatsState
import com.example.scoretask.model.SessionStatus
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StatsViewModel(private val repository: TaskRepository) : ViewModel() {


    private val _state = MutableStateFlow(StatsState())
    val state = _state.asStateFlow()


    init {
        getTotalFocusTimeAllTime()
        getAllTimeSessionCount()
        onTabChanged(0)
    }

    fun getTotalFocusTimeAllTime() {

        viewModelScope.launch() {

            repository.getTotalFocusTimeAllTime(

                status = listOf(SessionStatus.FINISHED, SessionStatus.CANCELED)
            ).collect { totalFocusAllTimeMS ->

                val totalMinutes = totalFocusAllTimeMS / 60000
                val remainingSeconds = (totalFocusAllTimeMS % 60000) / 1000
                _state.update { currentState ->
                    currentState.copy(
                        totalFocusTimeMinutes = totalMinutes,
                        totalFocusTimeSeconds = remainingSeconds
                    )


                }
            }
        }
    }

    fun getAllTimeSessionCount() {

        viewModelScope.launch() {

            repository.getAllTimeSessionCount(

            ).collect { totalSessionAllTime ->


                _state.update { currentState ->
                    currentState.copy(
                        totalSessionCount = totalSessionAllTime
                    )


                }
            }
        }
    }


    fun onIntent(intent: StateIntent) {

        viewModelScope.launch() {

            when (intent) {

                is StateIntent.ChangeTab -> onTabChanged(intent.tabIndex)


            }
        }
    }


    private var chartJob: Job? = null

    private fun onTabChanged(tabIndex: Int) {
        // 🎯 حماية حاسمة: إذا كان الضغط على نفس الـ Tab المختار حالياً، تجاهل الطلب تماماً!
        if (tabIndex == _state.value.selectedTab && chartJob?.isActive == true) {
            return
        }
        // 🎯 1. إلغاء الـ Job القديمة فوراً قبل أي عمل لمنع تداخل الاستعلامات
        chartJob?.cancel()

        // 🎯 2. تحديد الأصفار الافتراضية بناءً على طول الـ Tab المختار
        val defaultEmptyPoints = when (tabIndex) {
            0 -> listOf(0, 0, 0, 0)          // Day: 4 فترات
            1 -> listOf(0, 0, 0, 0, 0, 0, 0) // Week: 7 أيام
            2 -> listOf(0, 0, 0, 0)          // Month: 4 أسابيع
            else -> listOf(0, 0, 0, 0, 0, 0, 0)//(0, 0, 0, 0, 0, 0, 0)
        }

        // 🎯 3. تحديث الـ UI فوراً بالتبويب الجديد مع الأصفار المبدئية
        _state.update { currentState ->
            currentState.copy(
                selectedTab = tabIndex,
                chartPoints = defaultEmptyPoints
            )
        }

        // 🎯 4. إسناد الـ Job المباشر للاستماع إلى بيانات Room الحقيقية
        chartJob = viewModelScope.launch {
            val pointsFlow = when (tabIndex) {
                0 -> repository.getDayChartPoints()
                1 -> repository.getWeekChartPoints()
                2 -> repository.getMonthChartPoints()
                else -> flowOf(defaultEmptyPoints)
            }

            pointsFlow.collect { points ->
                _state.update { currentState ->
                    val finalPoints =
                        if (points.isEmpty() || points.size != defaultEmptyPoints.size) {
                            defaultEmptyPoints
                        } else {
                            points
                        }

                    currentState.copy(
                        chartPoints = finalPoints,
                        selectedTab = tabIndex
                    )
                }
            }
        }


    }

}