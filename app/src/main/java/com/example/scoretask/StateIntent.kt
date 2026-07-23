package com.example.scoretask



sealed interface StateIntent {

    data class ChangeTab(val tabIndex: Int) : StateIntent

}
