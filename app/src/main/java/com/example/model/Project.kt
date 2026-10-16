package com.example.model

import android.graphics.Bitmap
import java.util.UUID

data class Project(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val width: Int = 1080,
    val height: Int = 1080,
    val layers: List<Layer> = emptyList(),
    val activeLayerId: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val thumbnailBitmap: Bitmap? = null
)

data class PresetTemplate(
    val id: String,
    val title: String,
    val category: String,
    val description: String,
    val width: Int,
    val height: Int,
    val tag: String
)
