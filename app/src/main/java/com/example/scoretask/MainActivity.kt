package com.example.scoretask

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import android.R.attr.centerX
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import androidx.annotation.Nullable
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Black
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.scoretask.model.TaskTemplateEntity
import com.example.scoretask.model.totalMinutes
import com.example.scoretask.ui.theme.AlarmTextStyle
import com.example.scoretask.ui.theme.ScoreTaskTheme
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianLayerRangeProvider
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
import db.ConcreteLocalSource
import kotlinx.coroutines.delay
import kotlin.collections.minusAssign
import kotlin.compareTo
import kotlin.div
import androidx.compose.material3.Checkbox
import androidx.compose.ui.draw.shadow
import kotlin.text.toFloat

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val localSource = ConcreteLocalSource.getInstance(applicationContext)
        val repository = TaskRepositoryImpl.getInstance(localSource)

        // 🛠️ 2. إنشاء الـ ViewModel بـ السطر المعتمد من جوجل باستخدام الـ Factory
        val taskViewModel: TaskViewModel by viewModels {
            TaskViewModelFactory(repository)
        }

       /* val timerViewModel: TimerViewModel by viewModels {
            TimerViewModelFactory(repository)
        }*/


        setContent {

            window.statusBarColor = android.graphics.Color.parseColor("#331E66")//.TRANSPARENT


            ScoreTaskTheme {

                // 1. تعريف الـ NavController الرئيسي للتطبيق كله
                val rootNavController = rememberNavController()
                // TaskScreenWrapper(taskViewModel)

                // 2. الـ NavHost الرئيسي (المسرح الأكبر للتطبيق)
                NavHost(
                    navController = rootNavController,
                    startDestination = Screen.Splash.route // نقطة انطلاق التطبيق الحتمية
                ) {




                    // --- المرحلة الأولى: الـ Splash ---
                    composable(route = Screen.Splash.route) {
                        SplashScreen(
                            onTimeout = {
                                rootNavController.navigate(Screen.Onboarding.route) {
                                    popUpTo(Screen.Splash.route) { inclusive = true }
                                }
                            }
                        )
                    }

                    // --- المرحلة الثانية: الـ Onboarding ---
                    composable(route = Screen.Onboarding.route) {
                        OnBoardingScreen(onFinished = {

                            rootNavController.navigate(Screen.MainHome.route) {
                                popUpTo(Screen.Onboarding.route) { inclusive = true }
                            }
                        })
                    }



                    composable(route = Screen.MainHome.route) {

                        BottomNav( taskViewModel, rootNavController)
                    }



                    // val state by timerViewModel.state.collectAsStateWithLifecycle()
                    // 🚀 سجلنا شاشة التايمر هنا كشاشة كاملة في الخريطة الكبرى
                    composable(route = Screen.TimerTask.route) {

                        val timerViewModel: TimerViewModel = viewModel(
                            factory = TimerViewModelFactory(repository)
                        )

                        ScoreRoute(timerViewModel, rootNavController)
                    }
                    composable (route = Screen.TaskCompletion.route){//TaskCompletion.route){
                        TaskCompletion()
                    }





                }

            }
        }
    }



    @Composable
    fun BottomNav(taskViewModel: TaskViewModel ,rootNavController: NavController) {

        // 1. تعريف الحالة في قمة الدالة (Top of the function)
        val selectedNavigationIndex = rememberSaveable {
            mutableIntStateOf(0)
        }
        val bottomNavController = rememberNavController()
        Scaffold(/*(modifier = Modifier.fillMaxSize()*/

            containerColor = Color(0xFF331E66),
            bottomBar = {
                val gradientBrush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFEEEDED), // اللون الأول عند 0.19%
                        Color(0xFF888888)  // اللون الثاني عند النهاية
                    )
                )
                // نضع كود الـ NavigationBar هنا ليكون ثابتاً في الأسفل
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 18.dp), // مسافة بسيطة عن حافة الشاشة السفلية
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier
                            // بدلاً من width: 400، نستخدم نسبة من عرض الشاشة (مثلاً 90%)
                            .fillMaxWidth(0.8f)
                            // بدلاً من height الثابت، نحدد ارتفاعاً مرناً أو نتركه يتمدد حسب المحتوى
                            .height(70.dp)
                            .padding(horizontal = 8.dp),
                        shape = RoundedCornerShape(12.dp), // نفس الـ border-radius المطلوب
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF331E66) // نفس الـ background المطلوب
                        )
                    )

                    {

                        Row(
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            navigationItems.forEachIndexed { index, item ->
                                val isSelected =
                                    selectedNavigationIndex.intValue == index
                                if (isSelected) {
                                    Box(
                                        modifier = Modifier
                                            .wrapContentSize()
                                            .clip(RoundedCornerShape(5.dp))
                                            .background(brush = gradientBrush) // استخدام التدرج الخاص بكِ
                                            .padding(
                                                vertical = 6.dp,
                                                horizontal = 10.dp
                                            )
                                            .clickable {
                                                selectedNavigationIndex.intValue = index
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                painter = painterResource(id = item.icon),
                                                contentDescription = item.title,
                                                modifier = Modifier.size(20.dp),
                                                tint = Color(0xFF331E66) // لون الأيقونة داكن ليظهر فوق الـ Gradient
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = item.title,
                                                color = Color(0xFF331E66),
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                } else {
                                    // الحالة العادية: أيقونة فقط بدون خلفية
                                    IconButton(onClick = {
                                        selectedNavigationIndex.intValue = index

                                        bottomNavController.navigate(item.route) {

                                            // نصيحة احترافية: هذا السطر يمنع تراكم الشاشات فوق بعضها في الـ Backstack
                                            popUpTo(bottomNavController.graph.startDestinationId) {
                                                saveState = true
                                            }
                                            launchSingleTop =
                                                true // يمنع تكرار نفس الشاشة فوق نفسها
                                            restoreState =
                                                true // يستعيد حالة الشاشة فوراً بدون إعادة تحميل
                                        }

                                    }) {
                                        Icon(
                                            painter = painterResource(id = item.icon),
                                            contentDescription = item.title,
                                            modifier = Modifier.size(24.dp),
                                            tint = Color.White // لون باهت للعناصر غير المختارة
                                        )
                                    }
                                }

                            }
                        }
                    }
                }
            }
        )
        { innerPadding ->


                // 2. الشاشة الأولى (Task Screen)


            NavHost(
                navController = bottomNavController,
                startDestination = Screen.MainHome.route,


                ) {
                composable(route = Screen.MainHome.route) {

                   HomeRoute(viewModel = taskViewModel,
                       onNavigateToTimer = {
                           // هنا نأمر الموجه الأكبر بفتح شاشة التايمر المسجلة فوق
                           rootNavController.navigate(Screen.TimerTask.route)
                       }
                   )
                   // HomeScreen()

                   // TaskScreenWrapper(viewModel = taskViewModel )
                }
                composable(route = Screen.Task.route) {
                    TaskRoute(taskViewModel)
                   // DailyOverviewScreen(taskViewModel)

                }
                composable(
                    route = Screen.Stats.route
                ) {
                    DashboardScreen()
                   // ScoreTaskTimer()
                }





            }




            val SplashCenterColor = Color(0xFF6B3FE2) // اللون الأرجواني الفاتح في الوسط
            val SplashOuterColor = Color(0xFF4A148C)

            val gradientBrush = Brush.linearGradient(
                colors = listOf(SplashCenterColor, SplashOuterColor),
                start = Offset(0f, 0f),
                end = Offset.Infinite // استخدام Offset.Infinite يعطي نفس نتيجة Float.POSITIVE_INFINITY
            )


        }


    }
}

private data class PreparedGlow(
    val brush: Brush,
    val shapeType: GlowShape,
    val topLeft: Offset,
    val size: Size,
    val center: Offset,
    val alpha: Float = 1.0f,
    val radius: Float,

)

@Composable
fun AppGlowBackground(
    glowList: List<GlowConfig>, // 🎯 ما زلنا نستقبل الكلاس الأصلي كما هو من الخارج
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {


    val color1 = Color(0xFFEBEBEB)
    val color2 = Color(0xFFE3EAFF)
    val color3 = Color(0xFF8350DB)
    val basePurple = Color(0xFF4A2997)
   // val basePurple = Color(0xFF6943AC)
// بنبدأ بالخلفية البنفسجية الأساسية

    Box(
       modifier =  modifier
            .fillMaxSize()
            .background(basePurple)
            .drawWithCache {
                val designWidth = 720f
                val designHeight = 1600f

                // 1. ⚡ غرفة الكاش العلوية: تحسب وتنشئ الـ Objects مرة واحدة فقط عند فتح الشاشة
                val preparedGlows = glowList.map { glow ->

                    // حساب المقاسات الحقيقية بناءً على حجم الشاشة الحالي (size)
                    val glowWidth = size.width * (glow.widthRatio / designWidth)
                    val glowHeight = size.height * (glow.heightRatio / designHeight)
                    val offsetX = size.width * (glow.offsetXRatio / designWidth)

                    val offsetY = size.height * (glow.offsetYRatio / designHeight)
                     Log.i("size width: ",size.width.toString())
                    val centerX = offsetX + (glowWidth * 0.6186f)
                    val centerY = offsetY + (glowHeight * 0.3892f)
                    val calculatedRadius = glowWidth * glow.radiusPercent/*0.5772f*/

                    // تجهيز مصفوفة الألوان في الكاش ومنع استدعاء toTypedArray() أثناء الرسم
                    val stops = glow.customColors?.invoke(color1, color2) ?: listOf(
                        0.0f to color1,
                        0.375f to color2,
                        1.0f to Color.Transparent
                    )

                    val gradientBrush = Brush.radialGradient(
                        colorStops = stops.toTypedArray(),
                        center = Offset(centerX, centerY),
                        radius = calculatedRadius
                    )

                    // حفظ الحسابات الجاهزة والفرشاة داخل الكلاس المساعد
                    PreparedGlow(
                        brush = gradientBrush,
                        shapeType = glow.shapeType,
                        topLeft = Offset(offsetX, offsetY),
                        size = Size(glowWidth, glowHeight),
                       // size = Size(size.width, glowHeight),
                        center = Offset(centerX, centerY),
                        radius = calculatedRadius,
                        alpha = glow.alpha
                    )
                }

                // 2. 🎨 غرفة الرسم النظيفة: ترسم كائنات جاهزة تماماً مع كل فريم (Zero-Allocation)
                onDrawBehind {
                    preparedGlows.forEach { glow ->
                        when (glow.shapeType) {
                            GlowShape.CIRCLE -> drawCircle(
                                brush = glow.brush,
                                radius = glow.radius,
                                center = glow.center,

                                blendMode = BlendMode.Overlay
                            )
                            GlowShape.OVAL -> drawOval(
                                brush = glow.brush,
                                topLeft = glow.topLeft,
                                size = glow.size,
                                blendMode = BlendMode.Overlay
                            )
                            GlowShape.RECTANGLE -> drawRect(
                                brush = glow.brush,
                                topLeft = glow.topLeft,
                                size = Size(size.width, glow.size.width), // استخدام عرض الشاشة الحقيقي للتصميم المستطيل
                                blendMode = BlendMode.Overlay
                            )
                        }
                    }
                }
            }
           /* .blur(
                radius = 81.dp,
                edgeTreatment = BlurredEdgeTreatment.Unbounded
            )*/,
        content = content
    )
}

@Composable
fun BgScreen() {
    val bgGlows = remember {
        listOf(
            GlowConfig(widthRatio = 938f, heightRatio = 1078f, offsetXRatio = -287f, offsetYRatio = -378f, shapeType = GlowShape.CIRCLE, radiusPercent = 0.47f),
            GlowConfig(widthRatio = 938f, heightRatio = 1078f, offsetXRatio = 177f, offsetYRatio = 509f, shapeType = GlowShape.CIRCLE, radiusPercent = 0.44f)
        )
    }

    AppGlowBackground(glowList = bgGlows) {
        // واجهة الشاشة هنا
    }
}

val navigationItems = listOf(
    NavigationItem(
        title = "Home",
        icon = R.drawable.icon_home,
        route = Screen.MainHome.route
    ),
    NavigationItem(
        title = "Task",
        icon = R.drawable.icon_task,
        route = Screen.Task.route
    ),

    NavigationItem(
        title = "Stats",
        icon = R.drawable.stat_icon_3_,
        route = Screen.Stats.route
    ),
)


val basePurple = Color(0xFF4A2997)

val color1 = Color(0xFFEBEBEB)
val color2 = Color(0xFFE3EAFF)
val colorSplash3 = Color(0xFF8350DB)
val color3 = Color(0xFF6943AC)


@SuppressLint("RestrictedApi", "RememberReturnType")
@Composable
fun SimpleVicoChart() {


    val pointsGradient = Brush.verticalGradient(
        0.149f to Color(0xFFA47FFB), // اللون الفاتح عند نسبة 14.9%
        1.0f to Color(0xFF614B95)    // اللون الغامق عند النهاية
    )
    // 1. Initialize the model producer (usually in a ViewModel)
    val modelProducer = remember { CartesianChartModelProducer() }
    val daysOfWeek = listOf("Sat", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri")

// 2. Load data into the producer
    LaunchedEffect(Unit) {
        modelProducer.runTransaction {
            lineSeries { series(4, 16, 8, 10, 7, 11, 3) }
        }
    }


    val borderBrush = Brush.linearGradient(
        0.0f to Color(255, 255, 255).copy(alpha = 0.0476f),
        1.0f to Color(107, 114, 128).copy(alpha = 0.2516f)
    )

    val customLineAsBox = rememberLineComponent(
        fill = Fill(Color.Transparent), // القلب شفاف تماماً
        thickness = 10.dp,        // الارتفاع الكلي للمستطيل كما في تصميمك (height: 10)
        strokeThickness = 1.dp,      // سمك الإطار (border-width: 1px)
        strokeFill = Fill(borderBrush), // التدرج اللوني على الحدود فقط

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
        // لون النقطة (أبيض من الداخل) مع تحويله لـ Int
        // fill = Fill(Color.White),
        fill = Fill(pointsGradient),
        // شكل النقطة (دائرة) - يحتاج لـ core
        shape = CircleShape,
        // إطار بلون الخط البنفسجي
        strokeFill = Fill(Color(0xFFAD8AFF)),

        strokeThickness = 2.dp
    )


    val lineSpec = LineCartesianLayer.rememberLine(
        fill = remember { LineCartesianLayer.LineFill.single(Fill(Color(0xFFAD8AFF))) },
        // نستخدم interpolator بدلاً من pointConnector
        interpolator = LineCartesianLayer.Interpolator.cubic(),//.MonotoneCubic(),


        pointProvider = remember {
            LineCartesianLayer.PointProvider.single(
                // الحل: يجب تغليف الـ Component والـ sizeDp داخل كائن Point
                LineCartesianLayer.Point(
                    component = dataMarkerComponent,
                    size = 10.dp // حجم الدائرة الصغيرة
                )

            )
        }
    )


// 3. Display the chart
    val myRangeProvider = remember {
        object : CartesianLayerRangeProvider {
            override fun getMinY(minY: Double, maxY: Double, extraStore: ExtraStore) =
                0.0 // البدء من الصفر دائماً

            override fun getMaxY(minY: Double, maxY: Double, extraStore: ExtraStore) =
                maxY * 1.2 // إضافة 20% يدوياً
        }
    }

    CartesianChartHost(

        chart = rememberCartesianChart(


            rememberLineCartesianLayer(
                lineProvider = LineCartesianLayer.LineProvider.series(lineSpec),
                //  startAxis = VerticalAxis.rememberStart(),
                // نستخدم الاسم الجديد هنا: rangeProvider بدلاً من axisValueOverrider
                rangeProvider = myRangeProvider
            ),


            bottomAxis = HorizontalAxis.rememberBottom(

                label = axisLabelComponent,
                line = customLineAsBox,
                tick = null,

                guideline = null, // إخفاء الشبكة
                valueFormatter = { context, value, _ ->

                    daysOfWeek.getOrNull(value.toInt()) ?: ""
                }
            ),


            layerPadding = {
                CartesianLayerPadding(
                    // نستخدم unscalable لأن حجم الدائرة ثابت (مثلاً 10dp)
                    unscalableStart = 12.dp,
                    unscalableEnd = 12.dp,

                    // الـ top والـ bottom عادة لا يحتاجان لتقسيم scalable/unscalable في التعريفات البسيطة
                )
            }


        ),

        modelProducer = modelProducer,

        modifier = Modifier

            // تحويل الـ top: 101px والـ left: 22px لمسافات داخلية
            .padding(top = 20.dp, start = 4.dp, end = 4.dp, bottom = 20.dp)
            .wrapContentWidth()
            .heightIn(min = 110.dp, max = 150.dp)

    )

}


@Composable
fun CustomTabRow() {
    val titles = listOf("Day", "Week", "Month")
    var state by remember { mutableStateOf(1) } // نبدأ بـ Week (Index 1)

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
            selectedTabIndex = state,
            containerColor = Color.Transparent,
            contentColor = Color.White,
            divider = {}, // إخفاء الخط السفلي الافتراضي
            indicator = { tabPositions ->

            }

        ) {
            titles.forEachIndexed { index, title ->
                Tab(
                    selected = state == index,
                    onClick = { state = index },
                    text = {
                        Text(
                            text = title,
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontFamily = FontFamily(Font(R.font.sfpro_medium)),

                                fontWeight = if (state == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (state == index) Color.White else Color.White.copy(alpha = 0.7f)
                            )
                        )
                    }
                )


            }
        }
    }
}


@Composable
fun TimeMuscleProgressIndicator(
    progress: Float, // القيمة من 0.0 إلى 1.0
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
                // style = Fill
            )
            // إطار الدائرة الرفيع جداً (0.3dp)
            drawCircle(
                color = Color(0xFF684DA7),
                radius = backgroundRadius,
                style = Stroke(width = 0.3.dp.toPx())
            )


            // محاكاة Conic Gradient باستخدام SweepGradient
            val sweepGradient = Brush.sweepGradient(
                0.0f to Color(0xFF200C4E),
                0.5f to Color(0xFF7F69B3),
                1.0f to Color(0xFF200C4E),
                center = Offset(size.width * 0.5612f, size.height * 0.5255f)
            )

            drawArc(
                brush = sweepGradient,
                startAngle = -90f, // البدء من الأعلى (الساعة 12)
                sweepAngle = 360f * progress,
                useCenter = false,
                style = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round),

                )
        }

        Text(
            text = "92%",
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
//CustomTopAppBar

@Composable
fun ScreenHeader (
    title: String,
    // 1. تمرير الأيقونة اليسرى كـ Resource ID، وافتراضياً هي أيقونة الـ Back الخاص بكِ
    @DrawableRes navigationIcon: Int = R.drawable.icon_back,
    onNavigationClick: () -> Unit = {},

    // 2. زر التعديل نجعله اختياري (null بافتراض)
    showEditButton: Boolean = false,
    onEditClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .statusBarsPadding(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        // أيقونة التنقل (تتغير تلقائياً حسب الشاشة)
        IconButton(
            onClick = onNavigationClick,
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                painter = painterResource(id = navigationIcon),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // عنوان الشاشة
        Text(
            text = title,
            color = Color.White,
            fontSize = 20.sp,
            modifier = Modifier.weight(1f),
            style = TextStyle(
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily(Font(R.font.sfpro_bold))
            )
        )

        // 3. السحر هنا: إذا كانت قيمة showEditButton تساوي true سيتم رسم الزر، وإلا سيختفي تماماً!
        if (showEditButton) {
            Box(
                modifier = Modifier
                    .minimumInteractiveComponentSize()
                    .wrapContentSize(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = onEditClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    modifier = Modifier
                        .wrapContentWidth()
                        .height(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_round_edit),
                            contentDescription = null,
                            tint = Color(0xFF8C5BFF),
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "Edit",
                            color = Color(0xFF040415),
                            style = TextStyle(
                                fontFamily = FontFamily(Font(R.font.inter_medium)),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                lineHeight = 10.sp
                            )
                        )
                    }
                }
            }
        }
    }
}
/*
@Composable
fun RowCard(
    title: String,             // نمرر النص هنا
    iconResId: Int
) {
    var isChecked by remember { mutableStateOf(true) }

    Row(
        Modifier.padding(start = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = iconResId/*R.drawable.img_finish*/),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.width(61.dp).height(41.dp).padding(start = 8.dp)

        )

        Text(
            title,
            color = Color.White,
            fontSize = 14.sp,
            lineHeight = 11.sp,
            modifier = Modifier.weight(1f),
            fontFamily = FontFamily(Font(R.font.sfpro_regular)),
            fontWeight = FontWeight.Bold,

            )
        Checkbox(
            checked = isChecked,
            onCheckedChange = { isChecked = it },

            )

    }
}*/

enum class TaskResultStatus(val title: String) {
    FINISH("Finish"),
    FINISH_EXTRA("Finish - (Need extra time)"),
    NOT_FINISH("Not Finish"),
    NOT_FINISH_EXTRA("Not Finish - (Need extra time)");

    // دالة ترجع السؤال المناسب لكل حالة
    fun getQuestion(): String {
        return when (this) {
           // FINISH -> "Great job! Do you need a buffer time to review your achievement?"
            FINISH_EXTRA -> "How much extra time do you need to completely wrap it up?"
            NOT_FINISH -> "It's okay! How much extra time do you need for the next session?"
            NOT_FINISH_EXTRA -> "Don't panic! Select the extra time needed to continue now:"
        }
    }

    fun getPsychologyOptions(): List<String> {
        return when (this) {
            FINISH_EXTRA-> emptyList()
                    //  FINISH -> emptyList()
           /* FINISH_EXTRA -> listOf(
                "🧩 Task was more complex than expected",
                "📝 Need extra polish / double-check",
                "⏳ Underestimated the required sub-tasks"
            )*/
            NOT_FINISH -> listOf(
                "🥱 I lost momentum / got distracted",
                "🧠 Cognitive fatigue / brain fog",
                "🛑 Hit a hard roadblock / got stuck"
            )
            NOT_FINISH_EXTRA -> listOf(
                "⚡ Unexpected friction or technical bug",
                "📱 Environment / notification distraction",
                "🔄 Perfectionism holding me back"
            )
        }
    }

    // دالة ترجع خيارات الوقت بالدقائق لكل حالة (ديناميكية)
    fun getDurationOptions(): List<Int> {
        return when (this) {
           // FINISH -> listOf(5, 10) // وقت بسيط للمراجعة
            FINISH_EXTRA -> listOf(10, 15, 20, 30) // وقت متوسط للتقفيل
            NOT_FINISH ->emptyList()// listOf(15, 30, 45, 60) // وقت كبير لجلسة تانية
            NOT_FINISH_EXTRA -> listOf(20, 30, 40, 50)
        }
    }
}
@Composable
fun RowCard(
    title: String,
    iconResId: Int,
    isSelected: Boolean,      // يحدد برمجياً هل هذا الصف هو المختار حالياً أم لا
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
            )
        } else {
            // مساحة فارغة بديلة للحفاظ على محاذاة العناصر إذا لم يكن مختاراً
            Spacer(modifier = Modifier.padding(end = 12.dp).size(16.dp))
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskCompletion() {
    // 2️⃣ الـ States الناقصة للتحكم في الاختيارات والـ Bottom Sheet
    var selectedStatus by remember { mutableStateOf<TaskResultStatus?>(null) }
    var selectedExtraTime by remember { mutableStateOf<Int?>(null) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedPsychologyReason by remember { mutableStateOf<String?>(null) }
    val sheetState = rememberModalBottomSheetState()

    BgScreen() // الخلفية الخاصة بكِ

    // تم تصليح الـ Column وفتح القوس المظبوط {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(50.dp))

        // الـ Header المخصص بتاعكِ
        ScreenHeader("ReadingSeationnnn", showEditButton = false)

        Spacer(Modifier.height(50.dp))

        // شارة الـ Time out البيضاء
        Text(
            text = "Time out",
            modifier = Modifier
                .shadow(elevation = 4.dp, shape = RoundedCornerShape(20.dp))
                .background(color = Color.White, shape = RoundedCornerShape(20.dp))
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .wrapContentSize(),
            color = Color(0xFFFF110D),
            fontSize = 20.sp,
            fontFamily = FontFamily(Font(R.font.inter_semibold)),
            style = TextStyle(
                lineHeightStyle = LineHeightStyle(
                    alignment = LineHeightStyle.Alignment.Center,
                    trim = LineHeightStyle.Trim.Both
                ),
                fontWeight = FontWeight.Bold,
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            )
        )

        Spacer(Modifier.height(40.dp))

        // نص التايمر باصفار
        Text(
            text = "00:00",
            color = Color.White,
            fontSize = 32.sp,
            letterSpacing = 1.sp,
            lineHeight = 11.sp,
            fontFamily = FontFamily(Font(R.font.sfpro_bold)),
            style = TextStyle(
                lineHeightStyle = LineHeightStyle(
                    alignment = LineHeightStyle.Alignment.Center,
                    trim = LineHeightStyle.Trim.Both
                ),
                fontWeight = FontWeight.Bold,
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            )
        )

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
                    modifier = Modifier.width(11.dp).height(20.dp)
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
                    val iconRes = when(status) {
                        TaskResultStatus.FINISH -> R.drawable.img_finish
                        TaskResultStatus.FINISH_EXTRA -> R.drawable.img_finish_need_extra_time
                        TaskResultStatus.NOT_FINISH -> R.drawable.img_finish
                        TaskResultStatus.NOT_FINISH_EXTRA -> R.drawable.img_not_finish
                    }

                    RowCard(
                        title = status.title,
                        iconResId = iconRes,
                        isSelected = selectedStatus == status,
                        onClick = {
                            selectedStatus = status
                            selectedExtraTime = null // ريست للوقت القديم لو اختار حالة تانية
                            selectedPsychologyReason = null
                            showBottomSheet = true  // افتح الستارة فوراً!
                        }
                    )
                }
            }
        }
    } // نهاية الـ Column الرئيسي

    // 4️⃣ الـ Modal Bottom Sheet المفقود (الستارة اللي بتظهر من تحت)
    if (showBottomSheet && selectedStatus != null) {
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
                    text = selectedStatus!!.getQuestion(),
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 24.dp)
                )


                if (selectedStatus != TaskResultStatus.FINISH) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
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
                    selectedStatus!!.getDurationOptions().forEach { minutes ->
                        val isTimeSelected = selectedExtraTime == minutes

                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .shadow(if (isTimeSelected) 4.dp else 0.dp, CircleShape)
                                .background(
                                    color = if (isTimeSelected) Color(0xFF8C5BFF) else Color.White.copy(alpha = 0.1f),
                                    shape = CircleShape
                                )
                                .clickable { selectedExtraTime = minutes },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "+$minutes\nm",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                val isConfirmEnabled = if (selectedStatus == TaskResultStatus.FINISH) {
                    selectedExtraTime != null
                } else {
                    selectedExtraTime != null && selectedPsychologyReason != null
                }

                // زرار التأكيد النهائي
                //
                // للـ Bottom Sheet
                Button(
                    onClick = {
                        showBottomSheet = false

                        // TODO: هنا أرسلي الـ `selectedStatus` والـ `selectedExtraTime` للـ ViewModel
                    },
                   // enabled = selectedExtraTime != null, // لا ينقر إلا بعد تحديد الوقت
                    enabled = isConfirmEnabled,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text("Confirm Selection", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
/*
@Composable
fun TaskCompletion() {

    BgScreen()
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        )

        {

            Spacer(Modifier.height(50.dp))

            ScreenHeader("ReadingSeationnnn", showEditButton = false)

            Spacer(Modifier.height(50.dp))

            /*   Text(
                text = "Time out",
                color = Color.Red, // اللون الأحمر كما في الصورة
                style = TextStyle(
                    fontFamily = FontFamily.SansSerif, // أو الخط الذي تستخدمينه
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    platformStyle = PlatformTextStyle(includeFontPadding = false),


                )
            )*/


               /* Surface(
                    modifier = Modifier
                        .wrapContentSize(),

                    // 260 / 2
                       // .height(36.dp), // 73 / 2
                    shape = CircleShape, // لجعل الحواف دائرية تماماً مثل الصورة
                    color = Color.White, // الخلفية البيضاء كما في السكرين
                    shadowElevation = 4.dp // إضافة ظل خفيف ليعطي عمق للزر فوق الخلفية البنفسجية
                ) {*/


                  /*  Box(
                        contentAlignment = Alignment.Center,
                        //color = Color.White,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {*/
                        Text(
                            text = "Time out",
                            modifier = Modifier
                          //  .shadow(elevation = 4.dp, shape = RoundedCornerShape(20.dp))
                        // 2. الخلفية البيضاء مع الشكل
                        .background(color = Color.White, shape = RoundedCornerShape(20.dp))
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                           // color = Color(0xFFFF110D), //CircleShape, // لجعل الحواف دائرية تماماً مثل الصورة
                        .wrapContentSize(),

                            color = Color(0xFFFF110D),
                            fontSize = 20.sp,
                            fontFamily = FontFamily(Font(R.font.inter_semibold)),
                            style = TextStyle(
                                lineHeightStyle = LineHeightStyle(
                                    alignment = LineHeightStyle.Alignment.Center,
                                    trim = LineHeightStyle.Trim.Both // قص أي مسافات إضافية
                                ),
                                fontWeight = FontWeight.Bold,
                                platformStyle = PlatformTextStyle(includeFontPadding = false) // يضمن توسيطاً دقيقاً
                            )

                        )
                   // }
               // }
            Spacer(Modifier.height(40.dp))
            Text(
                text = "00:00",
                //modifier = Modifier
                color = Color.White,
                fontSize = 32.sp,
                letterSpacing = 1.sp,
                lineHeight = 11.sp,

                fontFamily = FontFamily(Font(R.font.sfpro_bold)),
                style = TextStyle(
                    lineHeightStyle = LineHeightStyle(
                        alignment = LineHeightStyle.Alignment.Center,
                        trim = LineHeightStyle.Trim.Both // قص أي مسافات إضافية
                    ),
                    fontWeight = FontWeight.Bold,

                    platformStyle = PlatformTextStyle(includeFontPadding = false) // يضمن توسيطاً دقيقاً
                )

            )
          Spacer(Modifier.height(60.dp))
            Row(

                verticalAlignment = Alignment.CenterVertically,

                ) {
                Text(
                    text = "Are you finish your Task ?",
                      Modifier.padding(start =50.dp),
                    color = Color.White,
                    fontSize = 12.sp,
                    letterSpacing = 1.sp,
                    lineHeight = 11.sp,

                    fontFamily = FontFamily(Font(R.font.sfpro_regular)),
                    style = TextStyle(

                        lineHeightStyle = LineHeightStyle(
                            alignment = LineHeightStyle.Alignment.Center,
                            trim = LineHeightStyle.Trim.Both // قص أي مسافات إضافية
                        ),
                        fontWeight = FontWeight.Bold,

                        platformStyle = PlatformTextStyle(includeFontPadding = false) // يضمن توسيطاً دقيقاً
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

                        modifier = Modifier.width(11.dp).height(20.dp)

                    )
                }

            }//endRowAre
          Spacer(Modifier.height(10.dp))

            // 1. تعريف الألوان مع الشفافية (0.2 alpha تعادل 20% opacity)
            val startColor = Color(0xFF6347A4).copy(alpha = 0.2f)
            val endColor = Color(0xFF000000).copy(alpha = 0.2f)

// 2. إنشاء التدرج (90 درجة تعادل تدرج أفقي من اليسار لليمين)
            val gradientBrush = Brush.linearGradient(
                colors = listOf(startColor, endColor),
                start = Offset(0f, 0f),
                end = Offset(Float.POSITIVE_INFINITY, 0f) // يمتد لنهاية العرض أفقياً
            )
            Card(
                modifier = Modifier
                    .background(basePurple)
                    .fillMaxWidth(0.8f) // تأخذ 90% من عرض أي شاشة
                    .wrapContentHeight()

                    .background(brush = gradientBrush, shape = RoundedCornerShape(9.dp))
                    // ملاحظة: الـ top و left في فيجما يتم ترجمتهما عادةً كـ Padding أو كجزء من ترتيب العناصر في الـ Column/Row
                    //.padding( start = 2.dp,top =5.dp),
                .padding(bottom = 24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent // إزالة اللون الافتراضي نهائياً
                ),
              /*  border = BorderStroke(
                    width = 0.5.dp, // 1px / 2
                    color = Color(0xFFAD8AFF).copy(alpha = 0.18f) // تحويل الشفافية 2E إلى حوالي 0.18f
                )*/
               // border = BorderStroke(0.5.dp, Color(0xFF6347A4)),//.copy(alpha = 0.5f)) // 1px / 2 مع شفافية بسيطة
            ) {
                Column(
                    modifier = Modifier
                        // .wrapContentHeight()
                        .padding(top = 5.dp), // مسافة داخلية لتنظيم العناصر
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    // هنا تضعين صفوف الـ Checkbox (Finish, Not Finish...)

                    RowCard("Finish",R.drawable.img_finish)
                    RowCard("Finish-(Need extra time)",R.drawable.img_finish_need_extra_time)
                    RowCard("Not Finish",R.drawable.img_finish)
                    RowCard("Not Finish-(Need extra time)",R.drawable.img_not_finish)


                   /* Checkbox(

                        checked = isChecked,
                        onCheckedChange = { isChecked = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color.White,       // اللون عند الاختيار
                            uncheckedColor = Color.LightGray  // اللون عند عدم الاختيار
                        )
                    )*/

                }//////end colum card
            }
            }//end colum
        }*/

/*
@Composable
fun ScreenHeader() {
    Row(

        Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .statusBarsPadding(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start

    ) {

        IconButton(
            onClick = { /* العودة للخلف */ },

            modifier = Modifier.size(48.dp) // مساحة النقر القانونية
        ) {

            Icon(
                painter = painterResource(id = R.drawable.back_arrow),
                contentDescription = null,
                tint = Color.White,

                modifier = Modifier.size(22.dp)

            )
        }

        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Reading seassruio",
            color = Color.White,
            fontSize = 20.sp,
            modifier = Modifier.weight(1f),

            style = TextStyle(
                lineHeightStyle = LineHeightStyle(
                    alignment = LineHeightStyle.Alignment.Center,
                    trim = LineHeightStyle.Trim.Both // قص أي مسافات إضافية
                ),
                fontWeight = Black,// FontWeight(860),
//                fontFamily = FontFamily(Font(R.font.sfpro_bold)),
//
//                )
//
//
//        )
//    }
//}*/

@Composable
fun TodayOverviewCard() {

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
                    text = "8",
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.sfpro_semibold)),
                    color = Color.White
                )
                Text(
                    text = "Tasks Done",
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
                    text = "2h 84m",
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
fun TaskRow(titleTask: String = "",displayTime:String = "") {


    Text(

        text = titleTask,
        color = Color.White,


        style = TextStyle(

            fontFamily = FontFamily(Font(R.font.sfpro_semibold)), // استبدليها باسم ملف الخط لديكِ
            fontWeight = FontWeight(590), // أو FontWeight.SemiBold
            fontSize = 14.sp,
            lineHeight = 11.sp, // ملاحظة هامة بالأسفل حول هذه القيمة
            letterSpacing = 0.sp,

            //textAlign = TextAlign.Center
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

                //textAlign = TextAlign.Center
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
fun TaskRoute(
    viewModel: TaskViewModel = viewModel()
) {

    val state by viewModel.uiState.collectAsState()


    DailyOverviewScreen (state)

}


@Composable
fun ScoreTaskTimer(   state: TimerState,
                      onIntent: (TimerIntent) -> Unit,
                      // الدالة المسؤولة عن النقل
                      /* initialValue: Float = 0.1f,  totalTime: Long =60000L*/) {


  /*  var value by remember {
        mutableStateOf(initialValue)
    }

    // create variable for current time
    var currentTime by remember {
        mutableStateOf(totalTime)
    }
    // create variable for isTimerRunning
    var isTimerRunning by remember {
        mutableStateOf(true)
    }*/


   /* LaunchedEffect(key1 = currentTime, key2 = isTimerRunning) {
        if(currentTime > 0 && isTimerRunning) {
            delay(100L)
            currentTime -= 100L
            value = currentTime / totalTime.toFloat()
        }
    }*/


    /////////////////
    Box(modifier = Modifier.fillMaxSize().background(basePurple)) {}
    Box(
        modifier = Modifier

            .fillMaxSize()
            .background(basePurple)
            .drawWithCache {
                // الأبعاد الأصلية التي أعطيتِني إياها (Frame Size)
                val designWidth = 720f
                val designHeight = 1600f

                // 1. حساب الأبعاد الحقيقية بناءً على شاشة المستخدم الحالية
                val glowWidth = size.width * (938f / designWidth)
                val glowHeight = size.height * (1078f / designHeight)



                // 2. حساب الإزاحة (Offsets) بدقة
                val offsetX = size.width * (177f / designWidth)
                val offsetY = size.height * (509f / designHeight)

                onDrawBehind {
                    // رسم المستطيل الذي يحمل التوهج
                    drawOval(
                        brush = Brush.radialGradient(
                            0.0f to color1,
                            0.38f to color2,
                            // 0.85f to Color(0xFF6943AC).copy(alpha = 0.24f), // هنا نضع الـ 24% الخاصة بفيجما
                            1.0f to Color.Transparent,

                            center = Offset(
                                // نسلتها اد ايه
                                x =  offsetX+(glowWidth * 0.6186f),
                                y = offsetY+ (glowHeight * 0.3892f)
                            ),
                            // مدي اتشارها
                            radius = glowWidth * 0.5772f


                        ),
                        // هبدا رسم منين
                        topLeft = Offset(offsetX, offsetY),
                        // حجمها اد ايه
                        size = Size(glowWidth, glowHeight),
                        // تطبيق الشفافية الكلية (0.4) ووضع الدمج
                        //   alpha = 0.2f,
                        blendMode = BlendMode.Overlay
                    )
                }

            }.blur(
                radius = 81.dp,
                edgeTreatment = BlurredEdgeTreatment.Unbounded
            )
    )
    Column(modifier = Modifier.fillMaxSize(),horizontalAlignment = Alignment.CenterHorizontally)

    {

        Spacer(Modifier.height(50.dp))

        ScreenHeader("ReadingSeationnnn", showEditButton = true)

        Spacer(modifier = Modifier.height(110.dp))

        // progress من 1.0 (بداية) إلى 0.0 (نهاية)
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(146.dp)
            //.fillMaxSize()
            // .background(Color(0xFF4A2997))
        ) {

            Canvas(modifier = Modifier.fillMaxSize()) {
                val strokeWidth = 8.dp.toPx() // استنتاج السمك بالنظر

                // 1. الدائرة البيضاء (الخلفية الشفافة 20%)
                drawCircle(
                    Color(0xFF7F69B3).copy(alpha = 0.5f),
                   // color = Color(0xFFE4D8FF).copy(alpha = 0.2f),
                    // brush = Brush.linearGradient(colorStops = scoreTaskTimerStops),

                    style = Stroke(width = strokeWidth)
                )

                // 2. القوس الأسود (التقدم)
                drawArc(
                    // brush = Brush.linearGradient(colorStops = scoreTaskTimerStops),
                    color =  Color(0xFF200C4E),//Color.Black,
                    startAngle = -90f,
                    sweepAngle = state.value * 360f,//360f * progress,
                    useCenter = false,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            }
// 1. حساب الدقائق المتبقية
           // val minutes = (currentTime / 1000L) / 60

// 2. حساب الثواني المتبقية (باقي القسمة على 60 لتبدأ من 59 وتنازلياً)
          //  val seconds = (currentTime / 1000L) % 60

// 3. تنسيق النص ليظهر دائماً بخانتين (مثلاً الـ 5 ثواني تظهر 05 وليس 5)
          //  val formattedTime = String.format("%02d:%02d", minutes, seconds)
            // 3. نص الوقت في المنتصف
            Text(
                text = state.formattedTime,//(currentTime / 1000L).toString(), //"45:00",
                style = TextStyle(
                    fontWeight = Black,
                    lineHeight = 11.sp,
                    letterSpacing = 1.sp,// FontWeight(860),
                    fontFamily = FontFamily(Font(R.font.sfpro_bold)),

                    ),
                color = Color.White, fontSize = 32.sp
            )
        }

        Spacer(modifier = Modifier.height(80.dp))
        Row(
            Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center
        ) {

            IconButton(
                onClick = {
                    if (state.isRunning) {
                        onIntent(TimerIntent.PauseTimer)
                    }else{
                        onIntent(TimerIntent.StartTimer)

                    }
                },
                modifier = Modifier.size(41.dp)
            ) {
                Icon(
                    painter = painterResource(
                        id = if (state.isRunning) R.drawable.pause_icon else R.drawable.icon_play
                    ),
                   // painter = painterResource(id = R.drawable.icon_play),
                    contentDescription = "Start Timer",
                    tint = Color.Unspecified, // أو أي لون يتماشى مع الثيم الخاص بكِ
                    modifier = Modifier.fillMaxSize()
                )
            }

           /* Icon (
                painter = painterResource(id = R.drawable.icon_play), // اسم الملف الذي أنشأتِه
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(41.dp)//.padding(top = 2.dp, start = 2.dp) // حجم ثابت كما اتفقنا
            )*/
            Spacer(modifier = Modifier.width(80.dp))


            IconButton(
                onClick = {
                    onIntent(TimerIntent.ResetTimer)
                },
                modifier = Modifier.size(41.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.retry_icon),
                    contentDescription = "Reset Timer",
                    tint = Color.Unspecified, // أو أي لون يتماشى مع الثيم الخاص بكِ
                    modifier = Modifier.fillMaxSize()
                )
            }

         /*   Icon (

                painter = painterResource(id = R.drawable.retry_icon), // اسم الملف الذي أنشأتِه
                contentDescription = null,
                modifier = Modifier.size(41.dp)//.padding(top = 2.dp, start = 2.dp) // حجم ثابت كما اتفقنا
            )*/

        }
    }

}









@Composable
fun DailyOverviewScreen(  state: TaskUiState) {
    BgScreen()
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    )

    {

        Spacer(Modifier.height(50.dp))

        ScreenHeader("MyProductivty",R.drawable.back_arrow)

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

            TodayOverviewCard()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp, top = 46.dp), // أو fillMaxSize حسب حاجتك
                horizontalArrangement = Arrangement.End // لتوسيط العناصر أفقياً
            ) //) {
            {
                TimeMuscleProgressIndicator(0.90f)

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
            contentPadding = PaddingValues(vertical = 8.dp), // مسافة في أعلى وأسفل القائمة بالكامل
            verticalArrangement = Arrangement.spacedBy(12.dp) // 💡 تصنع مسافة تلقائية بمقدار 12dp بين كل كارد والآخر
        ) {

            items(state.tasksList) { task ->

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



        }
    }
}











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

@Composable
fun DashboardScreen() {
    BgScreen()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    )

    {

        Spacer(Modifier.height(50.dp))
        ScreenHeader("MySccore",R.drawable.back_arrow)
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
                    value = "12",
                    title = "Total Tasks Completed",
                    iconInside = R.drawable.icon_inside_circle_left,
                    cardGradient = cardGradient,
                    weight = 1f
                )
            }

            Box(modifier = Modifier.weight(1f)) {
                StateCart(
                    "5h 49m",
                    "Total Foucs Time",
                    R.drawable.icon_inside_right,
                    cardGradient = cardGradient,
                    weight = 1f
                )
            }

        }

        Spacer(Modifier.height(50.dp))
        CustomTabRow()
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

                // .padding(top=16.dp)
                .clip(RoundedCornerShape(17.dp)) // border-radius: 17px
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
            SimpleVicoChart()
        }

    }
}
/*
@Composable
fun OnBoardingBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {


    Box(

        modifier = Modifier

            .fillMaxSize()
            .background(basePurple)
            .drawWithCache {
                // الأبعاد الأصلية التي أعطيتِني إياها (Frame Size)
                val designWidth = 720f
                val designHeight = 1600f

                // 1. حساب الأبعاد الحقيقية بناءً على شاشة المستخدم الحالية
                val glowWidth = size.width * (630f / designWidth)
                val glowHeight = size.height * (630f / designHeight)


                // 2. حساب الإزاحة (Offsets) بدقة
                val offsetX = size.width * (217f / designWidth)
                val offsetY = size.height * (-384f / designHeight)

                onDrawBehind {
                    // رسم المستطيل الذي يحمل التوهج
                    drawOval(
                        brush = Brush.radialGradient(
                            0.0f to color1,
                            0.38f to color2,
                            // 0.85f to Color(0xFF6943AC).copy(alpha = 0.24f), // هنا نضع الـ 24% الخاصة بفيجما
                            1.0f to Color.Transparent,

                            center = Offset(
                                // نسلتها اد ايه
                                x = offsetX + (glowWidth * 0.6186f),
                                y = offsetY + (glowHeight * 0.3892f)
                            ),
                            // مدي اتشارها
                            radius = glowWidth * 0.5772f


                        ),
                        // هبدا رسم منين
                        topLeft = Offset(offsetX, offsetY),
                        // حجمها اد ايه
                        size = Size(glowWidth, glowHeight),
                        // تطبيق الشفافية الكلية (0.4) ووضع الدمج
                        //   alpha = 0.2f,
                        blendMode = BlendMode.Overlay
                    )
                }

            }
            .blur(
                radius = 81.dp,
                edgeTreatment = BlurredEdgeTreatment.Unbounded
            )


            .drawWithCache {
                // الأبعاد الأصلية التي أعطيتِني إياها (Frame Size)
                val designWidth = 720f
                val designHeight = 1600f

                // 1. حساب الأبعاد الحقيقية بناءً على شاشة المستخدم الحالية
                val glowWidth = size.width * (630f / designWidth)
                val glowHeight = size.height * (630f / designHeight)


                // 2. حساب الإزاحة (Offsets) بدقة
                val offsetX = size.width * (14f / designWidth)
                val offsetY = size.height * (326f / designHeight)

                onDrawBehind {
                    // رسم المستطيل الذي يحمل التوهج
                    drawCircle(
                        brush = Brush.radialGradient(
                            0.0f to color1,
                            0.38f to color2,
                            1.0f to Color.Transparent,

                            center = Offset(
                                // نسلتها اد ايه
                                x = offsetX + (glowWidth * 0.6186f),
                                y = offsetY + (glowHeight * 0.3892f)
                            ),
                            // مدي اتشارها
                            radius = glowWidth * 0.4772f


                        ),
                        center = Offset(
                            // نسلتها اد ايه
                            x = offsetX + (glowWidth * 0.6186f),
                            y = offsetY + (glowHeight * 0.3892f)
                        ),
                        // مدي اتشارها
                        radius = glowWidth * 0.4772f,
                        blendMode = BlendMode.Overlay
                    )
                }

            }


            .drawWithCache {
                // الأبعاد الأصلية التي أعطيتِني إياها (Frame Size)
                val designWidth = 720f
                val designHeight = 1600f

                // 1. حساب الأبعاد الحقيقية بناءً على شاشة المستخدم الحالية
                val glowWidth = size.width * (872f / designWidth)
                val glowHeight = size.height * (792f / designHeight)


                // 2. حساب الإزاحة (Offsets) بدقة
                val offsetX = size.width * (432f / designWidth)
                val offsetY = size.height * (832f / designHeight)

                onDrawBehind {
                    // رسم المستطيل الذي يحمل التوهج
                    drawCircle(
                        brush = Brush.radialGradient(
                            0.0f to color1,
                            0.38f to color2,
                            // 0.85f to Color(0xFF6943AC).copy(alpha = 0.24f), // هنا نضع الـ 24% الخاصة بفيجما
                            1.0f to Color.Transparent,

                            center = Offset(
                                // نسلتها اد ايه
                                x = offsetX + (glowWidth * 0.6186f),
                                y = offsetY + (glowHeight * 0.3892f)
                            ),
                            // مدي اتشارها
                            radius = glowWidth * 0.5772f


                        ),
                        center = Offset(
                            // نسلتها اد ايه
                            x = offsetX + (glowWidth * 0.6186f),
                            y = offsetY + (glowHeight * 0.3892f)
                        ),
                        // مدي اتشارها
                        radius = glowWidth * 0.5772f,
                        blendMode = BlendMode.Overlay
                    )
                }

            }
            .blur(
                radius = 81.dp,
                edgeTreatment = BlurredEdgeTreatment.Unbounded
            ),

        content = content
    )


}*/

@Composable
fun OnBoardingBackground(content: @Composable BoxScope.() -> Unit) {
    val onboardingGlows = remember {
        listOf(
            GlowConfig(widthRatio = 630f, heightRatio = 630f, offsetXRatio = 217f, offsetYRatio = -384f ,shapeType = GlowShape.OVAL),
            GlowConfig(widthRatio = 630f, heightRatio = 630f, offsetXRatio = 14f, offsetYRatio = 326f, shapeType = GlowShape.CIRCLE,radiusPercent = 0.4772f),
            GlowConfig(widthRatio = 872f, heightRatio = 792f, offsetXRatio = 432f, offsetYRatio = 832f, shapeType = GlowShape.CIRCLE, radiusPercent = 0.47f)
        )
    }

    AppGlowBackground(glowList = onboardingGlows, content = content)
}


@Composable
fun OnBoardingContent(onFinished: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {

        // {
        // Spacer(Modifier.height(902.dp))
        Image(
            painter = painterResource(id = R.drawable.img_onboarding),
            contentDescription = null,
            modifier = Modifier
                .padding(start = 35.dp, top = 102.dp)
                // .align(Alignment.Center) // أو تحديد مكانها بالـ Offset
                .size(300.dp), // مثال للحجم
            contentScale = ContentScale.Fit
        )

        Column(
            Modifier
                .padding(top = 515.dp, start = 24.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(6.dp),

            ) {
            Text(
                text = "Score Task",
                color = Color.White,
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.sfpro_light, FontWeight(274))),
                    fontSize = 16.sp, // 32px / 2 = 16
                    lineHeight = 14.sp,
                    fontWeight = FontWeight(274),
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false // ضروري جداً لتحقيق الـ leading-trim: NONE
                    ),
                    lineHeightStyle = LineHeightStyle(
                        alignment = LineHeightStyle.Alignment.Center,
                        trim = LineHeightStyle.Trim.None
                    )
                ),
                modifier = Modifier.wrapContentSize(Alignment.Center)
            )
            val annotatedText = buildAnnotatedString {
                // 1. الجزء الأول: Smarter Alarms\nBetter
                withStyle(
                    style = SpanStyle(
                        fontFamily = FontFamily(Font(R.font.sfpro_semibold, FontWeight(274))),
                        fontSize = 24.sp, // (32px / 2)

                    )
                ) {
                    append("Smarter Alarms\nBetter ")
                }

                // 2. الجزء المختلف: Habit
                withStyle(
                    style = SpanStyle(
                        fontFamily = FontFamily(Font(R.font.sfpro_light)), // ستايل Light
                        fontSize = 24.sp, // (48px / 2)

                        color = Color(0xFFC9C9C9)
                    )
                ) {
                    append("Habits")
                }
            }


            Text(
                text = annotatedText,
                color = Color.White,
                style = TextStyle(

                    lineHeight = 26.sp,
                    platformStyle = PlatformTextStyle(includeFontPadding = false),
                    lineHeightStyle = LineHeightStyle(
                        alignment = LineHeightStyle.Alignment.Center,
                        trim = LineHeightStyle.Trim.None
                    )
                )
            )


            Text(
                text = "Create alarms, manage your \n schedule,and never miss an important\n moment.",
                Modifier.padding(top = 8.dp),
                color = Color(0xFFC9C9C9),

                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.sfpro_regular, FontWeight(274))),
                    fontSize = 8.sp, // (32px / 2)
                    fontWeight = FontWeight(400),
                    lineHeight = 8.sp,
                    platformStyle = PlatformTextStyle(includeFontPadding = false),
                    lineHeightStyle = LineHeightStyle(
                        alignment = LineHeightStyle.Alignment.Center,
                        trim = LineHeightStyle.Trim.None
                    )
                )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 240.dp), // مسافات الأمان عن حافة الشاشة
                verticalAlignment = Alignment.CenterVertically
                //   horizontalArrangement = Arrangement.End, // وضع العناصر في نهاية الشاشة (Next)
                //verticalAlignment = Alignment.CenterVertically // توسيط النص والأيقونة رأسياً
            ) {
                TextButton(

                    onClick = { onFinished() },
                    Modifier.wrapContentSize()
                )
                {

                    Text(
                        text = "Next",
                        color = Color.White,
                        style = TextStyle(
                            fontFamily = FontFamily(
                                Font(
                                    R.font.inter_semibold,
                                    FontWeight.SemiBold
                                )
                            ),
                            fontSize = 20.sp, // 40px / 2
                            lineHeight = 7.sp, // 15px / 2 (انتبهي قد يحتاج زيادة لتجنب القص)
                            platformStyle = PlatformTextStyle(includeFontPadding = false),
                            lineHeightStyle = LineHeightStyle(
                                alignment = LineHeightStyle.Alignment.Center,
                                trim = LineHeightStyle.Trim.None
                            ),
                            // textAlign = TextAlign.Center
                        )
                    )
                }


                Row(
                    modifier = Modifier
                        .size(48.dp)
                        .padding(start = 8.dp) // المسافة بين كلمة Next والأسهم
                        .clickable { /* انتقال */ },
                    verticalAlignment = Alignment.CenterVertically


                ) {

                    Icon(
                        painter = painterResource(id = R.drawable.next_icon),
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = Color.White
                    )

                    Icon(
                        painter = painterResource(id = R.drawable.next_icon),
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = Color.White
                    )
                }
            }


        }
    }

}

@Composable
fun OnBoardingScreen(onFinished: () -> Unit) {

    OnBoardingBackground() {
        OnBoardingContent(onFinished)
    }
}


@Composable
fun Float.screenFontSize(): TextUnit {
    val configuration = LocalConfiguration.current
    // نعتمد على عرض الشاشة كمرجع للتناسب
    val screenWidth = configuration.screenWidthDp

    // الحساب: (حجم الخط في التصميم / عرض التصميم الكلي) * عرض الشاشة الحالي
    val designWidth = 720f
    val ratio = this / designWidth
    val calculatedSize = screenWidth * ratio

    return calculatedSize.sp
}

@Composable
fun Float.screenPercentageY(): Dp {
    // الحصول على عرض الشاشة الحالي للجهاز الذي يعمل عليه التطبيق
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    // ضرب النسبة (مثلاً 0.23) في عرض الشاشة الحقيقي
    return screenHeight * this
}


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

){
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
                    onValueChange = { newValue->
                                     // text = newValue
                        onIntent(
                            TaskIntent.TitleChanged(newValue)
                        )},
                    label = { Text(text = "Date") },
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
                    TextButton(onClick = { /* Dismiss */ }) {
                        Text("Cancel", color = Color.White)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // زر التأكيد (واضح وجاذب للانتباه)
                    TextButton(onClick = {


                        Toast.makeText(context, "The task name has been successfully saved.", Toast.LENGTH_SHORT).show()



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
){
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
                        onValueChange = { newValue->
                           // hours = newValue
                            onIntent (TaskIntent.HoursChanged(newValue))
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
                        onValueChange = { newValue->
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
                    TextButton(onClick = { /* Dismiss */ }) {
                        Text("Cancel", color = Color.White)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(onClick = { /* OK */

                      //  Log.i("testButton::First",inputHolder.title)
                       if (state.title.isNotEmpty()) {
                            onIntent (TaskIntent.SaveTask)
                               onNavigateToTimer ()
                         //  ScoreRoute(viewModel)
                           Toast.makeText(context, "The task has been successfully saved.", Toast.LENGTH_SHORT).show()


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
fun ScoreRoute(
    viewModel: TimerViewModel,
    navController: NavController
) {
   // val state by viewModel.state.collectAsState()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = state.currentTime) {
        // إذا كان التايمر وصل لصفر (أو أقل من أو يساوي صفر للأمان البرمجي)
        if (state.currentTime <= 0.0) {
            navController.navigate(Screen.TaskCompletion.route) {
                // بنمسح شاشة التايمر من الـ BackStack عشان لو المستخدم داس زرار الرجوع ميرجعش للتايمر الميت
                popUpTo(Screen.TimerTask.route) { inclusive = true }
            }
        }
    }
    ScoreTaskTimer (
        state = state,
        onIntent = viewModel::onIntent
    )


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


    @Composable
    fun SplashContent(
        modifier: Modifier = Modifier
    ) {
        Box(
            modifier = modifier.fillMaxSize()
        ) {
            // 1. صورة البومة الفخمة (الـ Logo الخاص بالتطبيق)
            Image(
                painter = painterResource(id = R.drawable.img_splash),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(300.dp),
                contentScale = ContentScale.Fit
            )

            // 2. عمود النصوص الترحيبية ونصوص اسم التطبيق
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy((-15).dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // الكلمة الأولى: Score
                Text(
                    text = "Score",
                    modifier = Modifier.offset(y = (1120f / 1600f).screenPercentageY()),
                    style = TextStyle(
                        fontSize = 96f.screenFontSize(),
                        fontFamily = FontFamily(Font(R.font.sfpro_bold)),
                        color = Color.White,
                        letterSpacing = (-2).sp
                    )
                )

                // الكلمة الثانية: Task
                Text(
                    text = "Task",
                    modifier = Modifier.offset(y = (1120f / 1600f).screenPercentageY()),
                    style = TextStyle(
                        fontSize = 96f.screenFontSize(),
                        fontFamily = FontFamily(Font(R.font.sfpro_bold)),
                        color = Color(0xFFB99BFF)
                    )
                )

                Spacer(modifier = Modifier.height(55.dp))

                // النص الفرعي الصغير السفلي
                Text(
                    text = "Stay Focused, Stay Productive",
                    modifier = Modifier.offset(y = (1120f / 1600f).screenPercentageY()),
                    style = TextStyle(
                        fontSize = 20f.screenFontSize(),
                        fontFamily = FontFamily(Font(R.font.sfpro_regular)),
                        color = Color(0xFFC3C3C3),
                        letterSpacing = 1.sp
                    )
                )
            }
        }
    }
    /*
@Composable
fun SplashBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val color1 = Color(0xFFEF88ED)
    val color2 = Color(0xFFA099FF)
    val color3 = Color(0xFF8350DB)
    val basePurple = Color(0xFF4A2997)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(basePurple)
            // 💡 التوهج العلوي الأول
            .drawWithCache {
                val designWidth = 720f
                val designHeight = 1600f

                val glowWidth = size.width * (872f / designWidth)
                val glowHeight = size.height * (792f / designHeight)

                val offsetX = size.width * (-202f / designWidth)
                val offsetY = size.height * (-604f / designHeight)

                onDrawBehind {
                    drawOval(
                        brush = Brush.radialGradient(
                            0.0f to color1,
                            0.375f to color2,
                            1.0f to color3,
                            center = Offset(
                                x = offsetX + (glowWidth * 0.6186f),
                                y = offsetY + (glowHeight * 0.3892f)
                            ),
                            radius = glowWidth * 0.5772f
                        ),
                        topLeft = Offset(offsetX, offsetY),
                        size = Size(glowWidth, glowHeight),
                        alpha = 0.4f,
                        blendMode = BlendMode.Overlay
                    )
                }
            }
            // 💡 التوهج السفلي الثاني
            .drawWithCache {
                val designWidth = 720f
                val designHeight = 1600f
                val newOffestY = -110

                val glowSizePx = size.width * (630f / designWidth)
                val offsetX = 0f
                val offsetY = size.height * (661f / designHeight)

                onDrawBehind {
                    drawRect(
                        brush = Brush.radialGradient(
                            0.0f to Color(0xFFEBEBEB),
                            0.38f to Color(0xFFE3EAFF),
                            1.0f to Color.Transparent,
                            center = Offset(
                                x = glowSizePx * 0.6f,
                                y = offsetY + (glowSizePx * 0.30f)
                            ),
                            radius = glowSizePx / 2
                        ),
                        topLeft = Offset(offsetX, offsetY + newOffestY),
                        size = Size(size.width, glowSizePx),
                        blendMode = BlendMode.Overlay
                    )
                }
            },
        content = content // هنا سيتم رسم البومة والنصوص فوق التوهجات بسلام
    )
}*/

    @Composable
    fun SplashBackground(content: @Composable BoxScope.() -> Unit) {
        val splashGlows = remember {
            listOf(
                // التوهج العلوي (يستخدم اللون الثالث الافتراضي)
                GlowConfig(
                    widthRatio = 872f,
                    heightRatio = 792f,
                    offsetXRatio = -202f,
                    offsetYRatio = -604f,
                    shapeType = GlowShape.OVAL,
                    customColors = { c1, c2 ->
                        listOf(
                            0.0f to color1,
                            0.375f to color2,
                            1.0f to Color.Transparent
                        )
                    },
                    alpha = 0.4f
                ),
                // التوهج السفلي المستطيل الأبيض
                GlowConfig(
                    widthRatio = 630f,
                    heightRatio = 630f,
                    offsetXRatio = 0f,
                    offsetYRatio = 551f,
                    shapeType = GlowShape.CIRCLE,
                    customColors = { _, _ ->
                        listOf(
                            0.0f to Color(0xFFEBEBEB),
                            0.38f to color2,
                            1.0f to Color.Transparent
                        )
                    },
                    radiusPercent = 0.44f

                )
            )
        }

        AppGlowBackground(glowList = splashGlows, content = content)
    }

    @Composable
    fun SplashScreen(onTimeout: () -> Unit) {
        // 1. إدارة منطق التوقيت والانتقال
        LaunchedEffect(Unit) {
            delay(1000)
            onTimeout()

        }

        // 2. تجميع الشاشة: نضع الخلفية وبداخلها المحتوى
        SplashBackground {
            SplashContent()
        }
    }


    @Composable
    fun Greeting(name: String, modifier: Modifier = Modifier) {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
    }



/*
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ScoreTaskTheme {
        Greeting("Android")
    }
}*/