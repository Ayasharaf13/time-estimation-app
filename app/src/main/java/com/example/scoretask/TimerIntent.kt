package com.example.scoretask

interface TimerIntent {


        object StartTimer : TimerIntent


        object PauseTimer : TimerIntent


        object ResetTimer : TimerIntent


}