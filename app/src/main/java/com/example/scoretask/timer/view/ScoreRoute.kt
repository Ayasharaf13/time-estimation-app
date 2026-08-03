package com.example.scoretask.timer.view

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Black
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.scoretask.model.SessionStatus
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.example.scoretask.EditTaskTitleDialog
import com.example.scoretask.R
import com.example.scoretask.Screen
import com.example.scoretask.ScreenHeader
import com.example.scoretask.basePurple
import com.example.scoretask.taskmanagement.contract.TaskIntent
import com.example.scoretask.taskmanagement.contract.TaskUiState
import com.example.scoretask.taskmanagement.viewmodel.TaskViewModel
import com.example.scoretask.timer.contract.TimerIntent
import com.example.scoretask.timer.contract.TimerState
import com.example.scoretask.timer.viewmodel.TimerViewModel


@Composable
fun EndSessionDialog(
    onDismiss: () -> Unit,
    onConfirmFinish: () -> Unit,
    onConfirmGiveUp: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Are you done with your task?",
                fontSize = 18.sp,
                fontFamily = FontFamily(Font(R.font.sfpro_bold)),
                color = Color.White
            )
        },
        containerColor = Color(0xFF200C4E), // متناسق مع ثيم تطبيقك الداكن
        confirmButton = {

            TextButton(onClick = onConfirmFinish) {
                Text(
                    text = "🟢 Yes, I finished it!",
                    color = Color(0xFF4CAF50), // أخضر مريح
                    fontFamily = FontFamily(Font(R.font.sfpro_semibold))
                )
            }
        },
        dismissButton = {

            TextButton(onClick = onConfirmGiveUp) {
                Text(
                    text = "🔴 No, I'm giving up",
                    color = Color(0xFFFF5252), // أحمر ناعم ومريح
                    fontFamily = FontFamily(Font(R.font.sfpro_semibold))
                )
            }
        }
    )
}












@Composable
fun ScoreRoute(
    viewModel: TimerViewModel,
    sharedTaskViewModel: TaskViewModel,
    navController: NavController


) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val stateTask by sharedTaskViewModel.uiState.collectAsStateWithLifecycle()
    val stateTaskCreation by sharedTaskViewModel.state.collectAsStateWithLifecycle()

    var title = stateTaskCreation.title

    val expectedTime = state.totalTimeInMinutes // 👈 غيري الاسم حسب المتغير عندك في الـ state
    val idSession = state.idSession
    val extraTime = state.selectExtraTime
    // 2. نمرره في الـ Route بالشكل ده:

    LaunchedEffect(key1 = state.status) {
        // إذا كان التايمر وصل لصفر (أو أقل من أو يساوي صفر للأمان البرمجي)
        //  if (state.currentTime <= 0.0) {
        Log.i("statttus", state.status.toString())

        if (state.status == SessionStatus.CANCELED && state.isExtraTime) {

            Log.i("statttus_1", state.status.toString())
            navController.navigate(Screen.MainHome.route)

        } else if (state.status == SessionStatus.FINISHED && state.isExtraTime) {

            navController.navigate("${Screen.FeedbackScreenExtraTime.route}/$idSession") {
                popUpTo(Screen.MainHome.route) { inclusive = true }
            }


        } else if (state.status == SessionStatus.FINISHED || state.status == SessionStatus.CANCELED) {

            navController.navigate("${Screen.TaskCompletion.route}/$expectedTime/$idSession") {

            }

        }


    }
    ScoreTaskTimer(

        state = state,
        stateTask = stateTask,
        title = title,
        onIntent = viewModel::onIntent,
        onIntentTask = sharedTaskViewModel::onIntent,
        onNavigationClick = {
            // 👈 هذا الكود هو الذي يُنفذ عند ضغط السهم
            navController.navigate(Screen.MainHome.route) {
                popUpTo(Screen.MainHome.route) { inclusive = true }
            }
        }


    )


}







@SuppressLint("NonObservableLocale")
@Composable
fun ScoreTaskTimer(
    state: TimerState,
    stateTask: TaskUiState,
    title: String,
    onIntent: (TimerIntent) -> Unit,
    onIntentTask: (TaskIntent) -> Unit,
    onNavigationClick: () -> Unit


) {


    // 🎨 1. ألوان متغيرة بناءً على هل نحن في وقت إضافي (Extra Time) ولا عادي
    val extraTimeColor = Color(0xFFFFB300) // لون ذهبي/برتقالي للـ Extra Time
    val normalArcColor = Color(0xFF200C4E)
    val currentArcColor = if (state.isExtraTime) extraTimeColor else normalArcColor

    // تغيير ألوان الخلفية في حالة الـ Extra Time
    val color1 = if (state.isExtraTime) Color(0xFFFF8F00) else Color(0xFF9373D6)
    val color2 = if (state.isExtraTime) Color(0xFFFF3D00).copy(alpha = 0.5f) else Color(0xFF6943AC)

    Box(modifier = Modifier
        .fillMaxSize()
        .background(basePurple)) {}


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(basePurple)
            .drawWithCache {
                val designWidth = 720f
                val designHeight = 1600f

                val glowWidth = size.width * (938f / designWidth)
                val glowHeight = size.height * (1078f / designHeight)

                val offsetX = size.width * (177f / designWidth)
                val offsetY = size.height * (509f / designHeight)

                onDrawBehind {
                    drawOval(
                        brush = Brush.radialGradient(
                            0.0f to color1,
                            0.38f to color2,
                            1.0f to Color.Transparent,
                            center = Offset(
                                x = offsetX + (glowWidth * 0.6186f),
                                y = offsetY + (glowHeight * 0.3892f)
                            ),
                            radius = glowWidth * 0.5772f
                        ),
                        topLeft = Offset(offsetX, offsetY),
                        size = Size(glowWidth, glowHeight),
                        blendMode = BlendMode.Overlay
                    )
                }
            }
            .blur(
                radius = 81.dp,
                edgeTreatment = BlurredEdgeTreatment.Unbounded
            )
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(50.dp))

        ScreenHeader(
            title,
            showEditButton = true,
            onEditClick = onIntentTask,
            onNavigationClick = onNavigationClick
        )

        Spacer(modifier = Modifier.height(70.dp))

        // 🏷️ 2. إضافة Badge يظهر فقط في حالة الـ Extra Time فوق الدائرة
        if (state.isExtraTime) {
            Surface(
                color = extraTimeColor.copy(alpha = 0.2f),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, extraTimeColor),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text(
                    text = "⚡ EXTRA TIME MODE",
                    color = extraTimeColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        } else {
            Spacer(modifier = Modifier.height(30.dp))
        }

        // progress من 1.0 (بداية) إلى 0.0 (نهاية)
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(146.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val strokeWidth = 8.dp.toPx()

                // 1. الدائرة الخلفية
                drawCircle(
                    color = if (state.isExtraTime) extraTimeColor.copy(alpha = 0.15f) else Color(
                        0xFF7F69B3
                    ).copy(alpha = 0.5f),
                    style = Stroke(width = strokeWidth)
                )

                // 2. القوس التفاعلي (يتغير لونه للذهبي في الـ Extra Time)
                drawArc(
                    color = currentArcColor,
                    startAngle = -90f,
                    sweepAngle = state.value * 360f,
                    useCenter = false,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            }

            Text(
                text = state.formattedTime,
                style = TextStyle(
                    fontWeight = Black,
                    lineHeight = 11.sp,
                    letterSpacing = 1.sp,
                    fontFamily = FontFamily(Font(R.font.sfpro_bold)),
                ),
                color = Color.White,
                fontSize = 32.sp
            )
        }

        // 💬 3. رسالة تحفيزية تغير النص حسب المرحلة
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = if (state.isExtraTime) "Bonus Sprint: Almost done! 🚀" else "Stay Focused",
            color = if (state.isExtraTime) extraTimeColor else Color.White.copy(alpha = 0.7f),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(50.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(
                onClick = {
                    if (state.isRunning) {
                        onIntent(TimerIntent.PauseTimer(state.idTask))
                    } else {
                        if (state.isExtraTime) {
                            //idsession
                            onIntent(
                                TimerIntent.StartTimer(
                                    state.idTask,
                                    state.extraTimeFromMunToMill
                                )
                            )

                        } else {
                            onIntent(TimerIntent.StartTimer(state.idTask, state.totalTime))
                        }
                    }
                },

                modifier = Modifier.size(41.dp)
            ) {
                Icon(
                    painter = painterResource(
                        id = if (state.isRunning) R.drawable.pause_icon else R.drawable.icon_play
                    ),
                    contentDescription = "Start Timer",
                    tint = Color.Unspecified,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.width(80.dp))

            IconButton(
                onClick = {
                    onIntent(TimerIntent.ResetTimer(state.idTask))
                },
                modifier = Modifier.size(41.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.retry_icon),
                    contentDescription = "Reset Timer",
                    tint = Color.Unspecified,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Spacer(Modifier.height(70.dp))

        OutlinedButton(
            onClick = {
                onIntent(TimerIntent.EndSessionClicked)
            },
            border = BorderStroke(1.dp, Color(0xFFFF2521).copy(alpha = 0.7f)),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFF2521))
        ) {
            Text(text = "End Session", color = Color.White)
        }

        if (state.showEndSessionDialog) {
            EndSessionDialog(
                onDismiss = { onIntent(TimerIntent.DismissDialog) },
                onConfirmFinish = { onIntent(TimerIntent.ConfirmFinishEarly) },
                onConfirmGiveUp = { onIntent(TimerIntent.ConfirmGiveUp) }
            )
        }


        if (stateTask.showEditTitleDialog) {
            Log.i("idTskk", state.idTask.toString())
            EditTaskTitleDialog(
                onDismiss = { onIntentTask(TaskIntent.Cancel) },
                onConfirm = { newTitle ->
                    // 👈 2. استلام الـ newTitle القادم من الـ Dialog وتمريره مع الـ idTask
                    onIntentTask(TaskIntent.Confirm(state.idTask, newTitle))
                }

            )
        }


    }
}




