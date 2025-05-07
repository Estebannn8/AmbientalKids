package com.universidad.ambientalkids.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.universidad.ambientalkids.R

@Composable
fun BottomMenu(
    isHomeSection: Boolean,  // Nuevo parámetro para distinguir Home de otras pantallas
    sectionColor: String,     // Color de la sección (solo se usa en Home)
    menuBackgroundColor: Color,
    selectedItem: String,
    onItemSelected: (String) -> Unit
) {

    val navIconNames = listOf("perfil", "inicio", "tienda")

    val iconMap = mapOf(
        "lima_perfil" to R.drawable.ic_lima_perfil,
        "lima_inicio" to R.drawable.ic_lima_inicio,
        "lima_tienda" to R.drawable.ic_lima_tienda,

        "azul_perfil" to R.drawable.ic_azul_perfil,
        "azul_inicio" to R.drawable.ic_azul_inicio,
        "azul_tienda" to R.drawable.ic_azul_tienda,

        "naranja_perfil" to R.drawable.ic_naranja_perfil,
        "naranja_inicio" to R.drawable.ic_naranja_inicio,
        "naranja_tienda" to R.drawable.ic_naranja_tienda,

        "verde_perfil" to R.drawable.ic_verde_perfil,
        "verde_inicio" to R.drawable.ic_verde_inicio,
        "verde_tienda" to R.drawable.ic_verde_tienda,

        "gris_perfil" to R.drawable.ic_gris_perfil,
        "gris_inicio" to R.drawable.ic_gris_inicio,
        "gris_tienda" to R.drawable.ic_gris_tienda
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .background(
                color = menuBackgroundColor,
                shape = RoundedCornerShape(24.dp)
            )
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly, // Cambiado a SpaceEvenly para mejor distribución
            verticalAlignment = Alignment.CenterVertically
        ) {
            navIconNames.forEach { iconName ->
                // Lógica mejorada para determinar el color
                val colorKey = when {
                    // En Home, usa el color de la sección para todos los íconos
                    isHomeSection && selectedItem == "inicio" -> sectionColor
                    // En otras pantallas, usa colores fijos según el ícono
                    else -> getStaticColorForIcon(iconName)
                }

                val key = "${colorKey}_$iconName"
                val iconResId = iconMap[key] ?: R.drawable.placeholder

                val isSelected = selectedItem == iconName

                val animatedSize by animateDpAsState(
                    targetValue = if (isSelected) 56.dp else 42.dp,
                    label = "IconSizeAnimation"
                )

                Box(
                    modifier = Modifier
                        .height(56.dp)
                ) {
                    IconButton(
                        onClick = { onItemSelected(iconName) },
                        modifier = Modifier
                            .size(animatedSize)
                            .align(Alignment.Center)
                    ) {
                        Image(
                            painter = painterResource(id = iconResId),
                            contentDescription = iconName,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit
                        )
                    }
                }
            }
        }
    }
}

// Función actualizada para los iconos restantes
fun getStaticColorForIcon(iconName: String): String {
    return when (iconName) {
        "perfil" -> "gris"
        "inicio" -> "gris"
        "tienda" -> "gris"
        else -> "azul"
    }
}