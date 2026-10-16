package com.example.model

enum class AiToolId(
    val title: String,
    val subtitle: String,
    val modelFamily: String,
    val creditCost: Int,
    val category: String
) {
    SMART_SELECT(
        title = "Smart Select",
        subtitle = "Click or text to isolate elements",
        modelFamily = "SAM 2 / Grounding DINO",
        creditCost = 1,
        category = "Select"
    ),
    REMOVE_BG(
        title = "Remove Background",
        subtitle = "1-click cutout with precision alpha mask",
        modelFamily = "BiRefNet / RMBG",
        creditCost = 2,
        category = "Cutout"
    ),
    REPLACE_BG(
        title = "Replace Background",
        subtitle = "Swap backdrop with AI studio or beach",
        modelFamily = "BiRefNet + SDXL",
        creditCost = 3,
        category = "Cutout"
    ),
    OBJECT_REMOVE(
        title = "Object Remove",
        subtitle = "Erase unwanted people, items, blemishes",
        modelFamily = "LaMa / Inpaint",
        creditCost = 2,
        category = "Generative"
    ),
    GENERATIVE_FILL(
        title = "Generative Fill",
        subtitle = "Select area, type prompt, add content",
        modelFamily = "FLUX / SDXL Inpaint",
        creditCost = 3,
        category = "Generative"
    ),
    GENERATIVE_EXPAND(
        title = "Generative Expand",
        subtitle = "Outpaint canvas with seamless context",
        modelFamily = "Outpainting / FLUX",
        creditCost = 3,
        category = "Generative"
    ),
    INSTRUCTION_EDIT(
        title = "Instruction Edit",
        subtitle = "Say 'Add glasses' or 'Make it night'",
        modelFamily = "FLUX Kontext / InstructPix2Pix",
        creditCost = 3,
        category = "Creative"
    ),
    UPSCALE(
        title = "Upscale & Deblur",
        subtitle = "2x / 4x super-resolution & unsharp mask",
        modelFamily = "Real-ESRGAN / SwinIR",
        creditCost = 2,
        category = "Enhance"
    ),
    FACE_RETOUCH(
        title = "Face Restore",
        subtitle = "Skin smoothing, portrait pop & eye clarity",
        modelFamily = "CodeFormer / GFPGAN",
        creditCost = 2,
        category = "Enhance"
    ),
    RELIGHT(
        title = "Relight & Shadow",
        subtitle = "Match studio lighting, directional rims",
        modelFamily = "Relighting AI",
        creditCost = 2,
        category = "Creative"
    ),
    AUTO_ENHANCE(
        title = "Auto Enhance",
        subtitle = "1-click HDR exposure, curves & color grade",
        modelFamily = "Classical CV + AI Presets",
        creditCost = 1,
        category = "Enhance"
    ),
    OLD_PHOTO_RESTORE(
        title = "Restore & Colorize",
        subtitle = "Fix scratches, creases & colorize sepia",
        modelFamily = "Restoration Net",
        creditCost = 3,
        category = "Enhance"
    ),
    TEXT_TO_IMAGE(
        title = "Text to Image Layer",
        subtitle = "Generate assets straight onto new layer",
        modelFamily = "FLUX.1 / SDXL",
        creditCost = 3,
        category = "Generative"
    )
}
