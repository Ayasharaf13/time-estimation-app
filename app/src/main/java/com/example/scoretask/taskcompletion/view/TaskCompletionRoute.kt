package com.example.scoretask.taskcompletion.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.scoretask.BgScreen
import com.example.scoretask.R
import com.example.scoretask.Screen
import com.example.scoretask.ScreenHeader
import com.example.scoretask.taskcompletion.contract.TaskCompletionIntent
import com.example.scoretask.taskcompletion.contract.TaskCompletionState
import com.example.scoretask.taskcompletion.viewmodel.TaskCompletionViewModel
import com.example.scoretask.TaskResultStatus
import com.example.scoretask.TimeoutBadge
import com.example.scoretask.ZeroTimerText
import com.example.scoretask.taskmanagement.viewmodel.TaskViewModel


@Composable

fun RowCard(
    title: String,
    iconResId: Int,
    isSelected: Boolean,
    showArrow: Boolean,// يحدد برمجياً هل هذا الصف هو المختار حالياً أم لا
    onClick: () -> Unit       // الأكشن الذي ينطلق عند الضغط على الصف بالكامل
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() } // الكارد بالكامل أصبح قابل للنقر لتسهيل تجربة الاستخدام
            .background(
                // إذا تم اختياره، نعطيه خلفية بيضاء شفافة بنسبة 15% كمؤشر بصري، وإلا يظل شفافاً
                color = if (isSelected) Color.White.copy(alpha = 0.15f) else Color.Transparent,
                shape = RoundedCornerShape(9.dp)
            )
            .padding(vertical = 6.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // 1. الأيقونة الخاصة بالحالة (Finish, Not Finish...)
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = null,
            tint = Color.Unspecified, // للحفاظ على ألوان الأيقونة الأصلية كما هي
            modifier = Modifier
                .width(61.dp)
                .height(41.dp)
                .padding(start = 8.dp)

        )

        Spacer(modifier = Modifier.width(8.dp))

        // 2. نص الحالة
        Text(
            text = title,
            color = Color.White,
            fontSize = 14.sp,
            lineHeight = 11.sp,
            modifier = Modifier.weight(1f),
            fontFamily = FontFamily(Font(R.font.sfpro_regular)),
            fontWeight = FontWeight.Bold,
        )

        // 3. بديل الـ Checkbox: أيقونة "صح" تظهر فقط إذا كان هذا الصف هو المختار حالياً
        if (isSelected) {

            Icon(
                painter = painterResource(id = R.drawable.bottom_icon), // يمكنك استخدام أيقونة سهم أو صح متاحة لديكِ
                contentDescription = "Selected Indicator",
                tint = Color.White,
                modifier = Modifier
                    .padding(end = 12.dp)
                    .size(16.dp)
                    .graphicsLayer(alpha = if (showArrow) 1f else 0f)
            )
        } else {
            // مساحة فارغة بديلة للحفاظ على محاذاة العناصر إذا لم يكن مختاراً
            Spacer(modifier = Modifier
                .padding(end = 12.dp)
                .size(16.dp))
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskCompletion(
    expectTime: Int, sessionId: Long, state: TaskCompletionState,
    onIntent: (TaskCompletionIntent) -> Unit, title: String, onNavigationClick: () -> Unit
) {
    // 2️⃣ الـ States الناقصة للتحكم في الاختيارات والـ Bottom Sheet
    var selectedStatus by remember { mutableStateOf<TaskResultStatus?>(null) }
    var selectedExtraTime by remember { mutableStateOf<Int?>(null) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedPsychologyReason by remember { mutableStateOf<String?>(null) }
    val sheetState = rememberModalBottomSheetState()
    val extraTimeMs = (selectedExtraTime ?: 0) * 60 * 1000L
    BgScreen() // الخلفية الخاصة بكِ

    // تم تصليح الـ Column وفتح القوس المظبوط {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(50.dp))

        // الـ Header المخصص بتاعكِ
        ScreenHeader(title, showEditButton = false, onNavigationClick = onNavigationClick)

        Spacer(Modifier.height(50.dp))

        TimeoutBadge()
        ZeroTimerText()



        Spacer(Modifier.height(60.dp))

        // سؤال النص السفلي والـ Arrow icon
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Are you finish your Task ?",
                modifier = Modifier.padding(start = 50.dp),
                color = Color.White,
                fontSize = 12.sp,
                letterSpacing = 1.sp,
                lineHeight = 11.sp,
                fontFamily = FontFamily(Font(R.font.sfpro_regular)),
                style = TextStyle(
                    lineHeightStyle = LineHeightStyle(
                        alignment = LineHeightStyle.Alignment.Center,
                        trim = LineHeightStyle.Trim.Both
                    ),
                    fontWeight = FontWeight.Bold,
                    platformStyle = PlatformTextStyle(includeFontPadding = false)
                )
            )

            IconButton(
                onClick = { /* العودة للخلف */ },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.bottom_icon),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .width(11.dp)
                        .height(20.dp)
                )
            }
        }

        Spacer(Modifier.height(10.dp))

        // إعدادات الجراديانت الخاص بالكارد بتاعكِ
        val startColor = Color(0xFF6347A4).copy(alpha = 0.2f)
        val endColor = Color(0xFF000000).copy(alpha = 0.2f)
        val gradientBrush = Brush.linearGradient(
            colors = listOf(startColor, endColor),
            start = Offset(0f, 0f),
            end = Offset(Float.POSITIVE_INFINITY, 0f)
        )

        // كارد الخيارات الرئيسي
        Card(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .wrapContentHeight()
                .background(brush = gradientBrush, shape = RoundedCornerShape(9.dp))
                .padding(bottom = 24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
        ) {
            Column(
                modifier = Modifier.padding(top = 5.dp, start = 4.dp, end = 4.dp, bottom = 4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 3️⃣ ربط الكروت ديناميكياً بالـ الـ الـ Single-Selection والـ Bottom Sheet
                TaskResultStatus.values().forEach { status ->
                    val iconRes = when (status) {
                        TaskResultStatus.FINISH -> R.drawable.img_finish
                        TaskResultStatus.FINISH_EXTRA -> R.drawable.img_finish_need_extra_time
                        TaskResultStatus.NOT_FINISH -> R.drawable.img_not_finish
                        TaskResultStatus.NOT_FINISH_EXTRA -> R.drawable.img_lazy
                    }

                    RowCard(
                        title = status.title,
                        iconResId = iconRes,
                        isSelected = selectedStatus == status,
                        showArrow = (status != TaskResultStatus.FINISH),
                        onClick = {
                            if (status != TaskResultStatus.FINISH) {
                                showBottomSheet = true
                            } else {
                                showBottomSheet = false // حماية عشان الستارة متفتحش لو كانت مفتوحة
                            }
                            selectedStatus = status
                            selectedExtraTime = null // ريست للوقت القديم لو اختار حالة تانية
                            selectedPsychologyReason = null
                            // showBottomSheet = true  // افتح الستارة فوراً!

                            if (status == TaskResultStatus.FINISH) {
                                showBottomSheet = false
                                // 🟢 1. التأكيد الفوري والمباشر فقط عند اختيار FINISH
                                onIntent(
                                    TaskCompletionIntent.ConfirmExtraTime(
                                        sessionId = sessionId,
                                        selectedStatus = TaskResultStatus.FINISH,
                                        extraTimeMinutes = selectedExtraTime ?: 0
                                    )
                                )
                            } else {
                                showBottomSheet = true
                            }
                        }
                    )
                }
            }
        }
    }

    // 4️⃣ الـ Modal Bottom Sheet المفقود (الستارة اللي بتظهر من تحت)
    // if (showBottomSheet && selectedStatus != null) {
    if (showBottomSheet && selectedStatus != null && selectedStatus != TaskResultStatus.FINISH) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            containerColor = Color(0xFF1C1B2B), // لون داكن متناسق مع تطبيقكِ
            dragHandle = { BottomSheetDefaults.DragHandle(color = Color.White.copy(alpha = 0.4f)) }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // عرض السؤال المناسب للحالة المختارة
                Text(
                    text = selectedStatus?.getQuestion() ?: "",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 24.dp)
                )


                if (selectedStatus != TaskResultStatus.FINISH) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        selectedStatus!!.getPsychologyOptions().forEach { reason ->
                            val isReasonSelected = selectedPsychologyReason == reason

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        color = if (isReasonSelected) Color(0xFF8C5BFF).copy(alpha = 0.2f) else Color.White.copy(
                                            alpha = 0.05f
                                        ),
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .border(
                                        width = 1.dp,
                                        color = if (isReasonSelected) Color(0xFF8C5BFF) else Color.Transparent,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .clickable { selectedPsychologyReason = reason }
                                    .padding(horizontal = 16.dp, vertical = 12.dp)
                            ) {
                                Text(
                                    text = reason,
                                    color = if (isReasonSelected) Color.White else Color.White.copy(
                                        alpha = 0.7f
                                    ),
                                    fontSize = 13.sp,
                                    fontWeight = if (isReasonSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }
                // عرض دوائر اختيار الدقائق
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(bottom = 32.dp)
                ) {
                    selectedStatus!!.getDurationOptions(expectTime).forEach { minutes ->
                        val isTimeSelected = selectedExtraTime == minutes


                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .shadow(if (isTimeSelected) 4.dp else 0.dp, CircleShape)
                                .background(
                                    color = if (isTimeSelected) Color(0xFF8C5BFF) else Color.White.copy(
                                        alpha = 0.1f
                                    ),
                                    shape = CircleShape
                                )
                                .clickable { selectedExtraTime = minutes },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (minutes == 0) "No Time" else "+${minutes}m",
                                // text = "+$minutes\nm",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                val isConfirmEnabled = if (selectedStatus == TaskResultStatus.FINISH_EXTRA) {
                    selectedExtraTime != null

                } else if (selectedStatus == TaskResultStatus.NOT_FINISH) {
                    selectedPsychologyReason != null
                } else {
                    selectedExtraTime != null && selectedPsychologyReason != null
                }



                Text(
                    text = "Note: You can extend this session only once.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.6f)
                )
                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = {
                        showBottomSheet = false

                        onIntent(TaskCompletionIntent.AddExtraTime(sessionId, extraTimeMs))
                        Log.i("idSesss", sessionId.toString())//previous36idses

                        onIntent(
                            TaskCompletionIntent.ConfirmExtraTime(
                                sessionId = sessionId,
                                selectedStatus = selectedStatus,
                                extraTimeMinutes = selectedExtraTime ?: 0
                            )
                        )


                    },
                    // enabled = selectedExtraTime != null, // لا ينقر إلا بعد تحديد الوقت
                    enabled = isConfirmEnabled,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text("Confirm Selection", fontWeight = FontWeight.Bold)
                }

            }
        }
    }
}
@Composable
fun TaskCompletionRoute(
    expectTime: Int,
    sessionId: Long,
    viewModel: TaskCompletionViewModel = viewModel(),
    navController: NavController,
    viewModelTask: TaskViewModel,

    ) {


    val stateTaskCompletion by viewModel.state.collectAsStateWithLifecycle()

    val stateTask by viewModelTask.state.collectAsStateWithLifecycle()



    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->

            when (effect) {
                is TaskCompletionIntent.ConfirmExtraTime -> {

                    // الانتقال لشاشة الـ Feedback مع إزالة الشاشة الحالية من الـ BackStack لو حابة
                    navController.navigate("${Screen.FeedBackScreen.route}/$expectTime/$sessionId/${effect.selectedStatus}/${effect.extraTimeMinutes}") {
                        // خيار احترافي: مسح شاشة إكمال المهمة عشان المستخدم لما يدوس Back ميرجعش ليها
                        popUpTo(Screen.MainHome.route) { inclusive = true }
                    }
                }

                else -> {}
            }
        }
    }



    TaskCompletion(
        expectTime, sessionId, stateTaskCompletion, onIntent = viewModel::onIntent, stateTask.title,
        onNavigationClick = {
            // 👈 هذا الكود هو الذي يُنفذ عند ضغط السهم
            navController.navigate(Screen.MainHome.route) {
                popUpTo(Screen.MainHome.route) { inclusive = true }
            }
        })


}