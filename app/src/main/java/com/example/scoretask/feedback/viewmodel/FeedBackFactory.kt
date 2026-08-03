package com.example.scoretask.feedback.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.scoretask.feedback.viewmodel.FeedBackViewModel
import com.example.scoretask.repository.TaskRepository

class FeedBackFactory(
    private val repository: TaskRepository
) : ViewModelProvider.Factory {


    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FeedBackViewModel::class.java)) {
            return FeedBackViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}