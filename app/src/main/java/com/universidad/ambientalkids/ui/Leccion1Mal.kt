package com.universidad.ambientalkids.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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

@Composable
fun Leccion1Mal(navController: NavController){
    Background {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable { navController.navigate(AppScreens.Leccion1cReciclaje.route) }
                .padding(WindowInsets.statusBars.asPaddingValues())
        ) {


            // Mal
            Image(
                painter = painterResource(id = R.drawable.ic_mal),
                contentDescription = "Caneca negra",
                modifier = Modifier
                    .size(300.dp)
                    .align(Alignment.Center)
                    .offset(x = (0).dp, y = (0).dp), // izquierda
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
                        text = "\uD83D\uDE05 ¡Ups! Esa no es la respuesta correcta. Pero no te preocupes, estás aprendiendo. \uD83C\uDF31",
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

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 40.dp, start = 26.dp, end = 26.dp)
                    .offset(y = 200.dp),
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = "La caneca verde no es para botellas ni juguetes.  Es para lo orgánico, como restos de comida o cáscaras \uD83C\uDF4C\uD83E\uDD6C\uD83C\uDF5E",
                    modifier = Modifier.padding(20.dp),
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Black,
                        lineHeight = 20.sp
                    )
                )
            }



        }
    }
}

@Preview(showBackground = true)
@Composable
fun Leccion1MalPreview() {
    // Pasamos un NavController falso para que compile en preview
    Leccion1Mal(navController = rememberNavController())
}