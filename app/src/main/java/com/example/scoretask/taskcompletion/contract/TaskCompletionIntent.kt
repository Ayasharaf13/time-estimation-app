package com.example.scoretask.taskcompletion.contract

import com.example.scoretask.TaskResultStatus

sealed interface TaskCompletionIntent {

    data class AddExtraTime(val sessionId: Long, val extraTime: Long) : TaskCompletionIntent


    data class ConfirmExtraTime(
        val sessionId: Long,
        val selectedStatus: TaskResultStatus?,
        val extraTimeMinutes: Int
    ) : TaskCompletionIntent


}