package com.universidad.ambientalkids.ui

import androidx.compose.foundation.Image
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.universidad.ambientalkids.ui.components.CustomButton

@Composable
fun Leccion1Reciclaje(navController: NavController){
    Background {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(WindowInsets.statusBars.asPaddingValues()),
        ) {

            // Imagen central
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_reciclaje),
                    contentDescription = "Ilustración central",
                    modifier = Modifier
                        .size(400.dp)
                        .offset(y = 0.dp, x = 0.dp),
                    contentScale = ContentScale.Crop
                )
            }

            // Texto ovalado
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 40.dp, start = 26.dp, end = 26.dp)
                    .offset(y = 20.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Surface(
                    shape = RoundedCornerShape(50), // Más ovalado
                    color = Color(0xFF79A948),
                    tonalElevation = 4.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                ) {
                    Text(
                        text = "¡Hola! Soy Pesito Reciclador. Hoy te enseño a separar la basura como un crack del planeta 🌍. ¿Listo para ser un héroe ecológico?",
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

            // Botón al fondo
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 20.dp)
                    .offset(y = (-150).dp), // Espacio inferior
                contentAlignment = Alignment.BottomCenter
            ) {
                CustomButton(
                    buttonText = "ESTOY LISTO!!!",
                    gradientLight = Color(0xFF4cff91),
                    gradientDark = Color(0xFF33932a),
                    baseColor = Color(0xFF5b9848),
                    onClick = { navController.navigate(AppScreens.Leccion1bReciclaje.route) }
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun Leccion1ReciclajePreview() {
    // Pasamos un NavController falso para que compile en preview
    Leccion1Reciclaje(navController = rememberNavController())
}