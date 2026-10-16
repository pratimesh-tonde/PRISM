package com.example.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.graphics.Rect
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.engine.ImageProcessor
import com.example.engine.SampleImages
import com.example.model.AdjustmentData
import com.example.model.AiPlan
import com.example.model.AiPlanStep
import com.example.model.AiToolId
import com.example.model.Layer
import com.example.model.LayerBlendMode
import com.example.model.LayerType
import com.example.model.PresetTemplate
import com.example.model.Project
import com.example.model.ShapeData
import com.example.model.ShapeType
import com.example.model.StepStatus
import com.example.model.TextData
import com.example.orchestrator.AiOrchestrator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.max
import java.util.UUID

enum class StudioTool {
    MOVE,
    BRUSH,
    ERASER,
    SELECT,
    TEXT,
    SHAPE,
    ADJUST,
    AI_MAGIC
}

enum class StudioSheet {
    NONE,
    LAYERS,
    AI_TOOLS,
    ADJUSTMENTS,
    EXPORT,
    CREDITS,
    TEMPLATES,
    PLAN_PREVIEW
}

class StudioViewModel(application: Application) : AndroidViewModel(application) {

    // Projects & Canvas
    private val _currentProject = MutableStateFlow(createInitialProject())
    val currentProject: StateFlow<Project> = _currentProject.asStateFlow()

    private val _projectsList = MutableStateFlow<List<Project>>(emptyList())
    val projectsList: StateFlow<List<Project>> = _projectsList.asStateFlow()

    // Undo / Redo History Stack
    private val historyStack = mutableListOf<List<Layer>>()
    private var historyIndex = -1

    // Canvas Transform
    private val _zoomScale = MutableStateFlow(1f)
    val zoomScale: StateFlow<Float> = _zoomScale.asStateFlow()

    private val _panOffset = MutableStateFlow(Offset.Zero)
    val panOffset: StateFlow<Offset> = _panOffset.asStateFlow()

    // Active Tool & Mode
    private val _activeTool = MutableStateFlow(StudioTool.MOVE)
    val activeTool: StateFlow<StudioTool> = _activeTool.asStateFlow()

    private val _activeSheet = MutableStateFlow(StudioSheet.NONE)
    val activeSheet: StateFlow<StudioSheet> = _activeSheet.asStateFlow()

    // Credits System (Starts at 50, top-up available)
    private val _credits = MutableStateFlow(50)
    val credits: StateFlow<Int> = _credits.asStateFlow()

    // AI Orchestrator Execution State
    private val _activePlan = MutableStateFlow<AiPlan?>(null)
    val activePlan: StateFlow<AiPlan?> = _activePlan.asStateFlow()

    private val _isExecutingPlan = MutableStateFlow(false)
    val isExecutingPlan: StateFlow<Boolean> = _isExecutingPlan.asStateFlow()

    private val _planPreviewBitmap = MutableStateFlow<Bitmap?>(null)
    val planPreviewBitmap: StateFlow<Bitmap?> = _planPreviewBitmap.asStateFlow()

    // Compare Mode (Press & hold to see original)
    private val _showOriginal = MutableStateFlow(false)
    val showOriginal: StateFlow<Boolean> = _showOriginal.asStateFlow()

    // Brush settings
    private val _brushSize = MutableStateFlow(24f)
    val brushSize: StateFlow<Float> = _brushSize.asStateFlow()

    private val _brushColor = MutableStateFlow(Color.White)
    val brushColor: StateFlow<Color> = _brushColor.asStateFlow()

    // Message / Toast banner
    private val _statusMessage = MutableStateFlow<String?>(null)
    val statusMessage: StateFlow<String?> = _statusMessage.asStateFlow()

    val availableTemplates = listOf(
        PresetTemplate("tpl_portrait", "Portrait Studio Retouch", "Portrait", "High-res portrait with studio lighting", 800, 1000, "Portrait"),
        PresetTemplate("tpl_product", "E-Commerce Luxury Watch", "Product", "Clean reflective surface product photography", 900, 900, "Commercial"),
        PresetTemplate("tpl_landscape", "Sunset Mountain Lake", "Landscape", "Golden hour landscape with atmospheric lake", 1000, 800, "Nature"),
        PresetTemplate("tpl_vintage", "Vintage Restoration", "Restoration", "Antique photo with scratches and sepia tint", 800, 1000, "Restoration")
    )

    init {
        // Record initial history
        recordHistory()
    }

    private fun createInitialProject(): Project {
        val initialBitmap = SampleImages.createPortraitBitmap()
        val bgLayer = Layer(
            id = "layer_base",
            name = "Background Image",
            type = LayerType.IMAGE,
            bitmap = initialBitmap,
            isVisible = true
        )
        return Project(
            name = "Portrait Project 1",
            width = initialBitmap.width,
            height = initialBitmap.height,
            layers = listOf(bgLayer),
            activeLayerId = bgLayer.id
        )
    }

    private fun recordHistory() {
        val currentLayers = _currentProject.value.layers
        // Truncate forward history if we were in the middle
        if (historyIndex < historyStack.size - 1) {
            val toRemove = historyStack.size - 1 - historyIndex
            repeat(toRemove) { historyStack.removeLast() }
        }
        historyStack.add(currentLayers)
        historyIndex = historyStack.size - 1
    }

    fun canUndo(): Boolean = historyIndex > 0
    fun canRedo(): Boolean = historyIndex < historyStack.size - 1

    fun undo() {
        if (canUndo()) {
            historyIndex--
            val restored = historyStack[historyIndex]
            _currentProject.update { it.copy(layers = restored) }
            showToast("Undo performed")
        }
    }

    fun redo() {
        if (canRedo()) {
            historyIndex++
            val restored = historyStack[historyIndex]
            _currentProject.update { it.copy(layers = restored) }
            showToast("Redo performed")
        }
    }

    fun setActiveTool(tool: StudioTool) {
        _activeTool.value = tool
    }

    fun setActiveSheet(sheet: StudioSheet) {
        _activeSheet.value = sheet
    }

    fun setZoom(scale: Float) {
        _zoomScale.value = scale.coerceIn(0.2f, 5.0f)
    }

    fun resetCanvasTransform() {
        _zoomScale.value = 1f
        _panOffset.value = Offset.Zero
    }

    fun updatePan(drag: Offset) {
        _panOffset.update { it + drag }
    }

    fun setShowOriginal(show: Boolean) {
        _showOriginal.value = show
    }

    fun setBrushSize(size: Float) {
        _brushSize.value = size
    }

    fun setBrushColor(color: Color) {
        _brushColor.value = color
    }

    fun clearStatusMessage() {
        _statusMessage.value = null
    }

    fun showToast(msg: String) {
        _statusMessage.value = msg
    }

    // --- Layer Operations ---

    fun selectActiveLayer(id: String) {
        _currentProject.update { it.copy(activeLayerId = id) }
    }

    fun getActiveLayer(): Layer? {
        val id = _currentProject.value.activeLayerId ?: return _currentProject.value.layers.lastOrNull()
        return _currentProject.value.layers.find { it.id == id }
    }

    fun toggleLayerVisibility(id: String) {
        _currentProject.update { p ->
            p.copy(
                layers = p.layers.map {
                    if (it.id == id) it.copy(isVisible = !it.isVisible) else it
                }
            )
        }
        recordHistory()
    }

    fun toggleLayerLock(id: String) {
        _currentProject.update { p ->
            p.copy(
                layers = p.layers.map {
                    if (it.id == id) it.copy(isLocked = !it.isLocked) else it
                }
            )
        }
        recordHistory()
    }

    fun updateLayerOpacity(id: String, opacity: Float) {
        _currentProject.update { p ->
            p.copy(
                layers = p.layers.map {
                    if (it.id == id) it.copy(opacity = opacity.coerceIn(0f, 1f)) else it
                }
            )
        }
    }

    fun updateLayerBlendMode(id: String, blendMode: LayerBlendMode) {
        _currentProject.update { p ->
            p.copy(
                layers = p.layers.map {
                    if (it.id == id) it.copy(blendMode = blendMode) else it
                }
            )
        }
        recordHistory()
    }

    fun reorderLayer(from: Int, to: Int) {
        val list = _currentProject.value.layers.toMutableList()
        if (from in list.indices && to in list.indices) {
            val item = list.removeAt(from)
            list.add(to, item)
            _currentProject.update { it.copy(layers = list) }
            recordHistory()
        }
    }

    fun duplicateLayer(id: String) {
        val source = _currentProject.value.layers.find { it.id == id } ?: return
        val dup = source.copy(
            id = UUID.randomUUID().toString(),
            name = "${source.name} Copy",
            x = source.x + 20f,
            y = source.y + 20f
        )
        val list = _currentProject.value.layers.toMutableList()
        val index = list.indexOfFirst { it.id == id }
        list.add(index + 1, dup)
        _currentProject.update { it.copy(layers = list, activeLayerId = dup.id) }
        recordHistory()
        showToast("Layer duplicated")
    }

    fun deleteLayer(id: String) {
        val list = _currentProject.value.layers.toMutableList()
        if (list.size <= 1) {
            showToast("Cannot delete the only layer")
            return
        }
        list.removeAll { it.id == id }
        _currentProject.update { it.copy(layers = list, activeLayerId = list.lastOrNull()?.id) }
        recordHistory()
        showToast("Layer deleted")
    }

    fun addTextLayer(text: String = "PRISM STUDIO") {
        val newLayer = Layer(
            id = UUID.randomUUID().toString(),
            name = "Text: $text",
            type = LayerType.TEXT,
            textData = TextData(text = text),
            isVisible = true
        )
        _currentProject.update { p ->
            p.copy(layers = p.layers + newLayer, activeLayerId = newLayer.id)
        }
        recordHistory()
        showToast("Text layer added")
    }

    fun addShapeLayer(shapeType: ShapeType = ShapeType.ROUNDED_RECT) {
        val newLayer = Layer(
            id = UUID.randomUUID().toString(),
            name = "Shape: ${shapeType.label}",
            type = LayerType.SHAPE,
            shapeData = ShapeData(type = shapeType),
            isVisible = true
        )
        _currentProject.update { p ->
            p.copy(layers = p.layers + newLayer, activeLayerId = newLayer.id)
        }
        recordHistory()
        showToast("Shape layer added")
    }

    fun applyAdjustmentsToActiveLayer(adj: AdjustmentData) {
        val activeId = _currentProject.value.activeLayerId ?: return
        _currentProject.update { p ->
            p.copy(
                layers = p.layers.map {
                    if (it.id == activeId) it.copy(adjustments = adj) else it
                }
            )
        }
        recordHistory()
    }

    // --- Direct AI Tools Execution ---

    fun executeDirectAiTool(tool: AiToolId, paramString: String = "") {
        if (_credits.value < tool.creditCost) {
            _activeSheet.value = StudioSheet.CREDITS
            showToast("Low credits! Please recharge.")
            return
        }

        val activeLayer = getActiveLayer()
        val baseBitmap = activeLayer?.bitmap ?: SampleImages.createPortraitBitmap()

        viewModelScope.launch {
            _isExecutingPlan.value = true
            showToast("Running ${tool.title} (${tool.modelFamily})...")

            val step = AiPlanStep(
                toolId = tool,
                functionName = tool.name.lowercase(),
                title = tool.title,
                description = tool.subtitle,
                params = if (paramString.isNotEmpty()) mapOf("scene" to paramString, "prompt" to paramString) else emptyMap()
            )

            val output = AiOrchestrator.executeStep(step, baseBitmap)

            // Deduct credits
            _credits.update { max(0, it - tool.creditCost) }

            // Non-destructive: add as new layer
            val newLayer = Layer(
                id = UUID.randomUUID().toString(),
                name = "${tool.title} Edit",
                type = LayerType.IMAGE,
                bitmap = output,
                isVisible = true
            )

            _currentProject.update { p ->
                p.copy(layers = p.layers + newLayer, activeLayerId = newLayer.id)
            }
            recordHistory()
            _isExecutingPlan.value = false
            showToast("${tool.title} completed (+1 layer created)")
        }
    }

    // --- AI Orchestrator: Prompt to Multi-Step Plan ---

    fun submitPromptToOrchestrator(prompt: String) {
        val plan = AiOrchestrator.createPlan(prompt)
        val totalCost = plan.steps.sumOf { it.toolId.creditCost }

        if (_credits.value < totalCost) {
            _activeSheet.value = StudioSheet.CREDITS
            showToast("Required $totalCost credits. You have ${_credits.value}.")
            return
        }

        _activePlan.value = plan
        _activeSheet.value = StudioSheet.PLAN_PREVIEW
        runPlanExecution()
    }

    private fun runPlanExecution() {
        val plan = _activePlan.value ?: return
        viewModelScope.launch {
            _isExecutingPlan.value = true
            var currentBitmap = getActiveLayer()?.bitmap ?: SampleImages.createPortraitBitmap()

            val updatedSteps = plan.steps.map { it.copy() }.toMutableList()

            for (i in updatedSteps.indices) {
                // Mark step running
                updatedSteps[i] = updatedSteps[i].copy(status = StepStatus.RUNNING)
                _activePlan.value = plan.copy(steps = updatedSteps, currentIndex = i)

                val stepResult = AiOrchestrator.executeStep(updatedSteps[i], currentBitmap)
                currentBitmap = stepResult

                // Mark step completed
                updatedSteps[i] = updatedSteps[i].copy(
                    status = StepStatus.COMPLETED,
                    generatedBitmap = stepResult
                )
                _activePlan.value = plan.copy(steps = updatedSteps, currentIndex = i + 1)
            }

            // Deduct credits
            val totalCost = plan.steps.sumOf { it.toolId.creditCost }
            _credits.update { max(0, it - totalCost) }

            _planPreviewBitmap.value = currentBitmap
            _activePlan.value = plan.copy(
                steps = updatedSteps,
                isCompleted = true,
                finalPreviewBitmap = currentBitmap
            )
            _isExecutingPlan.value = false
        }
    }

    fun approveOrchestratorResult() {
        val preview = _planPreviewBitmap.value ?: return
        val plan = _activePlan.value
        val layerName = plan?.originalPrompt?.take(24) ?: "AI Orchestrator Edit"

        val newLayer = Layer(
            id = UUID.randomUUID().toString(),
            name = "AI: $layerName",
            type = LayerType.IMAGE,
            bitmap = preview,
            isVisible = true
        )

        _currentProject.update { p ->
            p.copy(layers = p.layers + newLayer, activeLayerId = newLayer.id)
        }
        recordHistory()

        _activePlan.value = null
        _planPreviewBitmap.value = null
        _activeSheet.value = StudioSheet.NONE
        showToast("AI Edit approved and added as new layer!")
    }

    fun discardOrchestratorResult() {
        _activePlan.value = null
        _planPreviewBitmap.value = null
        _activeSheet.value = StudioSheet.NONE
        showToast("AI Edit discarded")
    }

    // --- Templates & Imports ---

    fun loadTemplate(templateId: String) {
        val bmp = when (templateId) {
            "tpl_product" -> SampleImages.createProductBitmap()
            "tpl_landscape" -> SampleImages.createLandscapeBitmap()
            "tpl_vintage" -> SampleImages.createVintagePhotoBitmap()
            else -> SampleImages.createPortraitBitmap()
        }

        val name = availableTemplates.find { it.id == templateId }?.title ?: "New Project"
        val layer = Layer(
            id = UUID.randomUUID().toString(),
            name = "Background Image",
            type = LayerType.IMAGE,
            bitmap = bmp,
            isVisible = true
        )

        _currentProject.value = Project(
            name = name,
            width = bmp.width,
            height = bmp.height,
            layers = listOf(layer),
            activeLayerId = layer.id
        )
        historyStack.clear()
        historyIndex = -1
        recordHistory()
        resetCanvasTransform()
        _activeSheet.value = StudioSheet.NONE
        showToast("Loaded $name")
    }

    fun createProjectFromImportedBitmap(bitmap: Bitmap, name: String = "Imported Photo") {
        val layer = Layer(
            id = UUID.randomUUID().toString(),
            name = "Original Photo",
            type = LayerType.IMAGE,
            bitmap = bitmap,
            isVisible = true
        )
        _currentProject.value = Project(
            name = name,
            width = bitmap.width,
            height = bitmap.height,
            layers = listOf(layer),
            activeLayerId = layer.id
        )
        historyStack.clear()
        historyIndex = -1
        recordHistory()
        resetCanvasTransform()
        _activeSheet.value = StudioSheet.NONE
        showToast("Photo loaded into Studio")
    }

    fun addRechargeCredits(amount: Int) {
        _credits.update { it + amount }
        showToast("+$amount credits added to your account!")
    }

    fun getFinalComposite(): Bitmap {
        return ImageProcessor.renderProjectComposite(_currentProject.value)
    }
}
