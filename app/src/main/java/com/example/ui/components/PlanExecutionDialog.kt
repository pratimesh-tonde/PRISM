package com.example.ui.components

import android.graphics.Bitmap
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.AiPlan
import com.example.model.StepStatus
import com.example.ui.theme.PrismCyan
import com.example.ui.theme.PrismEmerald
import com.example.ui.theme.PrismIndigo
import com.example.ui.theme.PrismRose
import com.example.ui.theme.StudioSurface
import com.example.ui.theme.StudioSurfaceBorder
import com.example.ui.theme.StudioSurfaceElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun PlanExecutionDialog(
    plan: AiPlan,
    previewBitmap: Bitmap?,
    isExecuting: Boolean,
    onApprove: () -> Unit,
    onDiscard: () -> Unit
) {
    Dialog(onDismissRequest = { if (!isExecuting) onDiscard() }) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = StudioSurface,
            border = androidx.compose.foundation.BorderStroke(1.5.dp, PrismIndigo.copy(alpha = 0.5f)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(18.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(PrismIndigo.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = PrismCyan, modifier = Modifier.size(18.dp))
                        }
                        Spacer(Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "AI Orchestrator Pipeline",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = TextPrimary
                            )
                            Text(
                                text = "LLM Planner → Neural Tool Router",
                                fontSize = 11.sp,
                                color = PrismCyan
                            )
                        }
                    }

                    if (!isExecuting) {
                        IconButton(onClick = onDiscard) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                        }
                    }
                }

                HorizontalDivider(color = StudioSurfaceBorder, modifier = Modifier.padding(vertical = 12.dp))

                // User Prompt Callout
                Surface(
                    color = StudioSurfaceElevated,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "USER PROMPT",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMuted
                        )
                        Text(
                            text = "\"${plan.originalPrompt}\"",
                            fontSize = 13.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(Modifier.height(14.dp))

                // Steps Execution List
                Text(
                    text = "DECOMPOSED PLAN STEPS (${plan.steps.size})",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted,
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                plan.steps.forEachIndexed { index, step ->
                    Surface(
                        color = when (step.status) {
                            StepStatus.RUNNING -> PrismIndigo.copy(alpha = 0.2f)
                            StepStatus.COMPLETED -> StudioSurfaceElevated
                            else -> StudioSurfaceElevated.copy(alpha = 0.6f)
                        },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .border(
                                width = 1.dp,
                                color = when (step.status) {
                                    StepStatus.RUNNING -> PrismIndigo
                                    StepStatus.COMPLETED -> PrismEmerald.copy(alpha = 0.6f)
                                    else -> StudioSurfaceBorder
                                },
                                shape = RoundedCornerShape(10.dp)
                            )
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Step Number / Status Icon
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when (step.status) {
                                            StepStatus.RUNNING -> PrismIndigo
                                            StepStatus.COMPLETED -> PrismEmerald
                                            else -> StudioSurfaceBorder
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                when (step.status) {
                                    StepStatus.RUNNING -> {
                                        CircularProgressIndicator(
                                            color = Color.White,
                                            modifier = Modifier.size(14.dp),
                                            strokeWidth = 2.dp
                                        )
                                    }
                                    StepStatus.COMPLETED -> {
                                        Icon(
                                            Icons.Default.Done,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                    else -> {
                                        Text(
                                            text = "${index + 1}",
                                            fontSize = 11.sp,
                                            color = TextSecondary,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }

                            Spacer(Modifier.width(10.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "${step.functionName}()",
                                    color = if (step.status == StepStatus.COMPLETED) Color.White else TextPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = step.description,
                                    color = TextMuted,
                                    fontSize = 10.sp,
                                    lineHeight = 12.sp
                                )
                            }

                            Text(
                                text = when (step.status) {
                                    StepStatus.RUNNING -> "Running..."
                                    StepStatus.COMPLETED -> "Ready"
                                    StepStatus.FAILED -> "Failed"
                                    StepStatus.PENDING -> "Queued"
                                },
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = when (step.status) {
                                    StepStatus.RUNNING -> PrismCyan
                                    StepStatus.COMPLETED -> PrismEmerald
                                    else -> TextMuted
                                }
                            )
                        }
                    }
                }

                // Result Preview if ready
                if (previewBitmap != null) {
                    Spacer(Modifier.height(14.dp))
                    Text(
                        text = "PREVIEW (PRE-COMMIT APPROVAL)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrismCyan
                    )
                    Spacer(Modifier.height(6.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.Black),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            bitmap = previewBitmap.asImageBitmap(),
                            contentDescription = "AI Result Preview",
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Actions (Approve vs Discard)
                if (plan.isCompleted) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = onDiscard,
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = PrismRose),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Discard", fontSize = 13.sp)
                        }

                        Button(
                            onClick = onApprove,
                            colors = ButtonDefaults.buttonColors(containerColor = PrismIndigo),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("approve_plan_button")
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("Accept Layer", fontSize = 13.sp)
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            color = PrismIndigo,
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(Modifier.width(10.dp))
                        Text(
                            text = "Orchestrating GPU inference pipeline...",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}
