package com.example.orchestrator

import android.graphics.Bitmap
import android.graphics.Rect
import com.example.engine.ImageProcessor
import com.example.model.AiPlan
import com.example.model.AiPlanStep
import com.example.model.AiToolId
import com.example.model.StepStatus
import kotlinx.coroutines.delay

object AiOrchestrator {

    val samplePrompts = listOf(
        "Background hata ke tropical beach laga do, skin smooth karo",
        "Cyberpunk neon street background aur dual rim lighting",
        "1-Click Auto enhance with cinematic color grading",
        "Luxury studio marble pedestal background with softbox lighting",
        "Add retro sunglasses and upscale 2x",
        "Old photo restore, fix scratches and colorize"
    )

    /**
     * Converts a user natural language prompt into a structured tool execution plan.
     */
    fun createPlan(prompt: String): AiPlan {
        val lower = prompt.lowercase()
        val steps = mutableListOf<AiPlanStep>()

        // 1. Background operations
        val hasRemoveBg = lower.contains("background hata") || lower.contains("remove bg") ||
                lower.contains("remove background") || lower.contains("cutout") ||
                lower.contains("background change") || lower.contains("beach") ||
                lower.contains("marble") || lower.contains("neon") || lower.contains("studio")

        val hasReplaceBg = lower.contains("beach") || lower.contains("marble") ||
                lower.contains("neon") || lower.contains("studio") || lower.contains("laga do") ||
                lower.contains("replace")

        if (hasRemoveBg && !hasReplaceBg) {
            steps.add(
                AiPlanStep(
                    toolId = AiToolId.REMOVE_BG,
                    functionName = "remove_background",
                    title = "Isolate Subject",
                    description = "Extracting subject with BiRefNet precision alpha mask",
                    params = mapOf("feather" to 0f)
                )
            )
        }

        if (hasReplaceBg) {
            val scene = when {
                lower.contains("beach") -> "Tropical Beach"
                lower.contains("neon") || lower.contains("cyberpunk") -> "Cyberpunk Street"
                lower.contains("marble") || lower.contains("studio") -> "Luxury Marble"
                else -> "Sunset"
            }
            steps.add(
                AiPlanStep(
                    toolId = AiToolId.REPLACE_BG,
                    functionName = "replace_background",
                    title = "Generate $scene Backdrop",
                    description = "Compositing new backdrop and lighting environment",
                    params = mapOf("scene" to scene)
                )
            )
        }

        // 2. Generative Fill (Accessories, Sunglasses)
        if (lower.contains("glasses") || lower.contains("sunglasses") || lower.contains("chashma")) {
            steps.add(
                AiPlanStep(
                    toolId = AiToolId.GENERATIVE_FILL,
                    functionName = "generative_fill",
                    title = "Generative Fill: Sunglasses",
                    description = "Inpainting gold frame retro sunglasses on subject",
                    params = mapOf("prompt" to "retro sunglasses")
                )
            )
        }

        // 3. Face Retouching
        if (lower.contains("skin smooth") || lower.contains("retouch") || lower.contains("face") || lower.contains("chehra")) {
            steps.add(
                AiPlanStep(
                    toolId = AiToolId.FACE_RETOUCH,
                    functionName = "retouch_face",
                    title = "Portrait Face Retouch",
                    description = "CodeFormer neural skin smoothing and eye clarity",
                    params = mapOf("strength" to 0.6f)
                )
            )
        }

        // 4. Relighting
        if (lower.contains("lighting") || lower.contains("relight") || lower.contains("sunset") || lower.contains("rim")) {
            val lightDir = when {
                lower.contains("golden") || lower.contains("sunset") -> "Golden Hour"
                lower.contains("cyberpunk") || lower.contains("neon") -> "Cyberpunk"
                else -> "Left"
            }
            steps.add(
                AiPlanStep(
                    toolId = AiToolId.RELIGHT,
                    functionName = "relight_subject",
                    title = "Relight Subject ($lightDir)",
                    description = "Synthesizing 3D directional rim light & ambient reflections",
                    params = mapOf("direction" to lightDir)
                )
            )
        }

        // 5. Restore / Colorize
        if (lower.contains("scratch") || lower.contains("restore") || lower.contains("colorize") || lower.contains("old photo")) {
            steps.add(
                AiPlanStep(
                    toolId = AiToolId.OLD_PHOTO_RESTORE,
                    functionName = "restore_and_colorize",
                    title = "Neural Scratch Repair & Colorize",
                    description = "Deep restoration network healing tears and adding color",
                    params = emptyMap()
                )
            )
        }

        // 6. Auto Enhance / Color Grade
        if (lower.contains("enhance") || lower.contains("cinematic") || lower.contains("color grade") || lower.contains("hdr") || steps.isEmpty()) {
            steps.add(
                AiPlanStep(
                    toolId = AiToolId.AUTO_ENHANCE,
                    functionName = "auto_enhance",
                    title = "Auto Dynamic Range & Color Grade",
                    description = "Histogram equalization, clarity balance, and cinematic LUT",
                    params = mapOf("filter" to "Cinematic")
                )
            )
        }

        // 7. Upscale
        if (lower.contains("upscale") || lower.contains("4k") || lower.contains("2x") || lower.contains("hd")) {
            steps.add(
                AiPlanStep(
                    toolId = AiToolId.UPSCALE,
                    functionName = "super_resolution_upscale",
                    title = "Real-ESRGAN 2x Upscale",
                    description = "Super-resolution edge reconstruction and unsharp deblur",
                    params = mapOf("factor" to 2)
                )
            )
        }

        return AiPlan(
            originalPrompt = prompt,
            steps = steps
        )
    }

    /**
     * Executes a single plan step on the provided bitmap using the model runner.
     */
    suspend fun executeStep(step: AiPlanStep, baseBitmap: Bitmap): Bitmap {
        // Simulate async model inference latency (realistic GPU execution)
        delay(600)

        return when (step.toolId) {
            AiToolId.REMOVE_BG -> {
                ImageProcessor.removeBackground(baseBitmap)
            }
            AiToolId.REPLACE_BG -> {
                val scene = step.params["scene"] as? String ?: "Beach"
                ImageProcessor.replaceBackground(baseBitmap, scene)
            }
            AiToolId.GENERATIVE_FILL -> {
                val p = step.params["prompt"] as? String ?: "retro sunglasses"
                val w = baseBitmap.width
                val h = baseBitmap.height
                val eyeRect = Rect((w * 0.35f).toInt(), (h * 0.37f).toInt(), (w * 0.65f).toInt(), (h * 0.44f).toInt())
                ImageProcessor.generativeFill(baseBitmap, eyeRect, p)
            }
            AiToolId.OBJECT_REMOVE -> {
                val w = baseBitmap.width
                val h = baseBitmap.height
                val sampleRect = Rect((w * 0.1f).toInt(), (h * 0.1f).toInt(), (w * 0.3f).toInt(), (h * 0.3f).toInt())
                ImageProcessor.removeObject(baseBitmap, sampleRect)
            }
            AiToolId.GENERATIVE_EXPAND -> {
                ImageProcessor.generativeExpand(baseBitmap, 1.35f)
            }
            AiToolId.FACE_RETOUCH -> {
                val strength = step.params["strength"] as? Float ?: 0.5f
                ImageProcessor.faceRetouch(baseBitmap, strength)
            }
            AiToolId.RELIGHT -> {
                val dir = step.params["direction"] as? String ?: "Left"
                ImageProcessor.relight(baseBitmap, dir)
            }
            AiToolId.AUTO_ENHANCE -> {
                ImageProcessor.autoEnhance(baseBitmap)
            }
            AiToolId.OLD_PHOTO_RESTORE -> {
                ImageProcessor.restoreAndColorize(baseBitmap)
            }
            AiToolId.UPSCALE -> {
                val factor = (step.params["factor"] as? Int) ?: 2
                ImageProcessor.upscaleAndDeblur(baseBitmap, factor)
            }
            else -> {
                ImageProcessor.autoEnhance(baseBitmap)
            }
        }
    }
}
