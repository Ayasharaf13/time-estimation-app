package com.example.scoretask

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class StatsState(


    val totalFocusTimeMinutes:Long =0,
    val totalFocusTimeSeconds:Long = 0,
    val totalTasksComplete:Long =0,
    val totalSessionCount:Long=0,

    val selectedTab: Int = 0, // الافتراضي Week (1)

    val chartPoints: List<Number> = listOf(1, 1, 1, 1, 1, 1, 1)// emptyList() //= listOf(0)


    /* val chartPoints: Flow<List<Number>> = flow {
       emit(listOf(4, 16, 8, 10))
   }*/

)
