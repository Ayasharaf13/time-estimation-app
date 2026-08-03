package com.example.scoretask


import androidx.navigation.NavType
import androidx.navigation.navArgument
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.scoretask.ui.theme.ScoreTaskTheme
import db.ConcreteLocalSource
import kotlinx.coroutines.delay
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.draw.shadow
import com.example.scoretask.dashboard.view.DashboardScreenRoute
import com.example.scoretask.dashboard.viewmodel.StatsViewModel
import com.example.scoretask.dashboard.viewmodel.StatsViewModelFactory
import com.example.scoretask.feedback.view.FeedbackScreenExtraTime
import com.example.scoretask.feedback.view.FeedbackScreenRoute
import com.example.scoretask.feedback.viewmodel.FeedBackFactory
import com.example.scoretask.feedback.viewmodel.FeedBackViewModel
import com.example.scoretask.repository.TaskRepository
import com.example.scoretask.repository.TaskRepositoryImpl
import com.example.scoretask.taskcompletion.view.TaskCompletionRoute
import com.example.scoretask.taskcompletion.viewmodel.TaskCompletionFactory
import com.example.scoretask.taskcompletion.viewmodel.TaskCompletionViewModel
import com.example.scoretask.taskmanagement.contract.TaskIntent
import com.example.scoretask.taskmanagement.view.HomeRoute
import com.example.scoretask.taskmanagement.view.TaskRoute
import com.example.scoretask.taskmanagement.viewmodel.TaskOverviewViewModel
import com.example.scoretask.taskmanagement.viewmodel.TaskOverviewViewModelFactory
import com.example.scoretask.taskmanagement.viewmodel.TaskViewModel
import com.example.scoretask.taskmanagement.viewmodel.TaskViewModelFactory
import com.example.scoretask.timer.view.ScoreRoute
import com.example.scoretask.timer.viewmodel.TimerViewModel
import com.example.scoretask.timer.viewmodel.TimerViewModelFactory
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val localSource = ConcreteLocalSource.getInstance(applicationContext)
        val repository = TaskRepositoryImpl.getInstance(localSource)



        setContent {

            window.statusBarColor = android.graphics.Color.parseColor("#331E66")//.TRANSPARENT


            ScoreTaskTheme {

                // 1. تعريف الـ NavController الرئيسي للتطبيق كله
                val rootNavController = rememberNavController()

                val sharedTaskViewModel: TaskViewModel = viewModel(
                    factory = TaskViewModelFactory(repository)
                )
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

                        BottomNav(rootNavController, repository, sharedTaskViewModel)
                    }


                    // 🚀 سجلنا شاشة التايمر هنا كشاشة كاملة في الخريطة الكبرى
                    composable(

                        route = "${Screen.TimerTask.route}?sessionId={sessionId}&extraTimeMs={extraTimeMs}&isExtraTime={isExtraTime}",
                        arguments = listOf(
                            navArgument("sessionId") {
                                type = NavType.LongType
                                defaultValue = 0L
                            },
                            navArgument("extraTimeMs") {
                                type = NavType.IntType
                                defaultValue = 0
                            },
                            navArgument("isExtraTime") {
                                type = NavType.BoolType
                                defaultValue = false
                            }
                        )
                    ) { backStackEntry ->

                        val sessionId = backStackEntry.arguments?.getLong("sessionId") ?: 0L
                        val extraTimeMs = backStackEntry.arguments?.getInt("extraTimeMs") ?: 0
                        val isExtraTime =
                            backStackEntry.arguments?.getBoolean("isExtraTime") ?: false


                        val timerViewModel: TimerViewModel = viewModel(
                            factory = TimerViewModelFactory(
                                repository, isExtraTime = isExtraTime,
                                extraTimeMs = extraTimeMs, sessionId = sessionId
                            )
                        )


                        // 3️⃣ تحويل القيمة لـ Int إذا كانت دالة ScoreRoute تتوقع Int
                        ScoreRoute(
                            viewModel = timerViewModel,
                            sharedTaskViewModel = sharedTaskViewModel,
                            navController = rootNavController,


                            )

                    }
                    composable(

                        route = "${Screen.FeedbackScreenExtraTime.route}/{sessionId}",
                        arguments = listOf(
                            navArgument("sessionId") { type = NavType.LongType }
                        )
                    ) { backStackEntry ->
                        val sessionId = backStackEntry.arguments?.getLong("sessionId") ?: 0L
                        val scope = rememberCoroutineScope()

                        FeedbackScreenExtraTime(
                            onConfirmFeedback = { wasEnough, gapMinutes ->
                                Log.i(
                                    "TEST_DEBUG",
                                    "1. Button Clicked with gapMinutes: $gapMinutes"
                                )
                                val selectGapMinutes = gapMinutes * 60 * 1000L
                                // 🎯 2. تحديث قاعدة البيانات مباشرة دون الحاجة لـ ViewModel جديد!

                                scope.launch {
                                    try {
                                        Log.i("TEST_DEBUG", "2. Starting DB Update...")
                                        withContext(NonCancellable) {
                                            repository.adjustActualDurationWithGap(
                                                sessionId = sessionId,
                                                additionalMs = selectGapMinutes,

                                                )
                                        }
                                        Log.i("ssid", sessionId.toString())
                                        // 🎯 3. العودة للشاشة الرئيسية بعد التحديث
                                        rootNavController.navigate(Screen.MainHome.route) {
                                            popUpTo(Screen.FeedbackScreenExtraTime.route) {
                                                inclusive = true
                                            }
                                        }

                                    } catch (e: Exception) {
                                        Log.e("ssid", "Error updating database: ${e.message}")
                                    }
                                }
                            }


                        )

                    }



                    composable(
                        // 💡 إضافة الـ arguments للمسار بنفس الترتيب
                        route = "${Screen.FeedBackScreen.route}/{expectTime}/{sessionId}/{status}/{extraTimeMinutes}",
                        arguments = listOf(
                            navArgument("expectTime") { type = NavType.LongType },
                            navArgument("sessionId") { type = NavType.LongType },
                            navArgument("status") {
                                type = NavType.StringType
                            }, // أو EnumType حسب تعريفك
                            navArgument("extraTimeMinutes") { type = NavType.IntType }


                        )
                    ) { backStackEntry ->
                        // 💡 استخراج القيم الممررة من الـ backStackEntry
                        val expectTime = backStackEntry.arguments?.getInt("expectTime") ?: 0
                        val sessionId = backStackEntry.arguments?.getLong("sessionId") ?: 0L
                        val statusName = backStackEntry.arguments?.getString("status") ?: ""
                        //   val selectedStatus = backStackEntry.arguments?.getString("selectedStatus") ?: ""
                        val extraTimeMinutes =
                            backStackEntry.arguments?.getInt("extraTimeMinutes") ?: 0

                        val feedBackViewModel: FeedBackViewModel = viewModel(
                            factory = FeedBackFactory(repository)
                        )


                        val (feedbackTitle, imageRes) = when (statusName) {
                            // 1. Finished and requested extra time (FINISH_EXTRA)
                            TaskResultStatus.FINISH_EXTRA.name -> {
                                "Great Job" to R.drawable.img_finish_need_extra_time// استبدلي اسم الصورة بصورتك المناسبة
                            }

                            // 2. Not finished and didn't request extra time (NOT_FINISH)
                            TaskResultStatus.NOT_FINISH.name -> {
                                "So Furious" to R.drawable.img_not_finish
                            }

                            // 3. Not finished and requested extra time (NOT_FINISH_EXTRA)
                            TaskResultStatus.NOT_FINISH_EXTRA.name -> {
                                "Go On!" to R.drawable.img_lazy // استبدلي اسم الصورة بصورتك المناسبة
                            }

                            TaskResultStatus.FINISH.name -> {
                                "Great Job!" to R.drawable.img_finish // استبدلي اسم الصورة بصورتك المناسبة
                            }

                            // 💡 حالة افتراضية fallback تحسباً لأي قيمة أخرى
                            else -> {
                                //
                                "Great Job" to R.drawable.img_finish
                            }
                        }
                        FeedbackScreenRoute(

                            title = feedbackTitle,
                            imageRes = imageRes,
                            sessionId = sessionId,
                            extraTimeMinutes = extraTimeMinutes,
                            viewModel = feedBackViewModel,
                            navController = rootNavController,
                            taskViewModel = sharedTaskViewModel

                        )


                    }




                    composable(
                        route = "${Screen.TaskCompletion.route}/{expectedTime}/{sessionId}",
                        arguments = listOf(
                            navArgument("expectedTime") { type = NavType.IntType },
                            navArgument("sessionId") { type = NavType.LongType }
                        )

                    ) { backStackEntry ->
                        // استخراج القيمة بأمان
                        val expectedTime = backStackEntry.arguments?.getInt("expectedTime") ?: 0
                        val sessionId = backStackEntry.arguments?.getLong("sessionId") ?: 0L
                        val taskCompletionViewModel: TaskCompletionViewModel = viewModel(
                            factory = TaskCompletionFactory(repository)
                        )
                        // باصي الرقم لشاشتكِ الجميلة
                        //  TaskCompletion(expectedTime)
                        TaskCompletionRoute(
                            expectedTime,
                            sessionId,
                            taskCompletionViewModel,
                            rootNavController,
                            sharedTaskViewModel
                        )

                    }


                }

            }
        }
    }


    @Composable
    fun BottomNav(
        rootNavController: NavController,
        repository: TaskRepository,
        sharedTaskViwModel: TaskViewModel
    ) {


        val sharedTaskOverViewViewModel: TaskOverviewViewModel = viewModel(
            factory = TaskOverviewViewModelFactory(repository)
        )


        val StateViewModel: StatsViewModel = viewModel(
            factory = StatsViewModelFactory(repository)
        )

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


                    HomeRoute(
                        viewModel = sharedTaskViwModel,
                        onNavigateToTimer = {

                            // هنا نأمر الموجه الأكبر بفتح شاشة التايمر المسجلة فوق
                            rootNavController.navigate(Screen.TimerTask.route)
                        }
                    )

                }
                composable(route = Screen.Task.route) {


                    TaskRoute(sharedTaskViwModel, sharedTaskOverViewViewModel, rootNavController)


                }
                composable(
                    route = Screen.Stats.route
                ) {
                    DashboardScreenRoute(StateViewModel, rootNavController)

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
        modifier = modifier
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
                    Log.i("size width: ", size.width.toString())
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
                                size = Size(
                                    size.width,
                                    glow.size.width
                                ), // استخدام عرض الشاشة الحقيقي للتصميم المستطيل
                                blendMode = BlendMode.Overlay
                            )
                        }
                    }
                }
            },
        content = content
    )
}

@Composable
fun BgScreen() {
    val bgGlows = remember {
        listOf(
            GlowConfig(
                widthRatio = 938f,
                heightRatio = 1078f,
                offsetXRatio = -287f,
                offsetYRatio = -378f,
                shapeType = GlowShape.CIRCLE,
                radiusPercent = 0.47f
            ),
            GlowConfig(
                widthRatio = 938f,
                heightRatio = 1078f,
                offsetXRatio = 177f,
                offsetYRatio = 509f,
                shapeType = GlowShape.CIRCLE,
                radiusPercent = 0.44f
            )
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


@Composable
fun ScreenHeader(
    title: String,
    // 1. تمرير الأيقونة اليسرى كـ Resource ID، وافتراضياً هي أيقونة الـ Back الخاص بكِ
    @DrawableRes navigationIcon: Int = R.drawable.icon_back,
    onNavigationClick: () -> Unit = {},
    // 2. زر التعديل نجعله اختياري (null بافتراض)
    showEditButton: Boolean = false,
    onEditClick: (TaskIntent) -> Unit = {}

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
                    onClick =
                        { onEditClick(TaskIntent.EditTask) },


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
            else -> error("Finish status should not trigger the bottom sheet question")
        }
    }

    fun getPsychologyOptions(): List<String> {
        return when (this) {
            FINISH_EXTRA -> emptyList()
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

            else -> emptyList()
        }
    }

    // دالة ترجع خيارات الوقت بالدقائق لكل حالة (ديناميكية)
    fun getDurationOptions(expectTime: Int): List<Int> {
        // val halfTime = (expectTime / 2).coerceAtLeast(5)
        return when (this) {

            // FINISH -> listOf(5, 10) // وقت بسيط للمراجعة
            FINISH_EXTRA -> {
                val op1 = (expectTime * 0.25).toInt()
                    .coerceAtLeast(5)
                    .coerceAtMost(expectTime)

                // 🎯 الخيار 2 (50% من الوقت الأصلي) - النصف
                val op2 = (expectTime * 0.50).toInt()
                    .coerceAtLeast(op1 + 5) // يضمن أن op2 أكبر من op1
                    .coerceAtMost(expectTime)

                // 🎯 الخيار 3 (75% من الوقت الأصلي) - أقصى حد مسموح به دون تجاوز expectTime
                val op3 = (expectTime * 0.75).toInt()
                    .coerceAtLeast(op2 + 5) // يضمن أن op3 أكبر من op2
                    .coerceAtMost(expectTime)

                // إرجاع القائمة بدون تكرار في حال كانت الأوقات صغيرة جداً (e.g. distinct)
                listOf(0, op1, op2, op3).distinct()
            }

            NOT_FINISH -> emptyList()// listOf(15, 30, 45, 60) // وقت كبير لجلسة تانية

            NOT_FINISH_EXTRA -> {
                val op1 = (expectTime * 0.20).toInt()
                    .coerceAtLeast(5)
                    .coerceAtMost(expectTime)

                // 🎯 الخيار 2: 35% من الوقت الأصلي (تمديد متوسط)
                val op2 = (expectTime * 0.35).toInt()
                    .coerceAtLeast(op1 + 5)
                    .coerceAtMost(expectTime)

                // 🎯 الخيار 3: 50% من الوقت الأصلي (أقصى تمديد مسموح لتجنب الإجهاد المعرفي)
                val op3 = (expectTime * 0.50).toInt()
                    .coerceAtLeast(op2 + 5)
                    .coerceAtMost(expectTime)

                listOf(0, op1, op2, op3).distinct()
            }

            else -> emptyList()
        }
    }
}


@Composable
fun GlowBackground(
    modifier: Modifier = Modifier,
    baseColor: Color,
    glowColor1: Color,
    glowColor2: Color,
    designWidth: Float = 720f,
    designHeight: Float = 1600f,
    content: @Composable BoxScope.() -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(baseColor)
            .drawWithCache {
                // --- 1. حسابات التوهج الأول (Top Left Glow) ---
                val glow1Width = size.width * (938f / designWidth)
                val glow1Height = size.height * (1078f / designHeight)
                val offsetX1 = size.width * (-287f / designWidth)
                val offsetY1 = size.height * (-378f / designHeight)

                val center1 = Offset(
                    x = offsetX1 + (glow1Width * 0.6186f),
                    y = offsetY1 + (glow1Height * 0.3892f)
                )
                val radius1 = glow1Width * 0.4772f

                val brush1 = Brush.radialGradient(
                    0.0f to glowColor1,
                    0.38f to glowColor2,
                    1.0f to Color.Transparent,
                    center = center1,
                    radius = radius1
                )

                // --- 2. حسابات التوهج الثاني (Bottom Right / Middle Glow) ---
                val glow2Width = size.width * (938f / designWidth)
                val glow2Height = size.height * (1078f / designHeight)
                val offsetX2 = size.width * (-157f / designWidth)
                val offsetY2 = size.height * (392f / designHeight)

                val center2 = Offset(
                    x = offsetX2 + (glow2Width * 0.6186f),
                    y = offsetY2 + (glow2Height * 0.3892f)
                )
                val radius2 = glow2Width * 0.5772f

                val brush2 = Brush.radialGradient(
                    0.0f to glowColor1,
                    0.38f to glowColor2,
                    1.0f to Color.Transparent,
                    center = center2,
                    radius = radius2
                )


                onDrawBehind {
                    // رسم Glow 1
                    drawCircle(
                        brush = brush1,
                        center = center1,
                        radius = radius1,
                        blendMode = BlendMode.Overlay
                    )

                    // Glow
                    drawCircle(
                        brush = brush2,
                        center = center2,
                        radius = radius2,
                        blendMode = BlendMode.Overlay
                    )
                }
            }
    ) {

    }
}


@Composable
fun TimeoutBadge(
    text: String = "Time out",
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier
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
}


@Composable
fun ZeroTimerText(
    timeText: String = "00:00",
    modifier: Modifier = Modifier
) {
    Text(
        text = timeText,
        modifier = modifier,
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
}


@Composable
fun EditTaskTitleDialog(
    currentTitle: String = "",
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,

    ) {
    var updatedTitle by remember { mutableStateOf(currentTitle) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Edit Task Title", fontWeight = FontWeight.Bold) },
        text = {
            OutlinedTextField(
                value = updatedTitle,
                onValueChange = { updatedTitle = it },
                label = { Text("Title") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (updatedTitle.isNotBlank()) {
                        onConfirm(updatedTitle)
                    }
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}


@Composable
fun OnBoardingBackground(content: @Composable BoxScope.() -> Unit) {
    val onboardingGlows = remember {
        listOf(
            GlowConfig(
                widthRatio = 630f,
                heightRatio = 630f,
                offsetXRatio = 217f,
                offsetYRatio = -384f,
                shapeType = GlowShape.OVAL
            ),
            GlowConfig(
                widthRatio = 630f,
                heightRatio = 630f,
                offsetXRatio = 14f,
                offsetYRatio = 326f,
                shapeType = GlowShape.CIRCLE,
                radiusPercent = 0.4772f
            ),
            GlowConfig(
                widthRatio = 872f,
                heightRatio = 792f,
                offsetXRatio = 432f,
                offsetYRatio = 832f,
                shapeType = GlowShape.CIRCLE,
                radiusPercent = 0.47f
            )
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


