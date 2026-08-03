package com.example.scoretask.taskmanagement.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scoretask.ui.theme.AlarmTextStyle
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import com.example.scoretask.BgScreen
import com.example.scoretask.R
import com.example.scoretask.taskmanagement.contract.TaskCreationState
import com.example.scoretask.taskmanagement.contract.TaskIntent
import com.example.scoretask.taskmanagement.viewmodel.TaskViewModel


@Composable
fun CustomBasicTimeInput(
    value: String,
    onValueChange: (String) -> Unit,
    unit: String,
    containerColor: Color,
    modifier: Modifier = Modifier
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        cursorBrush = SolidColor(Color(0xFFCAC4D0)), // هنا نغير لونه للأبيض ليظهر بوضوح على الخلفية البنفسجية
        modifier = modifier,
        textStyle = TextStyle(
            textAlign = TextAlign.Center, // الرقم يتسنتر جوه مساحته
            color = Color.White,
            fontSize = 23.sp,

            fontFamily = FontFamily(Font(R.font.roboto_regular))
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .background(containerColor, RoundedCornerShape(4.dp))
                    .border(
                        width = 1.dp,
                        color = Color(0xFFD0BCFF).copy(alpha = 0.5f),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 5.dp),
                // .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(

                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {

                    // نضع الـ TextField داخل Box ونعطيه عرضاً أدنى (Intrinsic width)
                    // عشان ميختفيش وفي نفس الوقت يفضل جنب الحرف
                    Box(
                        modifier = Modifier.width(IntrinsicSize.Min),
                        contentAlignment = Alignment.Center
                    ) {
                        // لو القيمة فاضية بنحط مساحة وهمية عشان الحقل ميبقاش عرضه صفر
                        if (value.isEmpty()) {
                            Text("00", color = Color.Transparent, fontSize = 22.5.sp)
                        }
                        innerTextField()
                    }

                    Text(
                        text = unit,
                        style = TextStyle(
                            fontSize = 23.sp,//22.5
                            color = Color.White,//(0xFFE6E1E5).copy(alpha = 0.7f),
                            fontFamily = FontFamily(Font(R.font.roboto_regular))
                        ),
                        modifier = Modifier.padding(
                            start = if (unit == "h") 1.dp else 4.dp
                        )
                    )
                }
            }
        }
    )
}



@Composable

fun EnterTask(
    state: TaskCreationState,
    onIntent: (TaskIntent) -> Unit

) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 28.dp),
        // مسافة X من فيجما
        // ارتفاع الكارت
        shape = RoundedCornerShape(18.dp),


        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        // نستخدم Box لعمل تراكب الطبقات
        Box(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color(0xFF000000).copy(alpha = 0.56f)) // الطبقة السوداء الداكنة
            ) //{
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color(0xFFD0BCFF).copy(alpha = 0.11f)) // طبقة اللمعة البنفسجية
            ) //{
            // محتويات الكارت هنا


            Column(modifier = Modifier.fillMaxWidth()) {

                Text(

                    text = "Set Task",
                    Modifier.padding(start = 12.dp, top = 8.dp),
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.roboto_medium)),

                    color = Color(0xFFCAC4D0),


                    )

                Text(
                    "Enter Task",
                    Modifier.padding(start = 12.dp, top = 34.dp),
                    color = Color(0xFFE6E1E5),
                    fontFamily = FontFamily(Font(R.font.roboto_regular)),

                    fontSize = 16.sp


                )

                OutlinedTextField(
                    value = state.title,
                    onValueChange = { newValue ->
                        // text = newValue
                        onIntent(
                            TaskIntent.TitleChanged(newValue)
                        )
                    },
                    label = { Text(text = "Task Title") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .padding(horizontal = 12.dp),
                    shape = RoundedCornerShape(2.dp),

                    colors = OutlinedTextFieldDefaults.colors(
                        // لون الإطار عندما تضغطين عليه للكتابة
                        focusedBorderColor = Color.White,

                        // لون الـ Label (كلمة Date) عند الضغط
                        focusedLabelColor = Color(0xFFCAC4D0),
                        // لون النص الذي يكتبه المستخدم
                        focusedTextColor = Color(0xFFCAC4D0)
                    )

                )


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.End // وضع الأزرار على اليمين كما في أندرويد القياسي
                ) {
                    // زر الإلغاء (هادئ بصرياً)
                    TextButton(onClick = {

                        onIntent(TaskIntent.CancelTask)
                    }) {
                        Text("Cancel", color = Color.White)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // زر التأكيد (واضح وجاذب للانتباه)
                    TextButton(onClick = {
                        Toast.makeText(
                            context,
                            "The task name has been successfully saved.",
                            Toast.LENGTH_SHORT
                        ).show()

                    }) {
                        Text("OK", color = Color.White)
                    }
                }


            }

        }
    }

}

@Composable
fun EnterExpectTime(
    state: TaskCreationState,
    onIntent: (TaskIntent) -> Unit,
    onNavigateToTimer: () -> Unit = {}
) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 28.dp),

        // مسافة X من فيجما
        // ارتفاع الكارت
        shape = RoundedCornerShape(18.dp),


        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {


        // نستخدم Box لعمل تراكب الطبقات
        Box(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color(0xFF000000).copy(alpha = 0.56f)) // الطبقة السوداء الداكنة
            ) //{
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color(0xFFD0BCFF).copy(alpha = 0.11f)) // طبقة اللمعة البنفسجية
            )
            Column(modifier = Modifier.fillMaxWidth()) {

                Text(

                    text = "Expect Time",
                    Modifier.padding(top = 12.dp, start = 12.dp, end = 12.dp),
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.roboto_medium)),

                    color = Color(0xFFCAC4D0),


                    )

                Spacer(modifier = Modifier.height(36.dp))//36?

                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .wrapContentHeight() // تعادل H 72 Hug
                            .padding(horizontal = 12.dp), // تعادل Padding 24 / 2
                    horizontalArrangement = Arrangement.spacedBy(6.dp), // تعادل Gap 12 / 2
                    verticalAlignment = Alignment.CenterVertically // تعادل Alignment Center

                ) {


                    // حقل الساعات
                    CustomBasicTimeInput(
                        value = state.hours,
                        //  value = hours,
                        onValueChange = { newValue ->
                            // hours = newValue
                            onIntent(TaskIntent.HoursChanged(newValue))
                        },
                        unit = "h",
                        containerColor = Color(0xFF4F378B),
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = ":",
                        modifier = Modifier.padding(horizontal = 8.dp),
                        style = TextStyle(
                            fontSize = 28.sp,
                            color = Color(0xFFE6E1E5)
                        )
                    )

                    // حقل الدقائق
                    CustomBasicTimeInput(
                        value = state.minutes,
                        // value = minuts,
                        onValueChange = { newValue ->
                            // minuts = newValue
                            onIntent(TaskIntent.MinutesChanged(newValue))
                        },
                        unit = "min",
                        containerColor = Color(0xFF7B42FF),
                        modifier = Modifier.weight(1f)
                    )

                }
// --- الأزرار تبقى كما هي في تصميمك ---
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = {

                        onIntent(TaskIntent.CancelTime)
                    }) {
                        Text("Cancel", color = Color.White)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(onClick = { /* OK */

                        //  Log.i("testButton::First",inputHolder.title)
                        if (state.title.isNotEmpty()) {
                            onIntent(TaskIntent.SaveTask)
                            onNavigateToTimer()

                            Toast.makeText(
                                context,
                                "The task has been successfully saved.",
                                Toast.LENGTH_SHORT
                            ).show()


                        }


                    }) {

                        Text("OK", color = Color.White)
                    }
                }


            }//colum end
        }
    }
}

@Composable
fun HomeRoute(
    onNavigateToTimer: () -> Unit = {},
    viewModel: TaskViewModel
) {
    val state by viewModel.state.collectAsState()


    HomeScreen(
        onNavigateToTimer = onNavigateToTimer,
        state = state,
        onIntent = viewModel::onIntent
    )
}









@Composable
fun HomeScreen(
    onNavigateToTimer: () -> Unit = {},
    state: TaskCreationState,
    onIntent: (TaskIntent) -> Unit

) {


    BgScreen()
    Column(
        Modifier.fillMaxSize()
    )// توسيط أفقي)

    {
        Spacer(modifier = Modifier.height(53.dp))

        Text(

            text = "Alarm",
            style = AlarmTextStyle,
            modifier = Modifier
                .wrapContentSize()

                .padding(start = 38.dp),

            )


        Spacer(modifier = Modifier.height(44.dp))

        EnterTask(

            state = state,
            onIntent = onIntent
        )

        Spacer(modifier = Modifier.height(20.dp))



        EnterExpectTime(
            onNavigateToTimer = onNavigateToTimer,
            state = state,
            onIntent = onIntent
        )


    }
}



