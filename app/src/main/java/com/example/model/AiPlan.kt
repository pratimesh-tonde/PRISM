package com.example.model

import android.graphics.Bitmap
import java.util.UUID

enum class StepStatus {
    PENDING,
    RUNNING,
    COMPLETED,
    FAILED
}

data class AiPlanStep(
    val id: String = UUID.randomUUID().toString(),
    val toolId: AiToolId,
    val functionName: String,
    val title: String,
    val description: String,
    val params: Map<String, Any> = emptyMap(),
    val status: StepStatus = StepStatus.PENDING,
    val generatedBitmap: Bitmap? = null,
    val targetLayerName: String? = null
)

data class AiPlan(
    val id: String = UUID.randomUUID().toString(),
    val originalPrompt: String,
    val steps: List<AiPlanStep>,
    val currentIndex: Int = 0,
    val isExecuting: Boolean = false,
    val isCompleted: Boolean = false,
    val finalPreviewBitmap: Bitmap? = null
)
