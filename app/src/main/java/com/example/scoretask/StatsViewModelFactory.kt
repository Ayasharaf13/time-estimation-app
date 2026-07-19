package com.example.scoretask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider





class StatsViewModelFactory(
    private val repository: TaskRepository
) : ViewModelProvider.Factory {


    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StatsViewModel::class.java)) {
            return StatsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
