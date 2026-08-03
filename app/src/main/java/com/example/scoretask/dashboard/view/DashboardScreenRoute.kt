package com.example.scoretask.dashboard.view

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Black
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.scoretask.BgScreen
import com.example.scoretask.R
import com.example.scoretask.Screen
import com.example.scoretask.ScreenHeader
import com.example.scoretask.dashboard.contract.StateIntent
import com.example.scoretask.dashboard.contract.StatsState
import com.example.scoretask.dashboard.viewmodel.StatsViewModel
import com.example.scoretask.taskmanagement.contract.TaskUiState
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.compose.cartesian.data.lineSeries
import com.patrykandpatrick.vico.compose.cartesian.layer.CartesianLayerPadding
import com.patrykandpatrick.vico.compose.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.Fill
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.data.ExtraStore




@Composable
fun StateCart(
    value: String,
    title: String,
    iconInside: Int, // نمرر الـ Resource ID للأيقونة الداخلية
    cardGradient: Brush,
    weight: Float
    // modifier: Modifier = Modifier
) {

    Card(
        modifier = Modifier
            //.weight(1f)


            //.fillMaxWidth(0.4f) // تأخذ 90% من عرض الشاشة مهما كان حجمها
            // حذفنا الـ height(69.5.dp) الثابت لنجعلها مرنة
            .heightIn(min = 80.dp) // حد أدنى للارتفاع (139/2) لتجنب الانكماش الشديد
            // .padding(end = 16.dp)
            .background(
                brush = cardGradient,
                shape = RoundedCornerShape(8.dp) // الزوايا الدائرية
            )
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.1f), // إطار خفيف جداً لإعطاء تأثير الزجاج
                shape = RoundedCornerShape(8.dp)
            ),

        shape = RoundedCornerShape(8.dp),

        // مسافة خارجية عن حواف الشاشة

        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
        //border = BorderStroke(0.5.dp, Color.White.copy(alpha = 0.2f))
    ) {
////
        Row(
            modifier = Modifier
                .fillMaxWidth()

                .padding(vertical = 20.dp, horizontal = 8.dp),
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
                        painter = painterResource(id = iconInside), // أيقونة السهم مثلاً
                        contentDescription = null,
                        tint = Color(0xFF7446DE),
                        modifier = Modifier.size(10.dp) // حجم الأيقونة الداخلية (أصغر من الدائرة)
                    )
                }
            }
            Column {
                Text(
                    text = value,
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.sfpro_semibold)),
                    color = Color.White
                )
                Text(
                    text = title,
                    fontSize = 6.sp,
                    fontFamily = FontFamily(Font(R.font.sfpro_regular)),
                    color = Color.White.copy(alpha = 0.6f)
                )
            }

        }


    }
}
@SuppressLint("RestrictedApi")
@Composable
fun SimpleVicoChart(
    points: List<Number>,
    selectedTab: Int
) {
    val pointsGradient = Brush.verticalGradient(
        0.149f to Color(0xFFA47FFB),
        1.0f to Color(0xFF614B95)
    )


    val modelProducer = remember(selectedTab) { CartesianChartModelProducer() }

    // 1️⃣ تجهيز التسميات للـ Tab الحالي
    val axisLabels = remember(selectedTab) {
        when (selectedTab) {
            0 -> listOf("Morning", "Afternoon", "Evening", "Night")
            1 -> listOf("Sat", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri")
            2 -> listOf("W1", "W2", "W3", "W4")
            else -> listOf("Sat", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri")
        }
    }


    val safePoints = remember(points, axisLabels) {
        if (points.isNotEmpty() && points.size == axisLabels.size) {
            points
        } else {
            List(axisLabels.size) { 0 }
        }
    }

    val isAllZeros = remember(safePoints) {
        safePoints.all { it.toDouble() == 0.0 }
    }

    // 🎯 🎯 [تعديل 2]: تحديد الشفافية والألوان بناءً على حالة البيانات
    val lineColor = if (isAllZeros) Color(0xFFAD8AFF).copy(alpha = 0.25f) else Color(0xFFAD8AFF)
    // val markerColor = if (isAllZeros) Color(0xFFA47FFB).copy(alpha = 0.25f) else Color(0xFFA47FFB)
    val markerStroke = if (isAllZeros) Color(0xFFAD8AFF).copy(alpha = 0.4f) else Color(0xFFAD8AFF)

    val pointGradient = Brush.verticalGradient(
        0.149f to (if (isAllZeros) Color(0xFFA47FFB).copy(alpha = 0.2f) else Color(0xFFA47FFB)),
        1.0f to (if (isAllZeros) Color(0xFF614B95).copy(alpha = 0.2f) else Color(0xFF614B95))
    )
    // إرسال النقاط الجديدة فقط بدون extras
    LaunchedEffect(safePoints, selectedTab) {
        modelProducer.runTransaction {

            lineSeries {
                val xValues = safePoints.indices.toList()
                series(x = xValues, y = safePoints)
            }

        }
    }

    val borderBrush = Brush.linearGradient(
        0.0f to Color(255, 255, 255).copy(alpha = 0.0476f),
        1.0f to Color(107, 114, 128).copy(alpha = 0.2516f)
    )

    val customLineAsBox = rememberLineComponent(
        fill = Fill(Color.Transparent),
        thickness = 10.dp,
        strokeThickness = 1.dp,
        strokeFill = Fill(borderBrush)
    )

    val axisLabelComponent = rememberTextComponent(
        style = TextStyle(
            color = Color(0xFFA6A6A6),
            fontFamily = FontFamily(Font(R.font.sfpro_bold)),
            fontWeight = FontWeight.W700,
            fontSize = 8.sp,
            lineHeight = 12.sp,
            letterSpacing = 0.sp,
            textAlign = TextAlign.Center
        )
    )

    val dataMarkerComponent = rememberShapeComponent(
        fill = Fill(pointsGradient),
        shape = CircleShape,
        strokeFill = Fill(markerStroke),
        strokeThickness = if (isAllZeros) 1.dp else 2.dp

    )

    val lineSpec = LineCartesianLayer.rememberLine(
        fill = remember { LineCartesianLayer.LineFill.single(Fill(lineColor/*Color(0xFFAD8AFF)*/)) },
        interpolator = LineCartesianLayer.Interpolator.cubic(),
        pointProvider = remember {
            LineCartesianLayer.PointProvider.single(
                LineCartesianLayer.Point(
                    component = dataMarkerComponent,
                    size = if (isAllZeros) 6.dp else 10.dp

                )
            )
        }
    )

    val myRangeProvider = remember {
        object : CartesianLayerRangeProvider {
            override fun getMinY(minY: Double, maxY: Double, extraStore: ExtraStore) = 0.0
            override fun getMaxY(minY: Double, maxY: Double, extraStore: ExtraStore) =
                if (maxY == 0.0) 100.0 else maxY * 1.2// maxY * 1.2
        }
    }

    // 🎯 2️⃣ استخدام الـ key لربط الـ Producer والـ Chart معاً عند تغيير الـ Tab
    //  key(selectedTab) {
    // إعادة إنشاء الـ Producer لتفريغ ذاكرة العرض والتوزيع القديم (7 نقاط vs 4 نقاط)

    key(selectedTab) {
        CartesianChartHost(
            chart = rememberCartesianChart(
                rememberLineCartesianLayer(
                    lineProvider = LineCartesianLayer.LineProvider.series(lineSpec),
                    rangeProvider = myRangeProvider
                ),
                bottomAxis = HorizontalAxis.rememberBottom(
                    label = axisLabelComponent,
                    line = customLineAsBox,
                    tick = null,
                    guideline = null,
                    // 🎯 3️⃣ الاستغناء عن extraStore والقراءة المباشرة
                    valueFormatter = CartesianValueFormatter { _, value, _ ->
                        val index = value.toInt()
                        if (index in axisLabels.indices) {
                            axisLabels[index]
                        } else {
                            ""
                        }

                    }
                ),
                layerPadding = {
                    CartesianLayerPadding(
                        unscalableStart = 12.dp,
                        unscalableEnd = 12.dp
                    )
                }
            ),
            modelProducer = modelProducer,
            modifier = Modifier
                .padding(top = 20.dp, start = 4.dp, end = 4.dp, bottom = 20.dp)
                .wrapContentWidth()
                .heightIn(min = 110.dp, max = 150.dp)
        )
    }
}



@Composable
fun TimeMuscleProgressIndicator(
    state: TaskUiState,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,

        modifier = modifier
            .size(100.dp)


    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // --- 1. رسم الدائرة الخلفية (Background Circle) ---
            val backgroundRadius = size.minDimension / 2f
            drawCircle(
                color = Color(0xFF4D308F),
                radius = backgroundRadius,

                )
            // إطار الدائرة الرفيع جداً (0.3dp)
            drawCircle(
                color = Color(0xFF684DA7),
                radius = backgroundRadius,
                style = Stroke(width = 0.3.dp.toPx())
            )

            val sweepGradient = Brush.sweepGradient(
                0.0f to Color(0xFF200C4E),
                0.5f to Color(0xFF7F69B3),
                1.0f to Color(0xFF200C4E),
                center = Offset(size.width * 0.5612f, size.height * 0.5255f)
            )

            drawArc(
                brush = sweepGradient,
                startAngle = -90f,
                sweepAngle = 360f * (state.totalDailyEstimationAccuracy.toFloat() / 100f),//progress,
                useCenter = false,
                style = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round),

                )
        }

        val accuracy =
            state.totalDailyEstimationAccuracy // القيمة القادمة من الـ ViewModel (مثلاً 90.62)


        val formattedAccuracy = if (accuracy % 1 == 0.0) {
            "${accuracy.toInt()}%"
        } else {
            "${String.format("%.1f", accuracy)}%" // 👈 ستظهر: 90.6% (علامة عشرية واحدة)
        }



        Text(
            text = formattedAccuracy,
            style = TextStyle(
                fontWeight = Black,
                lineHeight = 11.sp,
                letterSpacing = 1.sp,// FontWeight(860),
                fontFamily = FontFamily(Font(R.font.sfpro_bold)),

                ),
            color = Color.White, fontSize = 16.sp
        )
    }
}



@Composable
fun CustomTabRow(
    selectedTab: Int,         // 👈 يستقبل رقم فقط
    onTabSelected: (Int) -> Unit
) {
    val titles = listOf("Day", "Week", "Month")
    // var state by remember { mutableStateOf(1) }

    // الحاوية الخارجية لضبط الشكل العام (Glassmorphism)
    Card(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .height(50.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF4A358E).copy(alpha = 0.5f)),
        border = BorderStroke(0.5.dp, Color.White.copy(alpha = 0.1f))
    ) {
        TabRow(
            selectedTabIndex = selectedTab, //state,
            containerColor = Color.Transparent,
            contentColor = Color.White,
            divider = {}, // إخفاء الخط السفلي الافتراضي
            indicator = { tabPositions ->

            }

        ) {
            titles.forEachIndexed { index, title ->
                val isSelected = selectedTab == index
                Tab(
                    selected = isSelected,
                    // selected = selectedTab == index,
                    onClick = {
                        onTabSelected(index)
                    },
                    text = {
                        Text(
                            text = title,
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontFamily = FontFamily(Font(R.font.sfpro_medium)),

                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.White else Color.White.copy(alpha = 0.7f)
                            )
                        )
                    }
                )


            }
        }
    }
}
@Composable
fun DashboardScreen(
    state: StatsState,
    onIntent: (StateIntent) -> Unit,
    onNavigationClick: () -> Unit

) {

    BgScreen()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    )

    {

        Spacer(Modifier.height(50.dp))
        ScreenHeader("My Score", R.drawable.back_arrow, onNavigationClick)
        Spacer(Modifier.height(50.dp))
        val cardGradient = Brush.linearGradient(
            0.0f to Color(0xFF6347A4).copy(alpha = 0.2f), // البداية (تقريباً -6.66%)
            0.8f to Color(0xFF000000).copy(alpha = 0.2f), // النهاية عند 80.77%
            start = Offset(0f, 0f),
            end = Offset(Float.POSITIVE_INFINITY, 0f) // لضمان الاتجاه الأفقي (90 درجة)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),//35
            horizontalArrangement = Arrangement.spacedBy(30.dp) //30 المسافة بين البطاقتين
        ) {

            Box(modifier = Modifier.weight(1f)) {
                StateCart(
                    value = "${state.totalSessionCount}", //"12",
                    title = "Total Tasks Completed",
                    iconInside = R.drawable.icon_inside_circle_left,
                    cardGradient = cardGradient,
                    weight = 1f
                )
            }

            Box(modifier = Modifier.weight(1f)) {
                StateCart(
                    "${state.totalFocusTimeMinutes}m ${state.totalFocusTimeSeconds}s",
                    //"5h 49m",
                    "Total Foucs Time",
                    R.drawable.icon_inside_right,
                    cardGradient = cardGradient,
                    weight = 1f
                )
            }

        }

        Spacer(Modifier.height(50.dp))
        CustomTabRow(
            selectedTab = state.selectedTab,
            onTabSelected = { newIndex ->
                onIntent(StateIntent.ChangeTab(newIndex))
            })
        Spacer(Modifier.height(30.dp))

        Text(
            text = "Activity",
            color = Color.White,
            fontSize = 16.sp,
            lineHeight = 11.sp,

            //  modifier = Modifier.weight(1f),

            style = TextStyle(
                lineHeightStyle = LineHeightStyle(
                    alignment = LineHeightStyle.Alignment.Center,
                    trim = LineHeightStyle.Trim.Both // قص أي مسافات إضافية
                ),
                fontWeight = Black,// FontWeight(860),
                fontFamily = FontFamily(Font(R.font.sfpro_bold)),

                )


        )
        Spacer(Modifier.height(20.dp))
        Box(
            modifier = Modifier
                // 1. جعل العرض متجاوباً (يأخذ 90% من عرض الشاشة)
                .fillMaxWidth(0.90f)

                // 2. الارتفاع يتحدد بناءً على المحتوى أو نسبة معينة
                .heightIn(min = 160.dp, max = 200.dp)


                .clip(RoundedCornerShape(17.dp))
                .background(
                    // التدرج اللوني الذي طلبتِيه مع الشفافية
                    Brush.linearGradient(
                        0.0f to Color(0xFF6347A4).copy(alpha = 0.2f),
                        0.8f to Color(0xFF000000).copy(alpha = 0.2f),
                        start = Offset(0f, 0f),
                        end = Offset(Float.POSITIVE_INFINITY, 0f)
                    )
                )
                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.1f), // إطار خفيف جداً لإعطاء تأثير الزجاج
                    shape = RoundedCornerShape(17.dp)
                )
        ) {
            SimpleVicoChart(
                points = state.chartPoints,
                selectedTab = state.selectedTab


            )
        }

    }
}
@Composable
fun DashboardScreenRoute(viewModel: StatsViewModel, navController: NavController) {

    val state by viewModel.state.collectAsStateWithLifecycle()


    DashboardScreen(
        state = state,
        onIntent = viewModel::onIntent,
        onNavigationClick = {
            // 👈 هذا الكود هو الذي يُنفذ عند ضغط السهم
            navController.navigate(Screen.MainHome.route) {
                popUpTo(Screen.MainHome.route) { inclusive = true }
            }
        }
    )


}