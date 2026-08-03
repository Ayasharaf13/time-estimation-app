package com.example.scoretask.taskcompletion.contract

import com.example.scoretask.TaskResultStatus

sealed interface TaskCompletionEffect {


    data class NavigateToFeedback(
        val sessionId: Long,
        val status: TaskResultStatus?,
        val extraTimeMinutes: Int
    ) : TaskCompletionEffect
}