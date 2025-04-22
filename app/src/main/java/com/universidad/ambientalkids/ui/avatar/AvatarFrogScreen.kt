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
fun AvatarFrogScreen(navController: NavController){
    Box(modifier = Modifier.fillMaxSize()) {

        // Imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.ic_frog_background),
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

            // Imagen central "frog" con efecto de click
            var frogClicked by remember { mutableStateOf(false) }
            val frogInteractionSource = remember { MutableInteractionSource() }
            val frogScale by animateFloatAsState(
                targetValue = if (frogClicked) 0.95f else 1f,
                animationSpec = tween(durationMillis = 200),
                label = "frogScale",
                finishedListener = { frogClicked = false }
            )

            Image(
                painter = painterResource(id = R.drawable.ic_frog),
                contentDescription = "Frog",
                modifier = Modifier
                    .sizeIn(maxWidth = 300.dp, maxHeight = 300.dp) // Ajusta estos valores según necesites
                    .padding(16.dp)
                    .scale(frogScale)
                    .clickable(
                        interactionSource = frogInteractionSource,
                        indication = null
                    ) {
                        frogClicked = true
                        // CoroutineScope para manejar el retardo
                        CoroutineScope(Dispatchers.Main).launch {
                            delay(300) // Espera 300ms para que se complete la animación
                            navController.navigate(AppScreens.LandingScreen1.route) {
                                popUpTo(AppScreens.AvatarFrogScreen.route) {
                                    inclusive = true
                                }
                            }
                            frogClicked = false // Resetear el estado después de navegar
                        }
                    },
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
                    navController.navigate(AppScreens.AvatarFoxScreen.route)
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
                    indication = null // <-- esto elimina el sombreado
                ) {
                    rightArrowClicked = true
                    navController.navigate(AppScreens.AvatarPorcupineScreen.route)
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
fun AvatarFrogScreenPreview() {
    // Pasamos un NavController falso para que compile en preview
    AvatarFrogScreen(navController = rememberNavController())
}
