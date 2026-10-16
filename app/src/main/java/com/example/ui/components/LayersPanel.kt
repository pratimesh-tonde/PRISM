package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Layer
import com.example.model.LayerBlendMode
import com.example.model.LayerType
import com.example.model.Project
import com.example.model.ShapeType
import com.example.ui.theme.PrismCyan
import com.example.ui.theme.PrismIndigo
import com.example.ui.theme.PrismRose
import com.example.ui.theme.StudioSurface
import com.example.ui.theme.StudioSurfaceBorder
import com.example.ui.theme.StudioSurfaceElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun LayersPanel(
    project: Project,
    onSelectLayer: (String) -> Unit,
    onToggleVisibility: (String) -> Unit,
    onToggleLock: (String) -> Unit,
    onUpdateOpacity: (String, Float) -> Unit,
    onUpdateBlendMode: (String, LayerBlendMode) -> Unit,
    onReorderLayer: (Int, Int) -> Unit,
    onDuplicateLayer: (String) -> Unit,
    onDeleteLayer: (String) -> Unit,
    onAddTextLayer: () -> Unit,
    onAddShapeLayer: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activeLayer = project.layers.find { it.id == project.activeLayerId } ?: project.layers.lastOrNull()
    var blendMenuExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(StudioSurface)
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Layers, contentDescription = null, tint = PrismIndigo)
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Layers Stack (${project.layers.size})",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
            }
        }

        HorizontalDivider(color = StudioSurfaceBorder, modifier = Modifier.padding(vertical = 10.dp))

        // Selected Layer Controls (Opacity & Blend Mode)
        activeLayer?.let { layer ->
            Surface(
                color = StudioSurfaceElevated,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Active: ${layer.name}",
                            color = PrismCyan,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        // Blend Mode Picker Button
                        Box {
                            Surface(
                                color = StudioSurfaceBorder,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.clickable { blendMenuExpanded = true }
                            ) {
                                Text(
                                    text = layer.blendMode.label,
                                    fontSize = 12.sp,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }

                            DropdownMenu(
                                expanded = blendMenuExpanded,
                                onDismissRequest = { blendMenuExpanded = false }
                            ) {
                                LayerBlendMode.entries.forEach { mode ->
                                    DropdownMenuItem(
                                        text = { Text(mode.label) },
                                        onClick = {
                                            onUpdateBlendMode(layer.id, mode)
                                            blendMenuExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(6.dp))

                    // Opacity Slider
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Opacity",
                            fontSize = 12.sp,
                            color = TextSecondary,
                            modifier = Modifier.width(60.dp)
                        )
                        Slider(
                            value = layer.opacity,
                            onValueChange = { onUpdateOpacity(layer.id, it) },
                            valueRange = 0f..1f,
                            colors = SliderDefaults.colors(
                                thumbColor = PrismIndigo,
                                activeTrackColor = PrismIndigo,
                                inactiveTrackColor = StudioSurfaceBorder
                            ),
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "${(layer.opacity * 100).toInt()}%",
                            fontSize = 12.sp,
                            color = TextSecondary,
                            modifier = Modifier.width(40.dp)
                        )
                    }

                    // Action buttons for active layer
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(
                            onClick = { onDuplicateLayer(layer.id) },
                            modifier = Modifier.size(34.dp)
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = "Duplicate", tint = TextSecondary, modifier = Modifier.size(18.dp))
                        }
                        IconButton(
                            onClick = { onDeleteLayer(layer.id) },
                            modifier = Modifier.size(34.dp)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = PrismRose, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }

        // Layers List (reversed so top layer is first like Photoshop)
        val reversedLayers = project.layers.asReversed()

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = false)
                .height(200.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            itemsIndexed(reversedLayers) { index, layer ->
                val actualIndex = project.layers.size - 1 - index
                val isSelected = layer.id == project.activeLayerId

                Surface(
                    color = if (isSelected) PrismIndigo.copy(alpha = 0.25f) else StudioSurfaceElevated,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = if (isSelected) 1.5.dp else 1.dp,
                            color = if (isSelected) PrismIndigo else StudioSurfaceBorder,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable { onSelectLayer(layer.id) }
                        .testTag("layer_item_${layer.id}")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Type Icon
                        val icon = when (layer.type) {
                            LayerType.IMAGE -> Icons.Default.Image
                            LayerType.TEXT -> Icons.Default.TextFields
                            LayerType.ADJUSTMENT -> Icons.Default.Tune
                            LayerType.SHAPE -> Icons.Default.Category
                        }
                        Icon(icon, contentDescription = null, tint = PrismCyan, modifier = Modifier.size(20.dp))

                        Spacer(Modifier.width(10.dp))

                        // Layer Name & Details
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = layer.name,
                                color = if (isSelected) Color.White else TextPrimary,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                maxLines = 1
                            )
                            Text(
                                text = "${layer.blendMode.label} • ${(layer.opacity * 100).toInt()}%",
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                        }

                        // Reorder buttons
                        IconButton(
                            onClick = {
                                if (actualIndex < project.layers.size - 1) {
                                    onReorderLayer(actualIndex, actualIndex + 1)
                                }
                            },
                            enabled = actualIndex < project.layers.size - 1,
                            modifier = Modifier.size(30.dp)
                        ) {
                            Icon(Icons.Default.ArrowUpward, contentDescription = "Move Up", tint = TextSecondary, modifier = Modifier.size(16.dp))
                        }

                        IconButton(
                            onClick = {
                                if (actualIndex > 0) {
                                    onReorderLayer(actualIndex, actualIndex - 1)
                                }
                            },
                            enabled = actualIndex > 0,
                            modifier = Modifier.size(30.dp)
                        ) {
                            Icon(Icons.Default.ArrowDownward, contentDescription = "Move Down", tint = TextSecondary, modifier = Modifier.size(16.dp))
                        }

                        // Lock Toggle
                        IconButton(
                            onClick = { onToggleLock(layer.id) },
                            modifier = Modifier.size(30.dp)
                        ) {
                            Icon(
                                if (layer.isLocked) Icons.Default.Lock else Icons.Default.LockOpen,
                                contentDescription = "Lock",
                                tint = if (layer.isLocked) PrismRose else TextMuted,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        // Visibility Toggle
                        IconButton(
                            onClick = { onToggleVisibility(layer.id) },
                            modifier = Modifier.size(30.dp)
                        ) {
                            Icon(
                                if (layer.isVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = "Visibility",
                                tint = if (layer.isVisible) Color.White else TextMuted,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(10.dp))

        // Bottom Add Layer Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilledTonalButton(
                onClick = onAddTextLayer,
                colors = ButtonDefaults.filledTonalButtonColors(containerColor = StudioSurfaceElevated),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(4.dp))
                Text("Add Text", fontSize = 12.sp)
            }

            FilledTonalButton(
                onClick = onAddShapeLayer,
                colors = ButtonDefaults.filledTonalButtonColors(containerColor = StudioSurfaceElevated),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(4.dp))
                Text("Add Shape", fontSize = 12.sp)
            }
        }
    }
}
