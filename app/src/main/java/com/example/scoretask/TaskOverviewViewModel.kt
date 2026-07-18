package com.example.scoretask

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scoretask.model.SessionStatus
import com.example.scoretask.utilities.DateTimeUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch



class TaskOverviewViewModel(private val repository: TaskRepository): ViewModel() {


    private val _state = MutableStateFlow(TaskUiState())
    val state = _state.asStateFlow()

    init {
        getSessionCountForDay()
        getTotalFocusTimeForDay()
        getDailyEstimationAccuracy()

    }


    fun getSessionCountForDay() {
        viewModelScope.launch() {
            repository.getSessionCountForDay(
                startOfDay = DateTimeUtils.getStartOfDayMillis(),
                endOfDay = DateTimeUtils.getEndOfDayMillis(),
                status = SessionStatus.FINISHED
            ).collect { count ->
          Log.i("counnnnt",count.toString())
                _state.update { currentState ->
                    currentState.copy(sessionCount = count)
                }
            }
        }
    }


    fun getTotalFocusTimeForDay(){

        viewModelScope.launch  (){

            repository.getTotalFocusTimeForDay(
                startOfDay = DateTimeUtils.getStartOfDayMillis(),
                endOfDay = DateTimeUtils.getEndOfDayMillis(),
                status= listOf(SessionStatus.FINISHED, SessionStatus.CANCELED)
            ).collect { totalMs ->
                val totalSeconds = totalMs / 1000
                val minutes = totalSeconds / 60
                val remainingSeconds = totalSeconds % 60 // الثواني المتبقية بعد حساب الدقائق
                _state.update { currentState->
                    currentState.copy(
                        totalMinutesToday = minutes,
                        totalSecondsToday = remainingSeconds
                    )


                }
            }
        }
    }

    fun getDailyEstimationAccuracy(

    ) {
        viewModelScope.launch()
        {

            repository.getDailyEstimationAccuracy(
                startOfDay = DateTimeUtils.getStartOfDayMillis(),
                endOfDay = DateTimeUtils.getEndOfDayMillis(),
                status = SessionStatus.FINISHED
            ).collect { dailyEstimationAccuracy ->
                _state.update { currentState ->
                    currentState.copy(
                        totalDailyEstimationAccuracy = dailyEstimationAccuracy
                    )

                }


            }

        }
    }

}