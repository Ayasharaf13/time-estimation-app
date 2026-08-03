package com.example.scoretask.feedback.view

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp





@Composable
fun FeedbackScreenExtraTime(
    onConfirmFeedback: (wasEnough: Boolean, gapMinutes: Int) -> Unit = { _, _ -> }
) {
    var feedbackAnswer by remember { mutableStateOf<FeedbackType?>(null) }
    var selectedGapMinutes by remember { mutableStateOf<Int?>(null) }
    var customTimeInput by remember { mutableStateOf("") }
    var isCustomSelected by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1C1B2B))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Notification Badge
        Surface(
            color = Color(0xFFFFB300).copy(alpha = 0.15f),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color(0xFFFFB300))
        ) {
            Text(
                text = "⚡ EXTRA TIME FINISHED",
                color = Color(0xFFFFB300),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Main Question
        Text(
            text = "Extra time is over! Did you complete the task?",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(28.dp))

        // 🟢 Option 1: Perfect Fit
        FeedbackOptionCard(
            title = "Yes, finished right on time! 🎯",
            isSelected = feedbackAnswer == FeedbackType.PERFECT,
            onClick = {
                feedbackAnswer = FeedbackType.PERFECT
                selectedGapMinutes = 0
                isCustomSelected = false
                customTimeInput = ""
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // ⏳ Option 2: Needed More Time
        FeedbackOptionCard(
            title = "Not yet, I needed more time ⏳",
            isSelected = feedbackAnswer == FeedbackType.NEEDED_MORE,
            onClick = {
                feedbackAnswer = FeedbackType.NEEDED_MORE
                selectedGapMinutes = null
                customTimeInput = ""
                isCustomSelected = false
            }
        )

        // 💡 Sub-Question: If "Needed More Time" is selected
        if (feedbackAnswer == FeedbackType.NEEDED_MORE) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "How much more time did you need?",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Preset Chip Selection (+5m, +10m, +15m, +30m)
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                listOf(5, 10, 15, 30).forEach { gap ->
                    val isSelected = !isCustomSelected && selectedGapMinutes == gap
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .background(
                                color = if (isSelected) Color(0xFF8C5BFF) else Color.White.copy(
                                    alpha = 0.08f
                                ),
                                shape = CircleShape
                            )
                            .border(
                                width = 1.dp,
                                color = if (isSelected) Color.White else Color.Transparent,
                                shape = CircleShape
                            )
                            .clickable {
                                isCustomSelected = false
                                customTimeInput =
                                    "" // 🎯 مهم جداً: تصفير النص المكتوب عند اختيار Chip
                                selectedGapMinutes = gap
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "+${gap}m",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ✏️ Custom Minute Input Field
            OutlinedTextField(
                value = customTimeInput,
                onValueChange = { input ->
                    if (input.all { it.isDigit() } && input.length <= 3) {
                        customTimeInput = input
                        isCustomSelected = true
                        selectedGapMinutes = input.toIntOrNull()
                    }
                },
                placeholder = {
                    Text(
                        "Or enter custom minutes...",
                        color = Color.White.copy(alpha = 0.4f),
                        fontSize = 12.sp
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF8C5BFF),
                    unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(0.85f)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // 🎯 الشرط المعدّل المحسّن والأكثر أماناً
        val isButtonEnabled = when (feedbackAnswer) {
            FeedbackType.PERFECT -> true
            FeedbackType.NEEDED_MORE -> (selectedGapMinutes ?: 0) > 0
            null -> false
            else -> {
                false
            }
        }

        Button(
            onClick = {
                val wasEnough = feedbackAnswer != FeedbackType.NEEDED_MORE
                val finalGap = selectedGapMinutes ?: 0
                onConfirmFeedback(wasEnough, finalGap)
            },
            enabled = isButtonEnabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black,
                disabledContainerColor = Color.White.copy(alpha = 0.3f),
                disabledContentColor = Color.Black.copy(alpha = 0.4f)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(24.dp)
        ) {
            Text("Save Feedback 📊", fontWeight = FontWeight.Bold)
        }
    }
}


// Enum لتحديد نوع التقييم
enum class FeedbackType { PERFECT, EARLY, NEEDED_MORE }

// Component كارت الاختيارات
@Composable
private fun FeedbackOptionCard(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = if (isSelected) Color(0xFF8C5BFF).copy(alpha = 0.25f) else Color.White.copy(
                    alpha = 0.05f
                ),
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 1.dp,
                color = if (isSelected) Color(0xFF8C5BFF) else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Text(
            text = title,
            color = if (isSelected) Color.White else Color.White.copy(alpha = 0.7f),
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}