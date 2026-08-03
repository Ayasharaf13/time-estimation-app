package com.example.scoretask.utilities

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class TimeUnitTransformation(val unit: String) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val out = text.text + unit
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int = offset
            override fun transformedToOriginal(offset: Int): Int =
                if (offset >= text.length) text.length else offset
        }
        return TransformedText(androidx.compose.ui.text.AnnotatedString(out), offsetMapping)
    }
}