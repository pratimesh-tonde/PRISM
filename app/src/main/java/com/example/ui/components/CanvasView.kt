package com.example.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Compare
import androidx.compose.material.icons.filled.FitScreen
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Project
import com.example.ui.theme.CanvasGrid
import com.example.ui.theme.PrismIndigo
import com.example.ui.theme.SelectionGlow
import com.example.ui.theme.StudioBackground
import com.example.ui.theme.StudioSurfaceBorder
import com.example.viewmodel.StudioTool
import kotlin.math.roundToInt

@Composable
fun CanvasView(
    project: Project,
    compositeBitmap: Bitmap,
    originalBitmap: Bitmap?,
    showOriginal: Boolean,
    activeTool: StudioTool,
    zoomScale: Float,
    panOffset: Offset,
    brushSize: Float,
    brushColor: Color,
    onZoomChange: (Float) -> Unit,
    onPanChange: (Offset) -> Unit,
    onResetTransform: () -> Unit,
    onToggleShowOriginal: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var brushCursorPos by remember { mutableStateOf<Offset?>(null) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(StudioBackground)
            .pointerInput(activeTool) {
                if (activeTool == StudioTool.MOVE) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        onZoomChange(zoomScale * zoom)
                        onPanChange(pan)
                    }
                } else if (activeTool == StudioTool.BRUSH || activeTool == StudioTool.ERASER || activeTool == StudioTool.SELECT) {
                    detectDragGestures(
                        onDragStart = { offset -> brushCursorPos = offset },
                        onDrag = { change, _ ->
                            change.consume()
                            brushCursorPos = change.position
                        },
                        onDragEnd = { brushCursorPos = null },
                        onDragCancel = { brushCursorPos = null }
                    )
                }
            }
    ) {
        // Main Drawing Canvas
        Canvas(modifier = Modifier.fillMaxSize().testTag("studio_canvas")) {
            val canvasW = size.width
            val canvasH = size.height

            // 1. Draw subtle studio grid pattern
            val gridSize = 40f * zoomScale
            if (gridSize > 15f) {
                var x = panOffset.x % gridSize
                while (x < canvasW) {
                    drawLine(CanvasGrid, Offset(x, 0f), Offset(x, canvasH), strokeWidth = 1f)
                    x += gridSize
                }
                var y = panOffset.y % gridSize
                while (y < canvasH) {
                    drawLine(CanvasGrid, Offset(0f, y), Offset(canvasW, y), strokeWidth = 1f)
                    y += gridSize
                }
            }

            // 2. Draw the Image Bitmap scaled & panned
            val bmp = if (showOriginal && originalBitmap != null) originalBitmap else compositeBitmap
            val imgW = bmp.width.toFloat()
            val imgH = bmp.height.toFloat()

            // Calculate fitted base scale
            val fitScale = minOf(canvasW * 0.88f / imgW, canvasH * 0.75f / imgH)
            val currentScale = fitScale * zoomScale

            val drawW = imgW * currentScale
            val drawH = imgH * currentScale
            val startX = (canvasW - drawW) / 2f + panOffset.x
            val startY = (canvasH - drawH) / 2f + panOffset.y

            // Drop shadow behind artwork
            drawRect(
                color = Color.Black.copy(alpha = 0.5f),
                topLeft = Offset(startX + 8f, startY + 8f),
                size = Size(drawW, drawH)
            )

            // Draw composite image
            drawImage(
                image = bmp.asImageBitmap(),
                srcOffset = IntOffset.Zero,
                srcSize = IntSize(bmp.width, bmp.height),
                dstOffset = IntOffset(startX.roundToInt(), startY.roundToInt()),
                dstSize = IntSize(drawW.roundToInt(), drawH.roundToInt())
            )

            // Thin canvas frame border
            drawRect(
                color = StudioSurfaceBorder,
                topLeft = Offset(startX, startY),
                size = Size(drawW, drawH),
                style = Stroke(width = 1.5f)
            )

            // Active Selection glow or tool feedback
            if (activeTool == StudioTool.SELECT) {
                drawRect(
                    color = SelectionGlow.copy(alpha = 0.7f),
                    topLeft = Offset(startX, startY),
                    size = Size(drawW, drawH),
                    style = Stroke(width = 2.5f)
                )
            }

            // Draw brush cursor ring when painting
            brushCursorPos?.let { pos ->
                drawCircle(
                    color = if (activeTool == StudioTool.ERASER) Color.Red.copy(alpha = 0.8f) else brushColor,
                    radius = brushSize * 0.75f,
                    center = pos,
                    style = Stroke(width = 2.5f)
                )
            }
        }

        // Top-Right Floating Controls (Zoom in/out, Fit, Hold Original)
        Surface(
            color = Color(0xDD111722),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 16.dp, end = 16.dp)
        ) {
            androidx.compose.foundation.layout.Row(
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilledTonalIconButton(
                    onClick = { onZoomChange((zoomScale * 1.25f).coerceAtMost(5f)) },
                    modifier = Modifier.size(36.dp),
                    colors = IconButtonDefaults.filledTonalIconButtonColors(containerColor = Color.Transparent)
                ) {
                    Icon(Icons.Default.ZoomIn, contentDescription = "Zoom In", tint = Color.White)
                }

                Text(
                    text = "${(zoomScale * 100).toInt()}%",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 11.sp,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )

                FilledTonalIconButton(
                    onClick = { onZoomChange((zoomScale * 0.8f).coerceAtLeast(0.2f)) },
                    modifier = Modifier.size(36.dp),
                    colors = IconButtonDefaults.filledTonalIconButtonColors(containerColor = Color.Transparent)
                ) {
                    Icon(Icons.Default.ZoomOut, contentDescription = "Zoom Out", tint = Color.White)
                }

                FilledTonalIconButton(
                    onClick = onResetTransform,
                    modifier = Modifier.size(36.dp),
                    colors = IconButtonDefaults.filledTonalIconButtonColors(containerColor = Color.Transparent)
                ) {
                    Icon(Icons.Default.FitScreen, contentDescription = "Fit Canvas", tint = Color.White)
                }

                FilledTonalIconButton(
                    onClick = { onToggleShowOriginal(!showOriginal) },
                    modifier = Modifier.size(36.dp),
                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                        containerColor = if (showOriginal) PrismIndigo else Color.Transparent
                    )
                ) {
                    Icon(Icons.Default.Compare, contentDescription = "Compare Original", tint = Color.White)
                }
            }
        }

        // Compare indicator banner if viewing original
        if (showOriginal) {
            Surface(
                color = PrismIndigo.copy(alpha = 0.9f),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp)
            ) {
                Text(
                    text = "ORIGINAL PHOTO (Tap compare icon to return)",
                    color = Color.White,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
}
