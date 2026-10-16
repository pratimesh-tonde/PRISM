package com.example.engine

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RadialGradient
import android.graphics.RectF
import android.graphics.Shader

object SampleImages {

    /**
     * Creates a high-fidelity stylized portrait with hair, face, eyes, lips, and studio background.
     */
    fun createPortraitBitmap(width: Int = 800, height: Int = 1000): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        // Background: Warm atmospheric studio gradient
        val bgShader = LinearGradient(
            0f, 0f, width.toFloat(), height.toFloat(),
            intArrayOf(Color.rgb(30, 27, 75), Color.rgb(79, 70, 229), Color.rgb(15, 23, 42)),
            null, Shader.TileMode.CLAMP
        )
        paint.shader = bgShader
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
        paint.shader = null

        // Ambient rim light in background
        val glowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = RadialGradient(
                width * 0.5f, height * 0.4f, width * 0.6f,
                Color.argb(120, 244, 63, 94), Color.TRANSPARENT, Shader.TileMode.CLAMP
            )
        }
        canvas.drawCircle(width * 0.5f, height * 0.4f, width * 0.5f, glowPaint)

        // Shoulders / Torso clothing
        paint.color = Color.rgb(24, 24, 27)
        val bodyPath = Path().apply {
            moveTo(width * 0.15f, height.toFloat())
            quadTo(width * 0.25f, height * 0.72f, width * 0.38f, height * 0.65f)
            lineTo(width * 0.62f, height * 0.65f)
            quadTo(width * 0.75f, height * 0.72f, width * 0.85f, height.toFloat())
            close()
        }
        canvas.drawPath(bodyPath, paint)

        // Neck
        paint.color = Color.rgb(234, 179, 142)
        val neckRect = RectF(width * 0.42f, height * 0.52f, width * 0.58f, height * 0.68f)
        canvas.drawRoundRect(neckRect, 20f, 20f, paint)

        // Neck shadow
        paint.color = Color.argb(60, 120, 53, 15)
        canvas.drawRect(width * 0.42f, height * 0.52f, width * 0.58f, height * 0.58f, paint)

        // Hair Back
        paint.color = Color.rgb(41, 23, 16)
        canvas.drawCircle(width * 0.5f, height * 0.38f, width * 0.32f, paint)

        // Head / Face
        paint.color = Color.rgb(245, 198, 165)
        val faceOval = RectF(width * 0.30f, height * 0.22f, width * 0.70f, height * 0.58f)
        canvas.drawOval(faceOval, paint)

        // Cheeks blush
        val blushPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = RadialGradient(
                width * 0.38f, height * 0.44f, width * 0.12f,
                Color.argb(70, 244, 63, 94), Color.TRANSPARENT, Shader.TileMode.CLAMP
            )
        }
        canvas.drawCircle(width * 0.38f, height * 0.44f, width * 0.1f, blushPaint)
        blushPaint.shader = RadialGradient(
            width * 0.62f, height * 0.44f, width * 0.12f,
            Color.argb(70, 244, 63, 94), Color.TRANSPARENT, Shader.TileMode.CLAMP
        )
        canvas.drawCircle(width * 0.62f, height * 0.44f, width * 0.1f, blushPaint)

        // Hair Front / Bangs
        paint.color = Color.rgb(41, 23, 16)
        val hairPath = Path().apply {
            moveTo(width * 0.26f, height * 0.35f)
            quadTo(width * 0.35f, height * 0.20f, width * 0.50f, height * 0.22f)
            quadTo(width * 0.65f, height * 0.18f, width * 0.74f, height * 0.35f)
            quadTo(width * 0.60f, height * 0.28f, width * 0.50f, height * 0.30f)
            quadTo(width * 0.40f, height * 0.28f, width * 0.26f, height * 0.35f)
            close()
        }
        canvas.drawPath(hairPath, paint)

        // Eyebrows
        paint.color = Color.rgb(60, 35, 25)
        paint.strokeWidth = 6f
        paint.style = Paint.Style.STROKE
        canvas.drawLine(width * 0.37f, height * 0.36f, width * 0.46f, height * 0.355f, paint)
        canvas.drawLine(width * 0.54f, height * 0.355f, width * 0.63f, height * 0.36f, paint)

        // Eyes (Almond shape)
        paint.style = Paint.Style.FILL
        paint.color = Color.WHITE
        val leftEye = RectF(width * 0.38f, height * 0.38f, width * 0.46f, height * 0.42f)
        val rightEye = RectF(width * 0.54f, height * 0.38f, width * 0.62f, height * 0.42f)
        canvas.drawOval(leftEye, paint)
        canvas.drawOval(rightEye, paint)

        // Irises
        paint.color = Color.rgb(45, 85, 125)
        canvas.drawCircle(width * 0.42f, height * 0.40f, width * 0.024f, paint)
        canvas.drawCircle(width * 0.58f, height * 0.40f, width * 0.024f, paint)

        // Pupils & Glint
        paint.color = Color.BLACK
        canvas.drawCircle(width * 0.42f, height * 0.40f, width * 0.012f, paint)
        canvas.drawCircle(width * 0.58f, height * 0.40f, width * 0.012f, paint)
        paint.color = Color.WHITE
        canvas.drawCircle(width * 0.425f, height * 0.395f, width * 0.005f, paint)
        canvas.drawCircle(width * 0.585f, height * 0.395f, width * 0.005f, paint)

        // Nose tip & bridge
        paint.color = Color.rgb(217, 150, 115)
        paint.strokeWidth = 4f
        paint.style = Paint.Style.STROKE
        val nosePath = Path().apply {
            moveTo(width * 0.50f, height * 0.38f)
            lineTo(width * 0.495f, height * 0.46f)
            quadTo(width * 0.50f, height * 0.475f, width * 0.52f, height * 0.47f)
        }
        canvas.drawPath(nosePath, paint)

        // Lips
        paint.style = Paint.Style.FILL
        paint.color = Color.rgb(225, 80, 100)
        val lipPath = Path().apply {
            moveTo(width * 0.44f, height * 0.52f)
            quadTo(width * 0.50f, height * 0.505f, width * 0.56f, height * 0.52f)
            quadTo(width * 0.50f, height * 0.545f, width * 0.44f, height * 0.52f)
            close()
        }
        canvas.drawPath(lipPath, paint)

        return bitmap
    }

    /**
     * E-Commerce Product Shot: Luxury watch on reflective stone slab.
     */
    fun createProductBitmap(width: Int = 900, height: Int = 900): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        // Studio clean floor & wall gradient
        val bgShader = LinearGradient(
            0f, 0f, 0f, height.toFloat(),
            intArrayOf(Color.rgb(220, 225, 235), Color.rgb(240, 243, 248), Color.rgb(195, 200, 210)),
            null, Shader.TileMode.CLAMP
        )
        paint.shader = bgShader
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
        paint.shader = null

        // Podium / pedestal
        paint.color = Color.rgb(180, 185, 195)
        val podium = RectF(width * 0.2f, height * 0.65f, width * 0.8f, height * 0.85f)
        canvas.drawRoundRect(podium, 30f, 30f, paint)

        // Drop shadow for watch
        val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.argb(80, 20, 25, 35)
        }
        canvas.drawOval(RectF(width * 0.32f, height * 0.66f, width * 0.68f, height * 0.72f), shadowPaint)

        // Watch Straps
        paint.color = Color.rgb(28, 32, 40)
        canvas.drawRoundRect(RectF(width * 0.44f, height * 0.22f, width * 0.56f, height * 0.40f), 15f, 15f, paint)
        canvas.drawRoundRect(RectF(width * 0.44f, height * 0.58f, width * 0.56f, height * 0.76f), 15f, 15f, paint)

        // Watch Case (Steel gold bezel)
        paint.color = Color.rgb(212, 160, 23)
        canvas.drawCircle(width * 0.5f, height * 0.5f, width * 0.21f, paint)

        paint.color = Color.rgb(250, 215, 90)
        canvas.drawCircle(width * 0.5f, height * 0.5f, width * 0.19f, paint)

        // Dial face (Deep emerald sunburst)
        paint.color = Color.rgb(6, 78, 59)
        canvas.drawCircle(width * 0.5f, height * 0.5f, width * 0.17f, paint)

        // Hour markers
        paint.color = Color.rgb(250, 215, 90)
        paint.strokeWidth = 4f
        for (i in 0 until 12) {
            val angle = Math.toRadians((i * 30).toDouble())
            val startR = width * 0.14
            val endR = width * 0.165
            val sx = (width * 0.5 + Math.cos(angle) * startR).toFloat()
            val sy = (height * 0.5 + Math.sin(angle) * startR).toFloat()
            val ex = (width * 0.5 + Math.cos(angle) * endR).toFloat()
            val ey = (height * 0.5 + Math.sin(angle) * endR).toFloat()
            canvas.drawLine(sx, sy, ex, ey, paint)
        }

        // Watch hands
        paint.strokeWidth = 6f
        paint.color = Color.WHITE
        canvas.drawLine(width * 0.5f, height * 0.5f, width * 0.57f, height * 0.44f, paint)
        paint.strokeWidth = 4f
        canvas.drawLine(width * 0.5f, height * 0.5f, width * 0.45f, height * 0.38f, paint)
        paint.color = Color.rgb(239, 68, 68)
        paint.strokeWidth = 2f
        canvas.drawLine(width * 0.5f, height * 0.5f, width * 0.52f, height * 0.62f, paint)

        // Center pinion
        paint.color = Color.rgb(250, 215, 90)
        canvas.drawCircle(width * 0.5f, height * 0.5f, 8f, paint)

        return bitmap
    }

    /**
     * Sunset Landscape with mountains, lake, and golden hour sky.
     */
    fun createLandscapeBitmap(width: Int = 1000, height: Int = 800): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        // Sunset sky gradient
        val skyShader = LinearGradient(
            0f, 0f, 0f, height * 0.6f,
            intArrayOf(Color.rgb(30, 27, 75), Color.rgb(180, 83, 9), Color.rgb(249, 115, 22), Color.rgb(254, 240, 138)),
            null, Shader.TileMode.CLAMP
        )
        paint.shader = skyShader
        canvas.drawRect(0f, 0f, width.toFloat(), height * 0.6f, paint)
        paint.shader = null

        // Glowing sun
        paint.color = Color.rgb(255, 255, 220)
        canvas.drawCircle(width * 0.55f, height * 0.40f, width * 0.09f, paint)

        // Distant Mountains
        paint.color = Color.rgb(88, 28, 135)
        val mtn1 = Path().apply {
            moveTo(0f, height * 0.6f)
            lineTo(width * 0.25f, height * 0.35f)
            lineTo(width * 0.55f, height * 0.50f)
            lineTo(width * 0.85f, height * 0.30f)
            lineTo(width.toFloat(), height * 0.55f)
            lineTo(width.toFloat(), height * 0.6f)
            close()
        }
        canvas.drawPath(mtn1, paint)

        // Foreground Mountain Ridge
        paint.color = Color.rgb(46, 16, 101)
        val mtn2 = Path().apply {
            moveTo(0f, height * 0.6f)
            lineTo(width * 0.35f, height * 0.42f)
            lineTo(width * 0.70f, height * 0.6f)
            close()
        }
        canvas.drawPath(mtn2, paint)

        // Lake Water with Reflections
        val lakeShader = LinearGradient(
            0f, height * 0.6f, 0f, height.toFloat(),
            intArrayOf(Color.rgb(249, 115, 22), Color.rgb(124, 45, 18), Color.rgb(15, 23, 42)),
            null, Shader.TileMode.CLAMP
        )
        paint.shader = lakeShader
        canvas.drawRect(0f, height * 0.6f, width.toFloat(), height.toFloat(), paint)
        paint.shader = null

        // Pine trees silhouette on foreground edge
        paint.color = Color.rgb(15, 23, 42)
        val ground = Path().apply {
            moveTo(0f, height.toFloat())
            lineTo(0f, height * 0.78f)
            quadTo(width * 0.3f, height * 0.72f, width * 0.45f, height.toFloat())
            close()
        }
        canvas.drawPath(ground, paint)

        return bitmap
    }

    /**
     * Old vintage photo for restoration with scratches and sepia tone.
     */
    fun createVintagePhotoBitmap(width: Int = 800, height: Int = 1000): Bitmap {
        val base = createPortraitBitmap(width, height)
        val canvas = Canvas(base)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        // Sepia tint overlay
        paint.color = Color.argb(140, 180, 140, 100)
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)

        // Add vintage scratches and noise
        val scratchPaint = Paint().apply {
            color = Color.argb(160, 240, 235, 220)
            strokeWidth = 2.5f
        }
        canvas.drawLine(width * 0.2f, 0f, width * 0.25f, height.toFloat(), scratchPaint)
        canvas.drawLine(width * 0.75f, height * 0.1f, width * 0.72f, height * 0.9f, scratchPaint)
        canvas.drawLine(width * 0.48f, height * 0.35f, width * 0.52f, height * 0.55f, scratchPaint)

        // Corner vignette wear
        val vignette = RadialGradient(
            width * 0.5f, height * 0.5f, width * 0.65f,
            Color.TRANSPARENT, Color.argb(180, 50, 30, 20), Shader.TileMode.CLAMP
        )
        paint.shader = vignette
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)

        return base
    }
}
