package com.example.ui.screens

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.CropFree
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.OpenWith
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AiToolId
import com.example.model.ShapeType
import com.example.ui.components.AdjustmentsSheet
import com.example.ui.components.AiToolsGrid
import com.example.ui.components.CanvasView
import com.example.ui.components.CreditsDialog
import com.example.ui.components.ExportDialog
import com.example.ui.components.LayersPanel
import com.example.ui.components.PlanExecutionDialog
import com.example.ui.components.PromptBar
import com.example.ui.components.TemplatesDialog
import com.example.ui.theme.PrismAmber
import com.example.ui.theme.PrismCyan
import com.example.ui.theme.PrismIndigo
import com.example.ui.theme.PrismViolet
import com.example.ui.theme.StudioBackground
import com.example.ui.theme.StudioSurface
import com.example.ui.theme.StudioSurfaceBorder
import com.example.ui.theme.StudioSurfaceElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.StudioSheet
import com.example.viewmodel.StudioTool
import com.example.viewmodel.StudioViewModel

@Composable
fun StudioScreen(
    viewModel: StudioViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val currentProject by viewModel.currentProject.collectAsState()
    val activeTool by viewModel.activeTool.collectAsState()
    val activeSheet by viewModel.activeSheet.collectAsState()
    val credits by viewModel.credits.collectAsState()
    val zoomScale by viewModel.zoomScale.collectAsState()
    val panOffset by viewModel.panOffset.collectAsState()
    val brushSize by viewModel.brushSize.collectAsState()
    val brushColor by viewModel.brushColor.collectAsState()
    val showOriginal by viewModel.showOriginal.collectAsState()
    val isExecutingPlan by viewModel.isExecutingPlan.collectAsState()
    val activePlan by viewModel.activePlan.collectAsState()
    val planPreviewBitmap by viewModel.planPreviewBitmap.collectAsState()
    val statusMessage by viewModel.statusMessage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(statusMessage) {
        statusMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearStatusMessage()
        }
    }

    // Android Zero-Permission Photo Picker
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let {
            try {
                val inputStream = context.contentResolver.openInputStream(it)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream?.close()
                bitmap?.let { bmp ->
                    viewModel.createProjectFromImportedBitmap(bmp, "Imported Photo")
                }
            } catch (e: Exception) {
                viewModel.showToast("Failed to load photo")
            }
        }
    }

    // Handle back press if a sheet is open
    BackHandler(enabled = activeSheet != StudioSheet.NONE) {
        viewModel.setActiveSheet(StudioSheet.NONE)
    }

    // Dynamic composite bitmap for canvas rendering
    val compositeBitmap = remember(currentProject) {
        viewModel.getFinalComposite()
    }
    val originalBitmap = remember(currentProject.layers) {
        currentProject.layers.firstOrNull()?.bitmap
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing),
        containerColor = StudioBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            StudioTopBar(
                projectName = currentProject.name,
                credits = credits,
                layerCount = currentProject.layers.size,
                canUndo = viewModel.canUndo(),
                canRedo = viewModel.canRedo(),
                onUndo = { viewModel.undo() },
                onRedo = { viewModel.redo() },
                onOpenCredits = { viewModel.setActiveSheet(StudioSheet.CREDITS) },
                onOpenTemplates = { viewModel.setActiveSheet(StudioSheet.TEMPLATES) },
                onOpenLayers = { viewModel.setActiveSheet(StudioSheet.LAYERS) },
                onOpenExport = { viewModel.setActiveSheet(StudioSheet.EXPORT) }
            )
        },
        bottomBar = {
            Column(modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)) {
                // Quick Tools Bar (Move, Brush, Text, Shape, Adjust, AI Magic)
                StudioToolsBar(
                    activeTool = activeTool,
                    onSelectTool = { tool ->
                        viewModel.setActiveTool(tool)
                        when (tool) {
                            StudioTool.ADJUST -> viewModel.setActiveSheet(StudioSheet.ADJUSTMENTS)
                            StudioTool.AI_MAGIC -> viewModel.setActiveSheet(StudioSheet.AI_TOOLS)
                            StudioTool.TEXT -> viewModel.addTextLayer()
                            StudioTool.SHAPE -> viewModel.addShapeLayer(ShapeType.ROUNDED_RECT)
                            else -> {}
                        }
                    }
                )

                // Prompt Bar for natural language AI editing
                PromptBar(
                    isExecuting = isExecutingPlan,
                    onExecutePrompt = { prompt ->
                        viewModel.submitPromptToOrchestrator(prompt)
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Interactive Canvas
            CanvasView(
                project = currentProject,
                compositeBitmap = compositeBitmap,
                originalBitmap = originalBitmap,
                showOriginal = showOriginal,
                activeTool = activeTool,
                zoomScale = zoomScale,
                panOffset = panOffset,
                brushSize = brushSize,
                brushColor = brushColor,
                onZoomChange = { viewModel.setZoom(it) },
                onPanChange = { viewModel.updatePan(it) },
                onResetTransform = { viewModel.resetCanvasTransform() },
                onToggleShowOriginal = { viewModel.setShowOriginal(it) }
            )

            // Bottom Sheets / Overlays
            AnimatedVisibility(
                visible = activeSheet == StudioSheet.LAYERS,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it }),
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                LayersPanel(
                    project = currentProject,
                    onSelectLayer = { viewModel.selectActiveLayer(it) },
                    onToggleVisibility = { viewModel.toggleLayerVisibility(it) },
                    onToggleLock = { viewModel.toggleLayerLock(it) },
                    onUpdateOpacity = { id, op -> viewModel.updateLayerOpacity(id, op) },
                    onUpdateBlendMode = { id, bm -> viewModel.updateLayerBlendMode(id, bm) },
                    onReorderLayer = { from, to -> viewModel.reorderLayer(from, to) },
                    onDuplicateLayer = { viewModel.duplicateLayer(it) },
                    onDeleteLayer = { viewModel.deleteLayer(it) },
                    onAddTextLayer = { viewModel.addTextLayer() },
                    onAddShapeLayer = { viewModel.addShapeLayer() },
                    onClose = { viewModel.setActiveSheet(StudioSheet.NONE) }
                )
            }

            AnimatedVisibility(
                visible = activeSheet == StudioSheet.AI_TOOLS,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it }),
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                AiToolsGrid(
                    creditsAvailable = credits,
                    onSelectTool = { tool ->
                        viewModel.setActiveSheet(StudioSheet.NONE)
                        viewModel.executeDirectAiTool(tool)
                    },
                    onClose = { viewModel.setActiveSheet(StudioSheet.NONE) }
                )
            }

            AnimatedVisibility(
                visible = activeSheet == StudioSheet.ADJUSTMENTS,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it }),
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                val activeLayer = viewModel.getActiveLayer()
                AdjustmentsSheet(
                    initialAdjustments = activeLayer?.adjustments ?: com.example.model.AdjustmentData(),
                    onApplyAdjustments = { viewModel.applyAdjustmentsToActiveLayer(it) },
                    onClose = { viewModel.setActiveSheet(StudioSheet.NONE) }
                )
            }
        }
    }

    // Dialogs
    if (activeSheet == StudioSheet.CREDITS) {
        CreditsDialog(
            creditsAvailable = credits,
            onAddCredits = { viewModel.addRechargeCredits(it) },
            onClose = { viewModel.setActiveSheet(StudioSheet.NONE) }
        )
    }

    if (activeSheet == StudioSheet.TEMPLATES) {
        TemplatesDialog(
            templates = viewModel.availableTemplates,
            onSelectTemplate = { viewModel.loadTemplate(it) },
            onPickFromGallery = {
                photoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            },
            onClose = { viewModel.setActiveSheet(StudioSheet.NONE) }
        )
    }

    if (activeSheet == StudioSheet.EXPORT) {
        ExportDialog(
            compositeBitmap = compositeBitmap,
            onClose = { viewModel.setActiveSheet(StudioSheet.NONE) },
            onSuccess = { viewModel.showToast(it) }
        )
    }

    if (activeSheet == StudioSheet.PLAN_PREVIEW && activePlan != null) {
        PlanExecutionDialog(
            plan = activePlan!!,
            previewBitmap = planPreviewBitmap,
            isExecuting = isExecutingPlan,
            onApprove = { viewModel.approveOrchestratorResult() },
            onDiscard = { viewModel.discardOrchestratorResult() }
        )
    }
}

@Composable
private fun StudioTopBar(
    projectName: String,
    credits: Int,
    layerCount: Int,
    canUndo: Boolean,
    canRedo: Boolean,
    onUndo: () -> Unit,
    onRedo: () -> Unit,
    onOpenCredits: () -> Unit,
    onOpenTemplates: () -> Unit,
    onOpenLayers: () -> Unit,
    onOpenExport: () -> Unit
) {
    Surface(
        color = StudioSurface,
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // App Branding & Template Switcher
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable(onClick = onOpenTemplates)
            ) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(PrismIndigo),
                    contentAlignment = Alignment.Center
                ) {
                    Text("P", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }

                Spacer(Modifier.width(8.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "PRISM",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color.White
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "AI",
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            color = PrismCyan
                        )
                    }
                    Text(
                        text = projectName,
                        fontSize = 11.sp,
                        color = TextMuted,
                        maxLines = 1
                    )
                }
            }

            // Center Actions: Undo & Redo
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onUndo,
                    enabled = canUndo,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.Undo,
                        contentDescription = "Undo",
                        tint = if (canUndo) Color.White else TextMuted
                    )
                }

                IconButton(
                    onClick = onRedo,
                    enabled = canRedo,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.Redo,
                        contentDescription = "Redo",
                        tint = if (canRedo) Color.White else TextMuted
                    )
                }
            }

            // Right Actions: Credits, Layers Sheet, Export
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Credits Chip
                Surface(
                    color = PrismAmber.copy(alpha = 0.15f),
                    shape = CircleShape,
                    modifier = Modifier.clickable(onClick = onOpenCredits)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Bolt, contentDescription = null, tint = PrismAmber, modifier = Modifier.size(14.dp))
                        Spacer(Modifier.width(2.dp))
                        Text(
                            text = "${credits}c",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrismAmber
                        )
                    }
                }

                // Layers Stack Button with Badge
                IconButton(
                    onClick = onOpenLayers,
                    modifier = Modifier.size(38.dp).testTag("layers_toggle_button")
                ) {
                    BadgedBox(
                        badge = {
                            Badge(
                                containerColor = PrismIndigo,
                                contentColor = Color.White
                            ) {
                                Text("$layerCount", fontSize = 9.sp)
                            }
                        }
                    ) {
                        Icon(Icons.Default.Layers, contentDescription = "Layers", tint = Color.White)
                    }
                }

                // Export Button
                FilledTonalIconButton(
                    onClick = onOpenExport,
                    colors = IconButtonDefaults.filledTonalIconButtonColors(containerColor = PrismIndigo),
                    modifier = Modifier.size(36.dp).testTag("export_button")
                ) {
                    Icon(Icons.Default.Download, contentDescription = "Export", tint = Color.White, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

@Composable
private fun StudioToolsBar(
    activeTool: StudioTool,
    onSelectTool: (StudioTool) -> Unit
) {
    Surface(
        color = StudioSurfaceElevated,
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            ToolButton(
                icon = Icons.Default.OpenWith,
                label = "Move",
                isSelected = activeTool == StudioTool.MOVE,
                onClick = { onSelectTool(StudioTool.MOVE) }
            )

            ToolButton(
                icon = Icons.Default.Brush,
                label = "Brush",
                isSelected = activeTool == StudioTool.BRUSH,
                onClick = { onSelectTool(StudioTool.BRUSH) }
            )

            ToolButton(
                icon = Icons.Default.CropFree,
                label = "Select",
                isSelected = activeTool == StudioTool.SELECT,
                onClick = { onSelectTool(StudioTool.SELECT) }
            )

            ToolButton(
                icon = Icons.Default.TextFields,
                label = "Text",
                isSelected = activeTool == StudioTool.TEXT,
                onClick = { onSelectTool(StudioTool.TEXT) }
            )

            ToolButton(
                icon = Icons.Default.Category,
                label = "Shapes",
                isSelected = activeTool == StudioTool.SHAPE,
                onClick = { onSelectTool(StudioTool.SHAPE) }
            )

            ToolButton(
                icon = Icons.Default.Tune,
                label = "Adjust",
                isSelected = activeTool == StudioTool.ADJUST,
                onClick = { onSelectTool(StudioTool.ADJUST) }
            )

            // Sparkle AI Magic Tools Button
            Surface(
                color = if (activeTool == StudioTool.AI_MAGIC) PrismIndigo else PrismIndigo.copy(alpha = 0.2f),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.clickable { onSelectTool(StudioTool.AI_MAGIC) }.testTag("ai_magic_tools_button")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = if (activeTool == StudioTool.AI_MAGIC) Color.White else PrismCyan,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "12 AI Tools",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (activeTool == StudioTool.AI_MAGIC) Color.White else PrismCyan
                    )
                }
            }
        }
    }
}

@Composable
private fun ToolButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        color = if (isSelected) PrismIndigo.copy(alpha = 0.25f) else Color.Transparent,
        shape = RoundedCornerShape(10.dp),
        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, PrismIndigo) else null,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) PrismCyan else TextSecondary,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = label,
                fontSize = 10.sp,
                color = if (isSelected) Color.White else TextMuted
            )
        }
    }
}
