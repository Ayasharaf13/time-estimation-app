package com.example.scoretask

import com.example.scoretask.model.TaskTemplateEntity


data class TaskUiState(

    val isLoading: Boolean = false,


    val tasksList: List<TaskTemplateEntity> = emptyList(),


    val errorMessage: String? = null,

    val sessionCount :Int =0


)