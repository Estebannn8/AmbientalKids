package com.universidad.ambientalkids.ui.lesson

import androidx.compose.ui.graphics.Color

data class TemaVisual(
    val baseColor: Color,
    val gradientLight: Color,
    val gradientDark: Color,
    val progressColor: Color,
    val background: Int,
    val categoryIcon: Int,
    val CloseIcon: Int,
    val progressBar: Int,
    val categoryIconFeliz: Int,
    val categoryIconTriste: Int
)
