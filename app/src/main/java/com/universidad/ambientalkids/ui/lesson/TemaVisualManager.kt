package com.universidad.ambientalkids.ui.lesson

import androidx.compose.ui.graphics.Color
import com.universidad.ambientalkids.R

object TemaVisualManager {

    // Definir los temas para cada categoría
    private val temasPorCategoria = mapOf(
        "reciclaje" to TemaVisual(
            baseColor = Color(0xFF208B56),
            gradientLight = Color(0xFF89C5A7),
            gradientDark = Color(0xFF12643B),
            progressColor = Color(0xDC208B56),
            background = R.drawable.background_reciclaje,
            categoryIcon = R.drawable.zorro_inicio,
            CloseIcon = R.drawable.ic_close_verde,
            progressBar = R.drawable.ic_bar_verde,
            categoryIconFeliz = R.drawable.zorro_feliz,
            categoryIconTriste = R.drawable.zorro_triste
        ),
        "tierra" to TemaVisual(
            baseColor = Color(0xFFED7621),
            gradientLight = Color(0xFFFFB987),
            gradientDark = Color(0xFFDD5500),
            progressColor = Color(0xDCED7621),
            background = R.drawable.background_tierra,
            categoryIcon = R.drawable.conejo_inicio,
            CloseIcon = R.drawable.ic_close_naranja,
            progressBar = R.drawable.ic_bar_naranja,
            categoryIconFeliz = R.drawable.conejo_feliz,
            categoryIconTriste = R.drawable.conejo_triste
        ),
        "biologia" to TemaVisual(
            baseColor = Color(0xFF62D744),
            gradientLight = Color(0xFFBEEDB2),
            gradientDark = Color(0xFF41A429),
            progressColor = Color(0xDC62D744),
            background = R.drawable.background_biologia,
            categoryIcon = R.drawable.rana_inicio,
            CloseIcon = R.drawable.ic_close_lima,
            progressBar = R.drawable.ic_bar_lima,
            categoryIconFeliz = R.drawable.rana_feliz,
            categoryIconTriste = R.drawable.rana_triste
        ),
        "agua" to TemaVisual(
            baseColor = Color(0xFF0270BA),
            gradientLight = Color(0xFF479FDB),
            gradientDark = Color(0xFF024F84),
            progressColor = Color(0xDC0270BA),
            background = R.drawable.background_agua,
            categoryIcon = R.drawable.gota_inicio,
            CloseIcon = R.drawable.ic_close_azul,
            progressBar = R.drawable.ic_bar_azul,
            categoryIconFeliz = R.drawable.gota_feliz,
            categoryIconTriste = R.drawable.gota_triste
        )
    )

    // Obtener el tema visual para una categoría dada
    fun obtenerTemaPorCategoria(categoria: String): TemaVisual? {
        return temasPorCategoria[categoria]
    }

}
