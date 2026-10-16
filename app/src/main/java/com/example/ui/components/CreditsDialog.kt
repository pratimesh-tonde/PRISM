package com.example.ui.components

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
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.PrismAmber
import com.example.ui.theme.PrismCyan
import com.example.ui.theme.PrismEmerald
import com.example.ui.theme.PrismIndigo
import com.example.ui.theme.StudioSurface
import com.example.ui.theme.StudioSurfaceBorder
import com.example.ui.theme.StudioSurfaceElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun CreditsDialog(
    creditsAvailable: Int,
    onAddCredits: (Int) -> Unit,
    onClose: () -> Unit
) {
    Dialog(onDismissRequest = onClose) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = StudioSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder),
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
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(PrismAmber.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Bolt, contentDescription = null, tint = PrismAmber, modifier = Modifier.size(20.dp))
                        }
                        Spacer(Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Prism Credits & Plans",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = TextPrimary
                            )
                            Text(
                                text = "High-performance GPU quota",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                    }

                    IconButton(onClick = onClose) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                    }
                }

                HorizontalDivider(color = StudioSurfaceBorder, modifier = Modifier.padding(vertical = 12.dp))

                // Balance Card
                Surface(
                    color = StudioSurfaceElevated,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("CURRENT BALANCE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = "$creditsAvailable",
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PrismAmber
                                )
                                Text(
                                    text = " credits",
                                    fontSize = 14.sp,
                                    color = TextSecondary,
                                    modifier = Modifier.padding(bottom = 4.dp, start = 4.dp)
                                )
                            }
                        }

                        Surface(
                            color = PrismEmerald.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Active Plan: Pro",
                                color = PrismEmerald,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(14.dp))

                // Top-up Packs
                Text(
                    text = "RECHARGE CREDIT PACKS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Pack 1: Starter Pack
                CreditPackRow(
                    title = "Starter Pack",
                    credits = 50,
                    price = "Free Daily Recharge",
                    isRecommended = false,
                    onBuy = { onAddCredits(50) }
                )

                // Pack 2: Creator Pack (Recommended)
                CreditPackRow(
                    title = "Creator Pack",
                    credits = 250,
                    price = "₹299",
                    isRecommended = true,
                    onBuy = { onAddCredits(250) }
                )

                // Pack 3: Studio Pro Pack
                CreditPackRow(
                    title = "Studio Studio Max",
                    credits = 1000,
                    price = "₹899",
                    isRecommended = false,
                    onBuy = { onAddCredits(1000) }
                )

                Spacer(Modifier.height(14.dp))

                // Tool Cost Breakdown
                Text(
                    text = "GPU COST REFERENCE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted,
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                Surface(
                    color = StudioSurfaceElevated,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        CostItem("1 Credit", "Auto Enhance, Smart Select, Color Grade")
                        CostItem("2 Credits", "BiRefNet BG Remove, Face Retouch, Upscale")
                        CostItem("3 Credits", "Generative Fill, FLUX Replace BG, Restore")
                    }
                }
            }
        }
    }
}

@Composable
private fun CreditPackRow(
    title: String,
    credits: Int,
    price: String,
    isRecommended: Boolean,
    onBuy: () -> Unit
) {
    Surface(
        color = if (isRecommended) PrismIndigo.copy(alpha = 0.15f) else StudioSurfaceElevated,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .border(
                width = if (isRecommended) 1.5.dp else 1.dp,
                color = if (isRecommended) PrismIndigo else StudioSurfaceBorder,
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = title,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    if (isRecommended) {
                        Spacer(Modifier.width(6.dp))
                        Surface(
                            color = PrismIndigo,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "POPULAR",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
                Text(
                    text = "+$credits Credits • $price",
                    fontSize = 11.sp,
                    color = PrismCyan
                )
            }

            Button(
                onClick = onBuy,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isRecommended) PrismIndigo else StudioSurfaceBorder
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(if (price.contains("Free")) "Claim" else "Top Up", fontSize = 11.sp)
            }
        }
    }
}

@Composable
private fun CostItem(cost: String, description: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(cost, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = PrismAmber, modifier = Modifier.width(68.dp))
        Text(description, fontSize = 11.sp, color = TextSecondary)
    }
}
