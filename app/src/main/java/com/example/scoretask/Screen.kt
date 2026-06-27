package com.example.scoretask

sealed   class Screen (val route:String) {

     object Splash : Screen("splash_screen")
     object Onboarding : Screen("onboarding_screen")
     object MainHome : Screen("main_home_screen")
     object Task: Screen("task_screen")
     object Stats: Screen("stats_screen")


}

