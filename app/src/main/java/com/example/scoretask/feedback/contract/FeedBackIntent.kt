package com.example.scoretask.feedback.contract

sealed interface FeedBackIntent {


    data class OnContinueBtnNavigateToTimer(val sessionId: Long, val extraTimeMs: Int) :
        FeedBackIntent


    object OnContinueBtnNavigateToHome : FeedBackIntent


    object OnCancelNaveToDashboard : FeedBackIntent


}