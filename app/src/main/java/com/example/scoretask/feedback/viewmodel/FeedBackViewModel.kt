package com.example.scoretask.feedback.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scoretask.feedback.contract.FeedBackIntent
import com.example.scoretask.repository.TaskRepository
import com.example.scoretask.timer.contract.TimerState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FeedBackViewModel(private val repository: TaskRepository) : ViewModel() {

    private val _state = MutableStateFlow(TimerState())
    val state = _state.asStateFlow()


    private val _effect = Channel<FeedBackIntent>(Channel.Factory.BUFFERED)
    val effect = _effect.receiveAsFlow()


    fun onIntent(intent: FeedBackIntent) {
        viewModelScope.launch {
            when (intent) {
                // 🎯 1. اختيار/إلغاء اختيار الوقت الإضافي من الـ UI
                is FeedBackIntent.OnContinueBtnNavigateToTimer -> handleOnContinueToNavTime(intent)


                // 🎯 2. عند الضغط على Continue
                is FeedBackIntent.OnContinueBtnNavigateToHome -> handleOnContinueToHome()
                // handleContinue(intent.sessionId)


                // 🎯 3. عند الضغط على Cancel
                is FeedBackIntent.OnCancelNaveToDashboard -> {
                    handleOnCancelToDashboard()
                    Log.i("NAV_TEST", "OnCancelNaveToDashboard_ViewModel_1")
                }
                // handleCancel(intent.sessionId)


            }
        }
    }

    private suspend fun handleOnContinueToHome() {
        _effect.send(FeedBackIntent.OnContinueBtnNavigateToHome)
    }

    private suspend fun handleOnCancelToDashboard() {
        Log.i("NAV_TEST", "OnCancelNaveToDashboard_ViewModel")
        _effect.send(FeedBackIntent.OnCancelNaveToDashboard)
    }

    private suspend fun handleOnContinueToNavTime(intent: FeedBackIntent.OnContinueBtnNavigateToTimer) {
        // viewModelScope.launch {

        _state.update { currentState ->
            currentState.copy(
                isExtraTime = true,
                selectExtraTime = intent.extraTimeMs

            )
        }





        _effect.send(
            FeedBackIntent.OnContinueBtnNavigateToTimer(

                sessionId = intent.sessionId,
                extraTimeMs = intent.extraTimeMs


            )
        )



    }


}