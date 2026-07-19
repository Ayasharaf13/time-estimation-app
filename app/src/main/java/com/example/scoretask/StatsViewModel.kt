package com.example.scoretask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scoretask.model.SessionStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StatsViewModel(private val repository: TaskRepository): ViewModel() {


    private val _state = MutableStateFlow(StatsState())
    val state = _state.asStateFlow()



init {
    getTotalFocusTimeAllTime()
    getAllTimeSessionCount()
}

    fun  getTotalFocusTimeAllTime(){

        viewModelScope.launch  (){

            repository.getTotalFocusTimeAllTime(

                status= listOf(SessionStatus.FINISHED, SessionStatus.CANCELED)
            ).collect { totalFocusAllTimeMS ->

                val totalMinutes = totalFocusAllTimeMS / 60000
                val remainingSeconds = (totalFocusAllTimeMS % 60000) / 1000
                _state.update { currentState->
                    currentState.copy(
                        totalFocusTimeMinutes = totalMinutes,
                        totalFocusTimeSeconds = remainingSeconds
                    )


                }
            }
        }
    }

    fun  getAllTimeSessionCount(){

        viewModelScope.launch  (){

            repository.getAllTimeSessionCount(

            ).collect { totalSessionAllTime ->


                _state.update { currentState->
                    currentState.copy(
                       totalSessionCount = totalSessionAllTime
                    )


                }
            }
        }
    }










}