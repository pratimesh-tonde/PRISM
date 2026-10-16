package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.CropFree
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AiToolId
import com.example.ui.theme.PrismAmber
import com.example.ui.theme.PrismCyan
import com.example.ui.theme.PrismIndigo
import com.example.ui.theme.StudioSurface
import com.example.ui.theme.StudioSurfaceBorder
import com.example.ui.theme.StudioSurfaceElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun AiToolsGrid(
    creditsAvailable: Int,
    onSelectTool: (AiToolId) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val toolsList = AiToolId.entries

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(StudioSurface)
            .padding(16.dp)
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = PrismCyan)
                Spacer(Modifier.width(8.dp))
                Column {
                    Text(
                        text = "PRISM AI Model Suite",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "12 Specialized Neural Tools • $creditsAvailable Credits",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
            }

            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
            }
        }

        HorizontalDivider(color = StudioSurfaceBorder, modifier = Modifier.padding(vertical = 10.dp))

        // Grid of 12 AI Tools
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(380.dp)
        ) {
            items(toolsList) { tool ->
                AiToolCard(
                    tool = tool,
                    onClick = { onSelectTool(tool) }
                )
            }
        }
    }
}

@Composable
private fun AiToolCard(
    tool: AiToolId,
    onClick: () -> Unit
) {
    val icon: ImageVector = when (tool) {
        AiToolId.SMART_SELECT -> Icons.Default.CropFree
        AiToolId.REMOVE_BG -> Icons.Default.AutoFixHigh
        AiToolId.REPLACE_BG -> Icons.Default.Image
        AiToolId.OBJECT_REMOVE -> Icons.Default.Brush
        AiToolId.GENERATIVE_FILL -> Icons.Default.AutoAwesome
        AiToolId.GENERATIVE_EXPAND -> Icons.Default.CropFree
        AiToolId.INSTRUCTION_EDIT -> Icons.Default.AutoAwesome
        AiToolId.UPSCALE -> Icons.Default.HighQuality
        AiToolId.FACE_RETOUCH -> Icons.Default.Face
        AiToolId.RELIGHT -> Icons.Default.LightMode
        AiToolId.AUTO_ENHANCE -> Icons.Default.Tune
        AiToolId.OLD_PHOTO_RESTORE -> Icons.Default.History
        AiToolId.TEXT_TO_IMAGE -> Icons.Default.ColorLens
    }

    Surface(
        color = StudioSurfaceElevated,
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("ai_tool_${tool.name}")
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(PrismIndigo.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = PrismCyan, modifier = Modifier.size(18.dp))
                }

                Surface(
                    color = PrismAmber.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "${tool.creditCost}c",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrismAmber,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = tool.title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                maxLines = 1
            )

            Text(
                text = tool.subtitle,
                fontSize = 10.sp,
                color = TextMuted,
                lineHeight = 13.sp,
                maxLines = 2,
                modifier = Modifier.height(28.dp)
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = tool.modelFamily,
                fontSize = 9.sp,
                fontWeight = FontWeight.SemiBold,
                color = PrismIndigo,
                maxLines = 1
            )
        }
    }
}
