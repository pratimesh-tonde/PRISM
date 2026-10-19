<div align="center">

<img src="docs/assets/prism-banner.png" alt="Prism Banner" width="100%"/>

# 🎨 PRISM  AI Photo Editing Studio

### Photoshop-Grade Canvas • AI Orchestrator Agent • Non-Destructive Layers

[![Platform](https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)](#)
[![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)](#)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)](#)
[![Material 3](https://img.shields.io/badge/Material%203-Dark-7C3AED?style=for-the-badge&logo=materialdesign&logoColor=white)](#)
[![AI Tools](https://img.shields.io/badge/AI%20Tools-12-00F5FF?style=for-the-badge)](#)
[![Min SDK](https://img.shields.io/badge/Min%20SDK-24-10B981?style=for-the-badge)](#)

**Designed & Developed by** [Pratimesh Tonde](https://github.com/pratimesh-tonde)

[Vision](#-vision--core-concepts) •
[Features](#-feature-suite) •
[AI Tools](#-12-tool-ai-neural-suite) •
[Architecture](#-architecture) •
[Structure](#-project-structure) •
[Getting Started](#-getting-started) •
[Permissions](#-permissions--security)

</div>

---

## 🌟 Vision & Core Concepts

**Prism** is an AI-powered, multi-layer photo editing studio built natively for Android with Kotlin and Jetpack Compose. It bridges professional Photoshop-grade canvas tools (layers, blend modes, masks, manual adjustments) with an intelligent **AI Orchestrator Agent** that decomposes natural language instructions into multi-step generative pipelines.

| | Concept | Description |
|---|---|---|
| 🖌️ | **Prompt + Canvas** | Manual precision tools (brush, selection, transform, text, shapes) and AI neural tools collaborate on one shared canvas. |
| 🧠 | **AI Orchestrator** | Type or speak a prompt, and the LLM planner breaks it into structured pipeline steps. |
| 🧱 | **Non-Destructive Layers** | Every AI edit and adjustment creates a new isolated layer. The original photo is never overwritten. |
| 👀 | **Preview-Before-Apply** | Review each step's before/after output, then commit or discard instantly. |

### 🎯 Example Prompt

```text
"Remove the background, put me on a tropical beach at golden hour, and retouch my face"
```

The orchestrator turns it into this pipeline:

```text
segment_subject()
remove_background()
generate_background("tropical beach")
relight_subject("golden hour")
retouch_face(strength=0.5)
```

---

## ✨ Feature Suite

### 1️⃣ Studio Core (Photoshop-Grade Canvas)

<details open>
<summary><b>Multi-Layer Stack</b></summary>

| Layer Type | Description |
|---|---|
| 🖼️ **Raster / Image** | Cutouts, imports, and AI outputs with custom transform offsets |
| 🎚️ **Adjustment** | Canvas-wide or per-layer dynamic color grading |
| 🔤 **Text** | Custom text, font size, colors, shadows, and alignment |
| 🔷 **Shape** | Rectangles, rounded boxes, circles, badges, and frames |

</details>

- **Blend Modes:** Normal, Multiply, Screen, Overlay, Darken, Lighten, Soft Light, Color Dodge
- **Layer Controls:** Drag to reorder, opacity slider (0-100%), visibility toggle, lock toggle, duplicate, delete
- **Canvas Navigation:** Smooth pinch-to-zoom (20% to 500%), pan gestures, 1-tap Fit to Canvas, press-to-compare with the original
- **History & Revert:** Full multi-step Undo and Redo with state rollbacks

---

## 🤖 12-Tool AI Neural Suite

| # | Tool | Purpose | Model Family | Cost |
|:-:|---|---|---|:-:|
| 1 | **Smart Select** | Click or prompt to isolate objects with a glowing boundary mask | SAM 2 / Grounding DINO | 1 Credit |
| 2 | **Remove Background** | 1-click subject cutout with precision alpha feathering | BiRefNet / RMBG | 2 Credits |
| 3 | **Replace Background** | Instant backdrops (Beach, Cyberpunk, Luxury Marble, Studio White, Sunset) | BiRefNet + SDXL | 3 Credits |
| 4 | **Object Remove** | Magic eraser with content-aware inpainting for photobombers and blemishes | LaMa / Inpaint | 2 Credits |
| 5 | **Generative Fill** | Select a region, type a prompt, add accessories (sunglasses, halos, badges) | FLUX / SDXL Inpaint | 3 Credits |
| 6 | **Generative Expand** | Outpaint and extend canvas boundaries 1.4x with contextual scenery | Outpainting / FLUX | 3 Credits |
| 7 | **Instruction Edit** | Conversational edits ("Make it night", "Add retro sunglasses") | FLUX Kontext / InstructPix2Pix | 3 Credits |
| 8 | **Upscale & Deblur** | 2x / 4x super-resolution with contrast-adaptive edge sharpening | Real-ESRGAN / SwinIR | 2 Credits |
| 9 | **Face Restore** | Neural portrait smoothing, eye clarity, skin tone illumination | CodeFormer / GFPGAN | 2 Credits |
| 10 | **Relight & Shadow** | Directional 3D lighting (Golden Hour, Cyberpunk Neon, Key Light) | Relighting AI | 2 Credits |
| 11 | **Auto Enhance** | 1-tap HDR exposure stretch, curves balance, color grading | Classical CV + AI Presets | 1 Credit |
| 12 | **Restore & Colorize** | Antique photo repair: scratch healing, noise cleaning, sepia colorization | Restoration Net | 3 Credits |

---

## 🎛️ Manual Color Grading

- **Sliders:** Exposure, Brightness, Contrast, Saturation, Warmth / Temperature, Radial Vignette
- **Cinematic LUT Presets:** Cinematic (Teal & Orange), Cyberpunk Neon, Golden Hour, Kodak Portra Vintage, B&W Dramatic Noir, Emerald

## 💳 Credit & Quota System

- **Starting balance:** 50 free credits
- **Dynamic deduction** per tool run, based on model complexity
- **Top-up packs:** Daily Free Recharge, Creator Pack, Studio Pro Pack

## 📤 Export & Sharing

| Option | Choices |
|---|---|
| **Formats** | PNG, JPEG, WEBP |
| **Resolutions** | Original, 2K (1440p), 4K (2160p) |
| **Watermark** | Free tier with watermark, clean export for Pro |

---

## 🏗️ Architecture

```text
┌──────────────────────────────────────────────┐
│   USER INPUT (Text Prompt / Voice / Tools)   │
└──────────────────────┬───────────────────────┘
                       ▼
┌──────────────────────────────────────────────┐
│   StudioViewModel                            │
│   (State, Layers, Undo/Redo, Credits)        │
└──────────────────────┬───────────────────────┘
                       ▼
┌──────────────────────────────────────────────┐
│   AiOrchestrator                             │
│   (Prompt Parser + Step Planner)             │
└──────────────────────┬───────────────────────┘
                       ▼
┌──────────────────────────────────────────────┐
│   Plan Steps → 12-Tool AI Neural Suite       │
│   (Async Execution Runner)                   │
└──────────────────────┬───────────────────────┘
                       ▼
┌──────────────────────────────────────────────┐
│   PlanExecutionDialog                        │
│   Preview Before / After → Commit or Discard │
└──────────────────────┬───────────────────────┘
                       ▼
┌──────────────────────────────────────────────┐
│   New Non-Destructive Layer                  │
│   (Original photo is never overwritten)      │
└──────────────────────┬───────────────────────┘
                       ▼
┌──────────────────────────────────────────────┐
│   ImageProcessor + CanvasView                │
│   (Bitmap Engine, Blend Modes, Rendering)    │
└──────────────────────────────────────────────┘
```

---

## 📁 Project Structure

```text
app/src/main/java/com/example/
├── MainActivity.kt                  # App entry point with Edge-to-Edge setup
├── model/
│   ├── Layer.kt                     # Layer, LayerBlendMode, LayerType, AdjustmentData, TextData, ShapeData
│   ├── AiTool.kt                    # 12 AI tools, categories, credit metadata
│   ├── AiPlan.kt                    # Orchestrator plan step schemas and status lifecycle
│   └── Project.kt                   # Project container and starter templates
├── engine/
│   ├── ImageProcessor.kt            # Bitmap Canvas engine, ColorMatrix, PorterDuff filters
│   └── SampleImages.kt              # Procedural sample generators (Portrait, Watch, Sunset, Vintage)
├── orchestrator/
│   └── AiOrchestrator.kt            # NLP prompt parser, step planner, async tool runner
├── viewmodel/
│   └── StudioViewModel.kt           # State, undo/redo history, layer manipulation, credits
└── ui/
    ├── components/
    │   ├── CanvasView.kt            # Multi-touch zoom/pan canvas, grid, brush cursor
    │   ├── PromptBar.kt             # Gradient prompt bar, voice input, suggestion chips
    │   ├── LayersPanel.kt           # Layers drawer, blend mode picker, opacity slider
    │   ├── AiToolsGrid.kt           # 12-tool modal grid with credit tags
    │   ├── AdjustmentsSheet.kt      # Color grading sliders and preset LUT chips
    │   ├── PlanExecutionDialog.kt   # Multi-step pipeline runner with preview-before-commit
    │   ├── CreditsDialog.kt         # Balance viewer, plan tiers, recharge packs
    │   ├── ExportDialog.kt          # PNG/JPEG/WEBP exporter with resolution presets
    │   └── TemplatesDialog.kt       # Template chooser and Android Photo Picker launcher
    ├── screens/
    │   └── StudioScreen.kt          # Main studio: top bar, tools bar, canvas, sheets
    └── theme/
        ├── Color.kt                 # Obsidian dark palette and Prism accent tokens
        ├── Theme.kt                 # Material 3 dark theme configuration
        └── Type.kt                  # Typography
```

---

## 🚀 Getting Started

### Prerequisites

- Android Studio Ladybug / Meerkat (or newer)
- Android SDK 36 (Minimum SDK: 24)
- JDK 17 or JDK 21

## 📦 Build & Install

```bash
# Clone the repository
git clone https://github.com/pratimesh-tonde/Prism.git
cd Prism

# Build the debug APK
gradle assembleDebug

# Run Robolectric unit tests
gradle :app:testDebugUnitTest
```

Then install and run on an Android device or emulator running **Android 7.0 (API 24) or higher**.

---

## 🔒 Permissions & Security

| Permission | Why it's needed |
|---|---|
| `INTERNET` | AI API communication and remote neural model calls |
| *No storage permissions* | Media selection uses only the official Android Photo Picker (`ActivityResultContracts.PickVisualMedia`), so legacy `READ_EXTERNAL_STORAGE` is not required |

---

## 🧰 Tech Stack

![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=flat-square&logo=kotlin&logoColor=white)
![Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=flat-square&logo=jetpackcompose&logoColor=white)
![Material3](https://img.shields.io/badge/Material%203-757575?style=flat-square&logo=materialdesign&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-02303A?style=flat-square&logo=gradle&logoColor=white)
![Robolectric](https://img.shields.io/badge/Robolectric-Tests-25A162?style=flat-square)

---

## 📸 Screenshots

<div align="center">

| Studio | AI Tools | Layers | Export |
|:---:|:---:|:---:|:---:|
| <img src="docs/assets/studio.png" width="200"/> | <img src="docs/assets/ai-tools.png" width="200"/> | <img src="docs/assets/layers.png" width="200"/> | <img src="docs/assets/export.png" width="200"/> |

</div>

---

## 📄 License & Credits

Designed and architected based on the **PRISM Project Plan (Version 1.0)**.

**Designed & Developed by:** Pratimesh Tonde
**Copyright:** © 2026 PRISM AI Studio. All rights reserved.

<div align="center">

**⭐ If you like Prism, please give the repo a star! ⭐**

Made with ❤️ by **Pratimesh Tonde**

</div>
