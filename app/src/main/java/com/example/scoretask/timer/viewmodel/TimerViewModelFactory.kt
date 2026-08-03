package com.example.scoretask.timer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.scoretask.repository.TaskRepository

class TimerViewModelFactory(
    private val repository: TaskRepository,
    private val extraTimeMs: Int,
    private val isExtraTime: Boolean,
    private val sessionId: Long
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TimerViewModel::class.java)) {
            return TimerViewModel(repository, extraTimeMs, isExtraTime, sessionId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}