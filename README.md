# PRISM  AI Photo Editing Studio


**Prism** is an AI-powered, multi-layer photo editing studio built for Android using Kotlin and Jetpack Compose. It bridges professional Photoshop-grade canvas tools (layers, blend modes, masks, manual adjustments) with an intelligent **AI Orchestrator Agent** that decomposes natural language instructions into multi-step generative pipelines.

---

## 🌟 Vision & Core Concepts

- **Prompt + Canvas**: Manual precision tools (brush, selection, transform, text, shapes) and AI neural tools seamlessly collaborate on a shared infinite canvas.
- **AI Orchestrator (Agent Planner)**: Type or speak a prompt like:
  
  The LLM orchestrator breaks it into structured pipeline execution steps:
  1. `segment_subject()`
  2. `remove_background()`
  3. `generate_background("tropical beach")`
  4. `relight_subject("golden hour")`
  5. `retouch_face(strength=0.5)`
- **Non-Destructive Layers**: Every AI edit and adjustment creates a new isolated layer. The original photo is never overwritten.
- **Preview-Before-Apply**: Users review step-by-step before/after outputs with instant commit or discard control.

---

## 🛠️ Complete Feature Suite

### 1. Studio Core (Photoshop-Grade Canvas)
- **Multi-Layer Stack**:
  - **Raster / Image Layers**: Cutouts, imports, AI outputs with custom transform offsets.
  - **Adjustment Layers**: Canvas-wide or per-layer dynamic color grading.
  - **Text Layers**: Customizable text, font sizing, colors, shadows, and alignment.
  - **Shape Layers**: Rectangles, rounded boxes, circles, badges, and frames.
- **Blend Modes**: *Normal, Multiply, Screen, Overlay, Darken, Lighten, Soft Light, Color Dodge*.
- **Layer Controls**: Drag/reorder stack, opacity slider (0–100%), visibility toggle, lock toggle, duplicate, and delete.
- **Canvas Navigation**: Smooth pinch-to-zoom (20% to 500%), pan gestures, 1-tap *Fit to Canvas*, and press-to-compare with original.
- **History & Revert**: Full multi-step Undo and Redo stack with state rollbacks.

### 2. 12-Tool AI Neural Suite
| # | Tool | Purpose | Model Family | Cost |
|---|---|---|---|---|
| 1 | **Smart Select** | Click or prompt to isolate objects with glowing boundary mask | SAM 2 / Grounding DINO | 1 Credit |
| 2 | **Remove Background** | 1-click subject cutout with precision alpha feathering | BiRefNet / RMBG | 2 Credits |
| 3 | **Replace Background** | Instant backdrop replacement (*Beach, Cyberpunk, Luxury Marble, Studio White, Sunset*) | BiRefNet + SDXL | 3 Credits |
| 4 | **Object Remove** | Magic eraser with content-aware inpainting for photobombers and blemishes | LaMa / Inpaint | 2 Credits |
| 5 | **Generative Fill** | Select region, type prompt, composite accessories (sunglasses, halos, badges) | FLUX / SDXL Inpaint | 3 Credits |
| 6 | **Generative Expand** | Outpaint and extend canvas boundaries 1.4x with contextual scenery | Outpainting / FLUX | 3 Credits |
| 7 | **Instruction Edit** | Conversational transformation (*"Make it night", "Add retro sunglasses"*) | FLUX Kontext / InstructPix2Pix | 3 Credits |
| 8 | **Upscale & Deblur** | 2x / 4x super-resolution with contrast-adaptive edge sharpening | Real-ESRGAN / SwinIR | 2 Credits |
| 9 | **Face Restore** | Neural portrait smoothing, eye clarity, and skin tone illumination | CodeFormer / GFPGAN | 2 Credits |
| 10 | **Relight & Shadow** | Directional 3D lighting simulation (*Golden Hour, Cyberpunk Neon, Key Light*) | Relighting AI | 2 Credits |
| 11 | **Auto Enhance** | 1-tap HDR exposure stretch, curves balance, and color grading | Classical CV + AI Presets | 1 Credit |
| 12 | **Restore & Colorize** | Antique photo repair: scratch healing, noise cleaning, sepia colorization | Restoration Net | 3 Credits |

### 3. Manual Color Grading & Adjustment Sliders
- **Controls**: Exposure, Brightness, Contrast, Saturation, Warmth / Temperature, and Radial Vignette.
- **Cinematic LUT Presets**: *Cinematic (Teal & Orange), Cyberpunk Neon, Golden Hour, Kodak Portra Vintage, B&W Dramatic Noir, Emerald*.

### 4. Credit & Quota System
- Initial account balance: **50 free credits**.
- Dynamic deduction per tool execution based on model complexity.
- Top-up packs (*Daily Free Recharge, Creator Pack, Studio Pro Pack*).

### 5. Export & Sharing
- Multi-format output: **PNG**, **JPEG**, and **WEBP**.
- Target resolutions: **Original**, **2K (1440p)**, and **4K (2160p)**.
- Watermark toggle (*Free tier with watermark, clean Pro export*).

---

## 🏗️ Architecture & Codebase Structure

```
app/src/main/java/com/example/
├── MainActivity.kt                     # Application entry point with Edge-to-Edge setup
├── model/
│   ├── Layer.kt                        # Layer, LayerBlendMode, LayerType, AdjustmentData, TextData, ShapeData
│   ├── AiTool.kt                       # 12 AI Tools definition, categories, and credit metadata
│   ├── AiPlan.kt                       # Orchestrator plan step schemas and status lifecycle
│   └── Project.kt                      # Project container and preset starter templates
├── engine/
│   ├── ImageProcessor.kt               # High-performance Bitmap Canvas engine, ColorMatrix, PorterDuff filters
│   └── SampleImages.kt                 # Procedural studio photography generators (Portrait, Watch, Sunset, Vintage)
├── orchestrator/
│   └── AiOrchestrator.kt               # NLP Prompt parser, step planner, and async tool execution runner
├── viewmodel/
│   └── StudioViewModel.kt              # State management, undo/redo history stack, layer manipulation, credits
└── ui/
    ├── components/
    │   ├── CanvasView.kt               # Multi-touch zoom/pan canvas, grid, and brush cursor
    │   ├── PromptBar.kt                # Gradient prompt bar, voice input, and quick suggestion chips
    │   ├── LayersPanel.kt              # Photoshop-style layers drawer, blend mode picker, opacity slider
    │   ├── AiToolsGrid.kt              # 12-tool modal grid with credit tags
    │   ├── AdjustmentsSheet.kt         # Color grading sliders and preset LUT chips
    │   ├── PlanExecutionDialog.kt      # Visual multi-step pipeline runner with preview-before-commit
    │   ├── CreditsDialog.kt            # Balance viewer, plan tiers, and recharge packs
    │   ├── ExportDialog.kt             # PNG/JPEG/WEBP exporter with resolution presets
    │   └── TemplatesDialog.kt          # Template chooser & Android Photo Picker launcher
    ├── screens/
    │   └── StudioScreen.kt             # Main studio orchestrating top bar, tools bar, canvas, and sheets
    └── theme/
        ├── Color.kt                    # Studio obsidian dark palette and Prism accent tokens
        ├── Theme.kt                    # Central Material 3 Dark theme configuration
        └── Type.kt                     # Typography definitions
```

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Ladybug / Meerkat (or newer)
- Android SDK 36 (Minimum SDK: 24)
- JDK 17 or JDK 21

### Building & Running
1. Clone the repository or open the project directory in Android Studio.
2. Ensure dependencies are resolved via Gradle:
   ```bash
   gradle assembleDebug
   ```
3. To run Robolectric unit tests:
   ```bash
   gradle :app:testDebugUnitTest
   ```
4. Install and run on an Android device or emulator running Android 7.0 (API 24) or higher.

---

## 🔒 Permissions & Security
- **Internet**: Declared in `AndroidManifest.xml` for AI API communication and remote neural model calls.
- **Zero-Storage Permissions**: Media selection strictly uses the official Android Photo Picker (`ActivityResultContracts.PickVisualMedia`), eliminating legacy `READ_EXTERNAL_STORAGE` requirements.

---

## 📄 License & Credits
Designed and architected based on the **PRISM Project Plan (Version 1.0)
Designed and Developed by Pratimesh Tonde
All rights reserved © 2026 PRISM AI Studio.
