package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Tune
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AdjustmentData
import com.example.ui.theme.PrismCyan
import com.example.ui.theme.PrismIndigo
import com.example.ui.theme.StudioSurface
import com.example.ui.theme.StudioSurfaceBorder
import com.example.ui.theme.StudioSurfaceElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun AdjustmentsSheet(
    initialAdjustments: AdjustmentData,
    onApplyAdjustments: (AdjustmentData) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    var adj by remember { mutableStateOf(initialAdjustments) }

    val presetFilters = listOf("None", "Cinematic", "Cyberpunk", "Golden Hour", "Vintage", "B&W Dramatic", "Emerald")

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
                Icon(Icons.Default.Tune, contentDescription = null, tint = PrismIndigo)
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Color Grading & Adjustments",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            Row {
                IconButton(
                    onClick = {
                        adj = AdjustmentData()
                        onApplyAdjustments(adj)
                    }
                ) {
                    Icon(Icons.Default.RestartAlt, contentDescription = "Reset", tint = TextSecondary)
                }
                IconButton(onClick = onClose) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                }
            }
        }

        HorizontalDivider(color = StudioSurfaceBorder, modifier = Modifier.padding(vertical = 8.dp))

        // Preset Filters Horizontal Row
        Text(
            text = "LOOK PRESETS",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = TextMuted,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            presetFilters.forEach { filter ->
                val isSelected = (adj.filterName == filter) || (filter == "None" && adj.filterName == null)
                Surface(
                    color = if (isSelected) PrismIndigo else StudioSurfaceElevated,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.clickable {
                        adj = adj.copy(filterName = if (filter == "None") null else filter)
                        onApplyAdjustments(adj)
                    }
                ) {
                    Text(
                        text = filter,
                        color = if (isSelected) Color.White else TextSecondary,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(Modifier.height(14.dp))

        // Sliders List
        AdjustmentSliderItem("Exposure", adj.exposure, -100f..100f) {
            adj = adj.copy(exposure = it); onApplyAdjustments(adj)
        }
        AdjustmentSliderItem("Brightness", adj.brightness, -100f..100f) {
            adj = adj.copy(brightness = it); onApplyAdjustments(adj)
        }
        AdjustmentSliderItem("Contrast", adj.contrast, -100f..100f) {
            adj = adj.copy(contrast = it); onApplyAdjustments(adj)
        }
        AdjustmentSliderItem("Saturation", adj.saturation, -100f..100f) {
            adj = adj.copy(saturation = it); onApplyAdjustments(adj)
        }
        AdjustmentSliderItem("Warmth", adj.warmth, -100f..100f) {
            adj = adj.copy(warmth = it); onApplyAdjustments(adj)
        }
        AdjustmentSliderItem("Vignette", adj.vignette, 0f..100f) {
            adj = adj.copy(vignette = it); onApplyAdjustments(adj)
        }
    }
}

@Composable
private fun AdjustmentSliderItem(
    label: String,
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = TextSecondary,
            modifier = Modifier.width(76.dp)
        )
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = range,
            colors = SliderDefaults.colors(
                thumbColor = PrismCyan,
                activeTrackColor = PrismIndigo,
                inactiveTrackColor = StudioSurfaceBorder
            ),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value.toInt().toString(),
            fontSize = 11.sp,
            color = TextMuted,
            modifier = Modifier.width(36.dp)
        )
    }
}
