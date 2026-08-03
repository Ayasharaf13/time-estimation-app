package com.example.scoretask.taskcompletion.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.scoretask.repository.TaskRepository

class TaskCompletionFactory(
    private val repository: TaskRepository,

    ) : ViewModelProvider.Factory {


    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskCompletionViewModel::class.java)) {
            return TaskCompletionViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}