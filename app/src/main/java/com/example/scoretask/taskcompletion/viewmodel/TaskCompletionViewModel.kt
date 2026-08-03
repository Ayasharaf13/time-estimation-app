package com.example.scoretask.taskcompletion.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scoretask.taskcompletion.contract.TaskCompletionState
import com.example.scoretask.repository.TaskRepository
import com.example.scoretask.taskcompletion.contract.TaskCompletionIntent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class TaskCompletionViewModel(private val repository: TaskRepository) : ViewModel() {

    private val _state = MutableStateFlow(TaskCompletionState())
    val state = _state.asStateFlow()


    private val _effect = Channel<TaskCompletionIntent>(Channel.Factory.BUFFERED)
    val effect = _effect.receiveAsFlow()


    fun onIntent(intent: TaskCompletionIntent) {

        viewModelScope.launch() {
            //_state.collect {intent->
            when (intent) {
                is TaskCompletionIntent.AddExtraTime -> addExtensionToSession(
                    intent.sessionId,
                    intent.extraTime
                )

                is TaskCompletionIntent.ConfirmExtraTime -> handleConfirmExtraTime(intent)

                else -> {}

            }
        }
    }

    private fun handleConfirmExtraTime(intent: TaskCompletionIntent.ConfirmExtraTime) {
        viewModelScope.launch {

            // 2. إطلاق أثر التنقل (Side Effect)

            // _effect.trySend(TaskCompletionEffect.NavigateToFeedback)

            _effect.trySend(
                TaskCompletionIntent.ConfirmExtraTime(
                    sessionId = intent.sessionId,
                    selectedStatus = intent.selectedStatus,
                    extraTimeMinutes = intent.extraTimeMinutes

                )
            )

        }

    }


    private suspend fun addExtensionToSession(idSession: Long, extraTime: Long) {


        repository.addExtensionToSession(
            idSession,
            extraTime
        )




    }


}