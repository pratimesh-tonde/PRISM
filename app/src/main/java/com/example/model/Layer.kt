package com.example.model

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import java.util.UUID

enum class LayerType {
    IMAGE,
    TEXT,
    ADJUSTMENT,
    SHAPE
}

enum class LayerBlendMode(val label: String) {
    NORMAL("Normal"),
    MULTIPLY("Multiply"),
    SCREEN("Screen"),
    OVERLAY("Overlay"),
    DARKEN("Darken"),
    LIGHTEN("Lighten"),
    SOFT_LIGHT("Soft Light"),
    COLOR_DODGE("Color Dodge")
}

data class AdjustmentData(
    val brightness: Float = 0f,    // -100 to 100
    val contrast: Float = 0f,      // -100 to 100
    val saturation: Float = 0f,    // -100 to 100
    val exposure: Float = 0f,      // -100 to 100
    val warmth: Float = 0f,        // -100 to 100
    val vignette: Float = 0f,      // 0 to 100
    val filterName: String? = null // Preset filter if any
)

data class TextData(
    val text: String = "PRISM STUDIO",
    val fontSizeSp: Float = 28f,
    val color: Color = Color.White,
    val isBold: Boolean = true,
    val isItalic: Boolean = false,
    val hasShadow: Boolean = true
)

enum class ShapeType(val label: String) {
    RECTANGLE("Rectangle"),
    ROUNDED_RECT("Rounded"),
    CIRCLE("Circle"),
    BADGE("Badge"),
    FRAME("Studio Frame")
}

data class ShapeData(
    val type: ShapeType = ShapeType.ROUNDED_RECT,
    val fillColor: Color = Color(0x666366F1),
    val strokeColor: Color = Color(0xFF6366F1),
    val strokeWidth: Float = 3f
)

data class Layer(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val type: LayerType = LayerType.IMAGE,
    val bitmap: Bitmap? = null,
    val adjustments: AdjustmentData = AdjustmentData(),
    val textData: TextData? = null,
    val shapeData: ShapeData? = null,
    val opacity: Float = 1.0f,
    val blendMode: LayerBlendMode = LayerBlendMode.NORMAL,
    val isVisible: Boolean = true,
    val isLocked: Boolean = false,
    val x: Float = 0f,
    val y: Float = 0f,
    val scale: Float = 1f,
    val rotation: Float = 0f
)
