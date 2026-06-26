package com.example.scoretask

import androidx.compose.ui.graphics.Color

data class GlowConfig(

    val widthRatio: Float,
    val heightRatio: Float,
    val offsetXRatio: Float,
    val offsetYRatio: Float,
    val centerXPercent: Float = 0.6186f,
    val centerYPercent: Float = 0.3892f,
    val radiusPercent: Float = 0.5772f,
    val newOffsetY: Float = 0f,
    val alpha: Float = 1.0f,
    val blurAfter: Boolean = false,
    val shapeType: GlowShape = GlowShape.CIRCLE,
    val customColors: ((c1: Color, c2: Color) -> List<Pair<Float, Color>>)? = null
)

enum  class GlowShape {
    CIRCLE, OVAL, RECTANGLE
}
