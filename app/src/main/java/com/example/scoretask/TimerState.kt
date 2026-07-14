package com.example.scoretask

import com.example.scoretask.model.SessionStatus



//data class TimerState(

 //   val currentTime: Long = 60000L,

   // val isTimerRunning: Boolean = false,
   // val progress: Float = 1.0f




    data class TimerState(
    val currentTime: Long = 0L,
    val totalTime: Long = 0L,
    val status: SessionStatus = SessionStatus.IDLE,
     val value : Float = 0.0f
) {
    // 💡 يُحسب تلقائياً بمجرد قراءة الـ State دون الحاجة لتحديثه يدوياً في الـ ViewModel
    val progress: Float
        get() = if (totalTime > 0) value * 360f else 0f//currentTime.toFloat() / totalTime.toFloat() else 0f

        val isRunning: Boolean get() = status ==  SessionStatus.RUNNING
    // 💡 النص جاهز تماماً للـ Text Composable بدون أي منطق رياضي بالشاشة
    val formattedTime: String
        get() {
            val totalSeconds = currentTime / 1000L
            val minutes = totalSeconds / 60
            val seconds = totalSeconds % 60
            return String.format("%02d:%02d", minutes, seconds)
        }

        val totalTimeInMinutes: Int
            get() = (totalTime / 1000L / 60L).toInt()
}





