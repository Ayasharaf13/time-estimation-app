package com.example.scoretask

sealed interface TimerIntent {


      //  object StartTimer : TimerIntent

        data class StartTimer(val templateId: Long,val estimateMs: Long) : TimerIntent
       // object PauseTimer : TimerIntent

        data class PauseTimer(val templateId: Long) : TimerIntent

        data class ResetTimer  (val templateId: Long) : TimerIntent

       // object ResetTimer : TimerIntent


        object EndSessionClicked : TimerIntent
        object DismissDialog : TimerIntent
        object ConfirmFinishEarly : TimerIntent
        object ConfirmGiveUp : TimerIntent

}