package com.universidad.ambientalkids.ui.avatar

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.universidad.ambientalkids.R
import com.universidad.ambientalkids.navigation.AppScreens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AvatarPorcupineScreen(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.ic_porcupine_background),
            contentDescription = "Fondo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )


        // Imagen central "porcupine" con efecto de click
        var porcupineClicked by remember { mutableStateOf(false) }
        val porcupineInteractionSource = remember { MutableInteractionSource() }
        val porcupineScale by animateFloatAsState(
            targetValue = if (porcupineClicked) 0.95f else 1f,
            animationSpec = tween(durationMillis = 200),
            label = "porcupineScale",
            finishedListener = { porcupineClicked = false }
        )

        Image(
            painter = painterResource(id = R.drawable.ic_porcupine),
            contentDescription = "Porcupine",
            modifier = Modifier
                .size(200.dp) // Tamaño modificable (ajusta según necesites)
                .align(Alignment.BottomEnd) // Posiciona en la esquina inferior derecha
                .padding(end = 16.dp, bottom = 16.dp)
                .offset(y = (-45).dp)
                .scale(porcupineScale)
                .clickable(
                    interactionSource = porcupineInteractionSource,
                    indication = null
                ) {
                    porcupineClicked = true
                    // CoroutineScope para manejar el retardo
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(300) // Espera 300ms para que se complete la animación
                        navController.navigate(AppScreens.LandingScreen1.route) {
                            popUpTo(AppScreens.AvatarPorcupineScreen.route) {
                                inclusive = true
                            }
                        }
                        porcupineClicked = false // Resetear el estado después de navegar
                    }
                }, // Padding opcional para separar de los bordes
            contentScale = ContentScale.Fit
        )

        // Flecha izquierda (centrada verticalmente)
        var leftArrowClicked by remember { mutableStateOf(false) }
        val interactionSource = remember { MutableInteractionSource() }

        val leftArrowScale by animateFloatAsState(
            targetValue = if (leftArrowClicked) 1.2f else 1f,
            animationSpec = tween(durationMillis = 200),
            label = "leftArrowScale",
            finishedListener = { leftArrowClicked = false }
        )

        Box(
            modifier = Modifier
                .size(70.dp)
                .align(Alignment.CenterStart)
                .padding(start = 20.dp)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    leftArrowClicked = true
                    navController.navigate(AppScreens.AvatarFrogScreen.route)
                }
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_arrow_left),
                contentDescription = "Flecha izquierda",
                modifier = Modifier
                    .fillMaxSize()
                    .scale(leftArrowScale),
                contentScale = ContentScale.Fit
            )
        }

        // Flecha derecha (centrada verticalmente)
        var rightArrowClicked by remember { mutableStateOf(false) }
        val interactionSource2 = remember { MutableInteractionSource() }

        val rightArrowScale by animateFloatAsState(
            targetValue = if (rightArrowClicked) 1.2f else 1f,
            animationSpec = tween(durationMillis = 200),
            label = "rightArrowScale",
            finishedListener = { rightArrowClicked = false }
        )

        Box(
            modifier = Modifier
                .size(70.dp)
                .align(Alignment.CenterEnd)
                .padding(end = 20.dp)
                .clickable(
                    interactionSource = interactionSource2,
                    indication = null
                ) {
                    rightArrowClicked = true
                    navController.navigate(AppScreens.AvatarBirdScreen.route)
                }
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_arrow_right),
                contentDescription = "Flecha derecha",
                modifier = Modifier
                    .fillMaxSize()
                    .scale(rightArrowScale),
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AvatarPorcupineScreenPreview() {
    // Pasamos un NavController falso para que compile en preview
    AvatarPorcupineScreen(navController = rememberNavController())
}
