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



}