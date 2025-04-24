package com.universidad.ambientalkids.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.universidad.ambientalkids.R
import com.universidad.ambientalkids.navigation.AppScreens
import com.universidad.ambientalkids.ui.components.Background
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Leccion1bReciclaje(navController: NavController){
    Background {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(WindowInsets.statusBars.asPaddingValues())
        ) {

            // Imagen central
            Image(
                painter = painterResource(id = R.drawable.ic_reciclaje),
                contentDescription = "Ilustración central",
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.Center)
                    .offset(x = (-55).dp, y = (-100).dp),
                contentScale = ContentScale.Crop
            )

            // Canecas alrededor de la imagen central
            Image(
                painter = painterResource(id = R.drawable.ic_caneca_negra),
                contentDescription = "Caneca negra",
                modifier = Modifier
                    .size(180.dp)
                    .align(Alignment.Center)
                    .offset(x = (-110).dp, y = (80).dp), // izquierda
                contentScale = ContentScale.Fit
            )

            Image(
                painter = painterResource(id = R.drawable.ic_caneca_blanca),
                contentDescription = "Caneca blanca",
                modifier = Modifier
                    .size(180.dp)
                    .align(Alignment.Center)
                    .offset(x = (20).dp, y = (190).dp), // abajo
                contentScale = ContentScale.Fit
            )

            Image(
                painter = painterResource(id = R.drawable.ic_caneca_verde),
                contentDescription = "Caneca verde",
                modifier = Modifier
                    .size(180.dp)
                    .align(Alignment.Center)
                    .offset(x = (100).dp, y = (0).dp), // derecha
                contentScale = ContentScale.Fit
            )

            // Texto ovalado
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 40.dp, start = 26.dp, end = 26.dp)
                    .offset(y = 20.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = Color(0xFF79A948),
                    tonalElevation = 4.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                ) {
                    Text(
                        text = "Te voy a enseñar a separar la basura como todo un experto ecológico 🌍💪",
                        modifier = Modifier.padding(20.dp),
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.White,
                            lineHeight = 20.sp
                        )
                    )
                }
            }

            // Botón abajo
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 20.dp),
                contentAlignment = Alignment.BottomCenter
            ) {

                // Imagen central "pasar" con efecto de click
                var pasarClicked by remember { mutableStateOf(false) }
                val pasarInteractionSource = remember { MutableInteractionSource() }
                val pasarScale by animateFloatAsState(
                    targetValue = if (pasarClicked) 0.95f else 1f,
                    animationSpec = tween(durationMillis = 200),
                    label = "pasarScale",
                    finishedListener = { pasarClicked = false }
                )

                Image(
                    painter = painterResource(id = R.drawable.ic_pasar),
                    contentDescription = "Caneca verde",
                    modifier = Modifier
                        .size(130.dp)
                        .align(Alignment.BottomEnd)
                        .scale(pasarScale)
                        .clickable(
                            interactionSource = pasarInteractionSource,
                            indication = null
                        ) {
                            pasarClicked = true
                            // CoroutineScope para manejar el retardo
                            CoroutineScope(Dispatchers.Main).launch {
                                delay(300) // Espera 300ms para que se complete la animación

                                navController.navigate(AppScreens.Leccion1cReciclaje.route)
                                pasarClicked = false // Resetear el estado después de navegar
                            }
                        }, // derecha
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun Leccion1bReciclajePreview() {
    // Pasamos un NavController falso para que compile en preview
    Leccion1bReciclaje(navController = rememberNavController())
}