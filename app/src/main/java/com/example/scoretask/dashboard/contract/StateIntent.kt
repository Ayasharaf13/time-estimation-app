package com.example.scoretask.dashboard.contract

sealed interface StateIntent {

    data class ChangeTab(val tabIndex: Int) : StateIntent

}