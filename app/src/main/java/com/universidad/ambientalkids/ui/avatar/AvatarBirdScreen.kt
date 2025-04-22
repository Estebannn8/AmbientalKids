package com.universidad.ambientalkids.ui.avatar

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
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
fun AvatarBirdScreen(navController: NavController){
    Box(modifier = Modifier.fillMaxSize()) {

        // Imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.ic_bird_background),
            contentDescription = "Fondo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center, // Centra verticalmente los elementos
            horizontalAlignment = Alignment.CenterHorizontally // Centra horizontalmente los elementos
        ) {

            // Imagen central "bird" con efecto de click
            var birdClicked by remember { mutableStateOf(false) }
            val birdInteractionSource = remember { MutableInteractionSource() }
            val birdScale by animateFloatAsState(
                targetValue = if (birdClicked) 0.95f else 1f,
                animationSpec = tween(durationMillis = 200),
                label = "birdScale",
                finishedListener = { birdClicked = false }
            )

            Image(
                painter = painterResource(id = R.drawable.ic_bird),
                contentDescription = "bird",
                modifier = Modifier
                    .sizeIn(maxWidth = 300.dp, maxHeight = 300.dp) // Ajusta estos valores según necesites
                    .padding(16.dp)
                    .offset(x = (-20).dp)
                    .scale(birdScale)
                    .clickable(
                        interactionSource = birdInteractionSource,
                        indication = null
                    ) {
                        birdClicked = true
                        // CoroutineScope para manejar el retardo
                        CoroutineScope(Dispatchers.Main).launch {
                            delay(300) // Espera 300ms para que se complete la animación
                            navController.navigate(AppScreens.LandingScreen1.route) {
                                popUpTo(AppScreens.AvatarBirdScreen.route) {
                                    inclusive = true
                                }
                            }
                            birdClicked = false // Resetear el estado después de navegar
                        }
                    }, // Opcional: añade padding si es necesario
                contentScale = ContentScale.Fit
            )
        }


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
                    indication = null // <-- esto elimina el sombreado
                ) {
                    leftArrowClicked = true
                    navController.navigate(AppScreens.AvatarPorcupineScreen.route)
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

    }
}

@Preview(showBackground = true)
@Composable
fun AvatarBirdScreenPreview() {
    // Pasamos un NavController falso para que compile en preview
    AvatarBirdScreen(navController = rememberNavController())
}