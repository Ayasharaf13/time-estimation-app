package com.example.scoretask.feedback.view

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.scoretask.EditTaskTitleDialog
import com.example.scoretask.GlowBackground
import com.example.scoretask.R
import com.example.scoretask.Screen
import com.example.scoretask.ScreenHeader
import com.example.scoretask.TimeoutBadge
import com.example.scoretask.ZeroTimerText
import com.example.scoretask.basePurple
import com.example.scoretask.color1
import com.example.scoretask.color2
import com.example.scoretask.feedback.contract.FeedBackIntent
import com.example.scoretask.feedback.viewmodel.FeedBackViewModel
import com.example.scoretask.taskmanagement.contract.TaskIntent
import com.example.scoretask.taskmanagement.contract.TaskUiState
import com.example.scoretask.taskmanagement.viewmodel.TaskViewModel
import com.example.scoretask.timer.contract.TimerState


@Composable
fun FeedbackScreen(
    titleFeedback: String,
    titleTaskHeader: String,
    stateTask: TaskUiState,
    sessionId: Long,
    extraTime: Int,
    @DrawableRes imageRes: Int = R.drawable.img_finish,
    onIntent: (FeedBackIntent) -> Unit,
    onIntentTask: (TaskIntent) -> Unit,
    state: TimerState,
    onNavigationClick: () -> Unit


) {

    Log.i("extraTime", extraTime.toString())

    GlowBackground(
        baseColor = basePurple,
        glowColor1 = color1,
        glowColor2 = color2
    ) {

    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    )

    {

        Spacer(Modifier.height(50.dp))
        ScreenHeader(
            titleTaskHeader,
            showEditButton = true,
            onEditClick = onIntentTask,
            onNavigationClick = onNavigationClick
        )

        Spacer(Modifier.height(50.dp))

        TimeoutBadge()
        ZeroTimerText()


        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            modifier = Modifier
                // .align(Alignment.Center) // أو تحديد مكانها بالـ Offset
                .size(300.dp), // مثال للحجم
            contentScale = ContentScale.Fit
        )



        Text(
            text = titleFeedback,

            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.archivoblack_regular)), // تأكدي من إضافة ملف الخط في res/font
                fontWeight = FontWeight.Normal, // Archivo Black عادة ما يأتي بوزن 400 افتراضياً كخط عريض
                fontSize = 18.sp,
                color = Color.White,
                lineHeight = 16.sp,
                letterSpacing = 1.sp,
                textAlign = TextAlign.Center,
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false // يساعد في تحقيق leading-trim: NONE بشكل أدق
                )
            )
        )
        val continueButtonTextStyle = TextStyle(
            fontFamily = FontFamily(Font(R.font.inter_medium)), // تأكدي من وجود ملف الخط في res/font
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            lineHeight = 20.sp,
            color = Color.White,

            textAlign = TextAlign.Center,
            platformStyle = PlatformTextStyle(
                includeFontPadding = false // ضروري جداً لتحقيق leading-trim: NONE
            )
        )

        Spacer(Modifier.height(30.dp))

        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 50.dp),
            horizontalArrangement = Arrangement.spacedBy(35.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = { /* Action */

                    if (extraTime > 0) {
                        onIntent(
                            FeedBackIntent.OnContinueBtnNavigateToTimer(
                                sessionId = sessionId,
                                extraTimeMs = extraTime
                            )
                        )

                    } else {
                        onIntent(FeedBackIntent.OnContinueBtnNavigateToHome)
                    }

                },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color(0xFFFF2521)


                ),
                modifier = Modifier
                    .minimumInteractiveComponentSize()
                    // 2. تطبيق العرض المرن (Responsive Width)
                    .widthIn(min = 30.dp, max = 40.dp)//70,80
                    .weight(1f)
                    // .widthIn(min = 70.dp)

                    .height(35.dp),

                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.5f)),
                contentPadding = PaddingValues(horizontal = 2.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Continue",
                    style = continueButtonTextStyle,
                    color = Color.White
                )
            }

            OutlinedButton(
                onClick = { /* Action */

                    onIntent(FeedBackIntent.OnCancelNaveToDashboard)
                    Log.i("NAV_TEST", "OnCancelNaveToDashboardButton")

                },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White

                ),
                modifier = Modifier
                    .minimumInteractiveComponentSize()
                    .weight(1f)
                    // 2. تطبيق العرض المرن (Responsive Width)
                    .widthIn(min = 30.dp)//70
                    .height(35.dp),

                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.5f)),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Cancel",

                    style = continueButtonTextStyle,
                    color = Color.Black
                )
            }


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
    }//end coum


}
















@Composable

fun FeedbackScreenRoute(
    title: String,
    imageRes: Int,
    extraTimeMinutes: Int,
    sessionId: Long,
    viewModel: FeedBackViewModel = viewModel(),
    navController: NavController,
    taskViewModel: TaskViewModel


) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val taskState by taskViewModel.uiState.collectAsStateWithLifecycle()
    val taskCreationState by taskViewModel.state.collectAsStateWithLifecycle()





    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->

            when (effect) {
                is FeedBackIntent.OnContinueBtnNavigateToTimer -> {

                    val route = "${Screen.TimerTask.route}" +
                            "?sessionId=${effect.sessionId}" +
                            "&extraTimeMs=${effect.extraTimeMs}" +
                            "&isExtraTime=true"


                    navController.navigate(route) {
                        popUpTo(Screen.MainHome.route) { inclusive = false }
                    }
                }

                is FeedBackIntent.OnContinueBtnNavigateToHome -> {
                    // 🎯 التنقل للرئيسية
                    navController.navigate(Screen.MainHome.route) {

                    }
                }

                is FeedBackIntent.OnCancelNaveToDashboard -> {

                    navController.navigate(Screen.MainHome.route)

                }

                else -> {
                    navController.navigate(Screen.MainHome.route)
                }
            }
        }
    }

    FeedbackScreen(
        titleFeedback = title,
        taskCreationState.title,
        stateTask = taskState,
        sessionId,
        extraTimeMinutes,
        imageRes,
        onIntent = viewModel::onIntent,
        onIntentTask = taskViewModel::onIntent,
        state = state,
        onNavigationClick = {
            // 👈 هذا الكود هو الذي يُنفذ عند ضغط السهم
            navController.navigate(Screen.MainHome.route) {
                popUpTo(Screen.MainHome.route) { inclusive = true }
            }
        })

}