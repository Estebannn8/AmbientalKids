package com.universidad.ambientalkids.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Leccion1cReciclaje(navController: NavController){
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
                painter = painterResource(id = R.drawable.ic_flecha_green),
                contentDescription = "Caneca negra",
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.Center)
                    .offset(x = (0).dp, y = (30).dp), // izquierda
                contentScale = ContentScale.Fit
            )


            Image(
                painter = painterResource(id = R.drawable.ic_trash),
                contentDescription = "Caneca verde",
                modifier = Modifier
                    .size(220.dp)
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
                        text = "¿Qué va en el\n" +
                                "contenedor VERDE?",
                        modifier = Modifier.padding(20.dp),
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = Color.White,
                            lineHeight = 20.sp
                        )
                    )
                }
            }

            // Dentro del mismo @Composable
            val opciones = listOf("PAPEL USADO", "CASCARAS DE FRUTA", "BOTELLAS DE VIDRIO", "JUGUETES ROTOS")
            val correcta = "CASCARAS DE FRUTA"

            val colorStates = remember { mutableStateListOf(*Array(opciones.size) { Color.White }) }
            val coroutineScope = rememberCoroutineScope()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 100.dp)
                    .align(Alignment.BottomCenter),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                opciones.forEachIndexed { index, texto ->
                    var offsetX by remember { mutableStateOf(0f) }
                    var offsetY by remember { mutableStateOf(0f) }

                    Box(
                        modifier = Modifier
                            .pointerInput(Unit) {
                                detectDragGestures { change, dragAmount ->
                                    change.consume()
                                    offsetX += dragAmount.x
                                    offsetY += dragAmount.y
                                }
                            }
                            .background(colorStates[index], RoundedCornerShape(12.dp))
                            .clickable {
                                coroutineScope.launch {
                                    // Asigna color solo a la opción seleccionada
                                    colorStates[index] = if (texto == correcta) Color(0xFF6FCF97) else Color(0xFFE57373)

                                    delay(600) // Mostrar el color antes de navegar

                                    if (texto == correcta) {
                                        navController.navigate(AppScreens.Leccion1Bien.route)
                                    } else {
                                        navController.navigate(AppScreens.Leccion1Mal.route)
                                    }
                                }
                            }
                            .padding(horizontal = 24.dp, vertical = 12.dp)
                    ) {
                        Text(
                            text = texto,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }



        }
    }
}



@Preview(showBackground = true)
@Composable
fun Leccion1cReciclejaPreview() {
    // Pasamos un NavController falso para que compile en preview
    Leccion1cReciclaje(navController = rememberNavController())
}