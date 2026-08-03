package com.example.scoretask.taskcompletion.contract

data class TaskCompletionState(

    val addExtensionTime: Int = 0,
    val sessionId: Long = 0L,
    val extraTime: Long = 0L,
    val psychologyReason: String = ""
)