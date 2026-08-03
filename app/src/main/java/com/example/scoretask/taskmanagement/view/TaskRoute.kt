package com.example.scoretask.taskmanagement.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.scoretask.BgScreen
import com.example.scoretask.R
import com.example.scoretask.Screen
import com.example.scoretask.ScreenHeader
import com.example.scoretask.taskmanagement.contract.TaskIntent
import com.example.scoretask.taskmanagement.viewmodel.TaskOverviewViewModel
import com.example.scoretask.taskmanagement.contract.TaskUiState
import com.example.scoretask.taskmanagement.viewmodel.TaskViewModel
import com.example.scoretask.dashboard.view.TimeMuscleProgressIndicator
import com.example.scoretask.model.totalMinutes



@Composable
fun TodayOverviewCard(state: TaskUiState) {

    Column(Modifier.padding(start = 14.dp, end = 14.dp)) {
        Text(
            text = "Today’s Overview",
            Modifier.padding(start = 22.dp, top = 11.dp),
            color = Color.White,


            style = TextStyle(

                fontFamily = FontFamily(Font(R.font.sfpro_semibold)), // استبدليها باسم ملف الخط لديكِ
                fontWeight = FontWeight(590), // أو FontWeight.SemiBold
                fontSize = 16.sp,
                lineHeight = 11.sp, // ملاحظة هامة بالأسفل حول هذه القيمة
                letterSpacing = 0.sp,


                )
        )


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp),
            //  .padding(vertical = 30.dp, horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp) // Gap بين الأيقونة والنص
        ) {

            IconButton(
                onClick = { /* العودة للخلف */ },
                modifier = Modifier.size(48.dp) // مساحة النقر المثالية
            ) {

                // الأيقونة الدائرية
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    // 1. الأيقونة الخلفية (الدائرة)
                    Icon(
                        painter = painterResource(id = R.drawable.crcular_icon_container),
                        contentDescription = null,
                        tint = Color(0xFF7446DE),
                        //tint = Color.White.copy(alpha = 0.2f), // شفافية الدائرة كما في التصميم
                        modifier = Modifier.size(30.dp) // حجم الدائرة الخارجية
                    )

                    // 2. الأيقونة الأمامية (السهم أو الـ Check)
                    Icon(
                        painter = painterResource(id = R.drawable.icon_inside_circle_left), // أيقونة السهم مثلاً
                        contentDescription = null,
                        tint = Color(0xFF7446DE),
                        modifier = Modifier.size(10.dp) // حجم الأيقونة الداخلية (أصغر من الدائرة)
                    )
                }
            }

            Column {
                Text(
                    text = state.sessionCount.toString(),
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.sfpro_semibold)),
                    color = Color.White
                )
                Text(
                    text = "work session done",
                    fontSize = 8.sp,
                    fontFamily = FontFamily(Font(R.font.sfpro_regular)),
                    color = Color.White.copy(alpha = 0.6f)
                )
            }
        }//endrow


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp) // Gap بين الأيقونة والنص
        ) {

            IconButton(
                onClick = { /* العودة للخلف */ },
                modifier = Modifier.size(48.dp) // مساحة النقر المثالية
            ) {

                // الأيقونة الدائرية
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    // 1. الأيقونة الخلفية (الدائرة)
                    Icon(
                        painter = painterResource(id = R.drawable.crcular_icon_container),
                        contentDescription = null,
                        tint = Color(0xFF7446DE),
                        //tint = Color.White.copy(alpha = 0.2f), // شفافية الدائرة كما في التصميم
                        modifier = Modifier.size(30.dp) // حجم الدائرة الخارجية
                    )

                    // 2. الأيقونة الأمامية (السهم أو الـ Check)
                    Icon(
                        painter = painterResource(id = R.drawable.icon_inside_right), // أيقونة السهم مثلاً
                        contentDescription = null,
                        tint = Color(0xFF7446DE),
                        modifier = Modifier.size(10.dp) // حجم الأيقونة الداخلية (أصغر من الدائرة)
                    )
                }
            }


            Column {
                Text(
                    text = "${state.totalMinutesToday}m ${state.totalSecondsToday}s",//"2h 84m",
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.sfpro_semibold)),
                    color = Color.White
                )
                Text(
                    text = "Foucs Time",
                    fontSize = 8.sp,
                    fontFamily = FontFamily(Font(R.font.sfpro_regular)),
                    color = Color.White.copy(alpha = 0.6f)
                )
            }

        }//endrow

    }
}



@Composable
fun TaskRow(titleTask: String = "", displayTime: String = "") {


    Text(

        text = titleTask,
        color = Color.White,


        style = TextStyle(

            fontFamily = FontFamily(Font(R.font.sfpro_semibold)), // استبدليها باسم ملف الخط لديكِ
            fontWeight = FontWeight(590), // أو FontWeight.SemiBold
            fontSize = 14.sp,
            lineHeight = 11.sp, // ملاحظة هامة بالأسفل حول هذه القيمة
            letterSpacing = 0.sp,

            )
    )


    Column(verticalArrangement = Arrangement.spacedBy(6.dp))//Modifier.fillMaxHeight(),

    {
        Text(
            text = displayTime,
            color = Color(0xFFA9A7A7),


            style = TextStyle(

                fontFamily = FontFamily(Font(R.font.sfpro_medium)), // استبدليها باسم ملف الخط لديكِ
                fontWeight = FontWeight(590), // أو FontWeight.SemiBold
                fontSize = 12.sp,
                lineHeight = 11.sp, // ملاحظة هامة بالأسفل حول هذه القيمة
                letterSpacing = 0.sp,


                )
        )

        Box(

        ) {
            // 1. الأيقونة الخلفية (الدائرة)
            Icon(
                painter = painterResource(id = R.drawable.retry_icon),
                contentDescription = null,
                tint = Color.White,
                //tint = Color.White.copy(alpha = 0.2f), // شفافية الدائرة كما في التصميم
                modifier = Modifier.size(31.dp) // حجم الدائرة الخارجية
            )

        }

    }

}


@Composable
fun DailyOverviewScreen(
    state: TaskUiState,
    stateOverView: TaskUiState,
    onIntentTask: (TaskIntent) -> Unit,
    onNavigationClick: () -> Unit
) {


    BgScreen()
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    )

    {

        Spacer(Modifier.height(50.dp))

        ScreenHeader("My Productivity", R.drawable.back_arrow, onNavigationClick)

        Spacer(Modifier.height(50.dp))


        ///////////
        val backgroundGradient = Brush.linearGradient(
            0.0f to Color(0xFF6347A4).copy(alpha = 0.2f), // بداية التدرج
            0.80f to Color(0xFF000000).copy(alpha = 0.2f), // نهاية التدرج
            start = Offset(0f, 0f),
            end = Offset(Float.POSITIVE_INFINITY, 0f) // لضمان اتجاه 90 درجة (أفقي)
        )

        Box(
            modifier = Modifier
                // 2. جعل العرض متجاوباً (يأخذ 90% من عرض الشاشة) بدلاً من width: 578
                .fillMaxWidth(0.90f)

                // 3. الارتفاع المرن (بين حد أدنى وأقصى) بدلاً من height: 332
                .heightIn(min = 200.dp, max = 350.dp)
                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.1f), // إطار خفيف جداً لإعطاء تأثير الزجاج
                    shape = RoundedCornerShape(8.dp)
                )
                // 4. تطبيق الخلفية والزوايا
                .clip(RoundedCornerShape(17.dp)) // border-radius: 17px
                .background(backgroundGradient),

            ) {

            TodayOverviewCard(stateOverView)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp, top = 46.dp), // أو fillMaxSize حسب حاجتك
                horizontalArrangement = Arrangement.End // لتوسيط العناصر أفقياً
            ) //) {
            {
                TimeMuscleProgressIndicator(stateOverView)

            }

        }

        Spacer(Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp),
            horizontalArrangement = Arrangement.SpaceBetween, // دفع العناصر للأطراف
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "TimeLine",
                color = Color.White,


                style = TextStyle(

                    fontFamily = FontFamily(Font(R.font.sfpro_semibold)), // استبدليها باسم ملف الخط لديكِ
                    fontWeight = FontWeight(590), // أو FontWeight.SemiBold
                    fontSize = 16.sp,
                    lineHeight = 11.sp, // ملاحظة هامة بالأسفل حول هذه القيمة
                    letterSpacing = 0.sp,


                    )
            )


            Text(
                text = "Today",
                color = Color(0xFFA9A7A7),


                style = TextStyle(

                    fontFamily = FontFamily(Font(R.font.sfpro_medium)), // استبدليها باسم ملف الخط لديكِ
                    fontWeight = FontWeight(590), // أو FontWeight.SemiBold
                    fontSize = 16.sp,
                    lineHeight = 11.sp, // ملاحظة هامة بالأسفل حول هذه القيمة
                    letterSpacing = 0.sp,

                    )
            )
        }

        Spacer(Modifier.height(20.dp))
        //card

        val cardGradient = Brush.linearGradient(
            0.0f to Color(0xFF6347A4).copy(alpha = 0.2f),
            0.8f to Color(0x00000000).copy(alpha = 0.2f),
            start = Offset(0f, 0f),
            end = Offset(Float.POSITIVE_INFINITY, 0f)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                top = 8.dp,
                bottom = 90.dp, // 👈 هذه المسافة تضمن عدم اختفاء آخر كارد خلف الـ Bottom Nav
                start = 0.dp,
                end = 0.dp

            ), // مسافة في أعلى وأسفل القائمة بالكامل
            verticalArrangement = Arrangement.spacedBy(12.dp) // 💡 تصنع مسافة تلقائية بمقدار 12dp بين كل كارد والآخر
        ) {

            items(state.tasksList, key = { task -> task.id })
            { task ->

                val dismissState = rememberSwipeToDismissBoxState(
                    confirmValueChange = { dismissValue ->
                        if (dismissValue == SwipeToDismissBoxValue.StartToEnd ||
                            dismissValue == SwipeToDismissBoxValue.EndToStart
                        ) {

                            // 🎯 إرسال Intent الحذف عند اكتمال السحب
                            onIntentTask(TaskIntent.DeleteTask(task.id))
                            true
                        } else {
                            false
                        }
                    }
                )

                SwipeToDismissBox(
                    state = dismissState,
                    enableDismissFromStartToEnd = true, // السحب لليمين
                    enableDismissFromEndToStart = true, // السحب لليسار
                    backgroundContent = {
                        // 🎨 خلفية السحب (تظهر باللون الأحمر مع أيقونة الحذف)
                        val color = when (dismissState.targetValue) {
                            SwipeToDismissBoxValue.Settled -> Color.Transparent
                            else -> Color(0xFFFF3D00).copy(alpha = 0.8f) // لون أحمر
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp)
                                .clip(RoundedCornerShape(17.dp))
                                .background(color)
                                .padding(horizontal = 20.dp),
                            contentAlignment = Alignment.CenterStart // محاذاة الأيقونة لجهة السحب
                        ) {

                        }
                    },

                    content = {
                        // 2. الـ OutlinedCard الآن أصبحت داخل الـ items لتتكرر مع كل عنصر
                        OutlinedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 110.dp)
                                .padding(horizontal = 16.dp),
                            shape = RoundedCornerShape(17.dp),
                            border = BorderStroke(1.dp, Color(0x2EAD8AFF)),
                            colors = CardDefaults.outlinedCardColors(containerColor = Color.Transparent)
                        ) {

                            // 3. محتويات الكارد من الداخل
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 20.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                // استدعاء تصميم السطر الخاص بكِ وتمرير بيانات الـ task الحالية له
                                TaskRow(
                                    titleTask = task.title,
                                    displayTime = "${task.totalMinutes} minutes"
                                )

                            }

                        }

                    }

                )


            }
        }
    }
}


@Composable
fun TaskRoute(
    viewModel: TaskViewModel = viewModel(),
    viewModelOverView: TaskOverviewViewModel = viewModel(), navController: NavController

) {

    val state by viewModel.uiState.collectAsState()
    val stateOverView by viewModelOverView.state.collectAsState()



    DailyOverviewScreen(
        state, stateOverView,
        onIntentTask = viewModel::onIntent,
        onNavigationClick = {
            // 👈 هذا الكود هو الذي يُنفذ عند ضغط السهم
            navController.navigate(Screen.MainHome.route) {
                popUpTo(Screen.MainHome.route) { inclusive = true }
            }
        })

}