package com.universidad.ambientalkids.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
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
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.universidad.ambientalkids.R
import com.universidad.ambientalkids.navigation.AppScreens
import com.universidad.ambientalkids.ui.components.Background
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HomeScreen1(navController: NavController){
    Background {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(WindowInsets.statusBars.asPaddingValues()),
            contentAlignment = Alignment.Center
        ) {

            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.tap_animation))

            LottieAnimation(
                composition = composition,
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(y = 100.dp, x = 50.dp)
                    .size(100.dp)
                    .zIndex(1f)
            )

            // Imagen ic_plant encima de todo, centrada
            Image(
                painter = painterResource(id = R.drawable.ic_plant),
                contentDescription = "Planta decorativa",
                modifier = Modifier
                    .align(Alignment.Center)
                    .zIndex(1f)
                    .offset(y = (-220).dp, x = (-20).dp)
                    .size(100.dp), // Ajusta tamaño si es necesario
                contentScale = ContentScale.Fit
            )

            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center, // Centra verticalmente
                horizontalAlignment = Alignment.CenterHorizontally // Centra horizontalmente
            ) {

                // Imagen central "home1" con efecto de click
                var home1Clicked by remember { mutableStateOf(false) }
                val home1InteractionSource = remember { MutableInteractionSource() }
                val home1Scale by animateFloatAsState(
                    targetValue = if (home1Clicked) 0.95f else 1f,
                    animationSpec = tween(durationMillis = 200),
                    label = "home1Scale",
                    finishedListener = { home1Clicked = false }
                )

                Image(
                    painter = painterResource(id = R.drawable.ic_home1),
                    contentDescription = "Ilustración central",
                    modifier = Modifier.size(400.dp)
                        .offset(y = 0.dp, x = (-20).dp)
                        .scale(home1Scale)
                        .clickable(
                            interactionSource = home1InteractionSource,
                            indication = null
                        ) {
                            home1Clicked = true
                            // CoroutineScope para manejar el retardo
                            CoroutineScope(Dispatchers.Main).launch {
                                delay(300) // Espera 300ms para que se complete la animación
                                // Evento click
                                home1Clicked = false // Resetear el estado después de navegar
                            }
                        },
                    contentScale = ContentScale.Crop // Mantiene proporciones sin recortar
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
                        navController.navigate(AppScreens.HomeScreen2.route)
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
                        navController.navigate(AppScreens.HomeScreen3.route)
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



            Spacer(modifier = Modifier.height(20.dp))

        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreen1Preview() {
    // Pasamos un NavController falso para que compile en preview
    HomeScreen1(navController = rememberNavController())
}