package com.universidad.ambientalkids.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.universidad.ambientalkids.R

@Composable
fun Background(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {

            // Parte superior con imagen decorativa
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(Color(0xFFcfecfb))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_top_decoration),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                        .offset(y = 23.dp)
                )
            }

            // Franja superior simulando el borde
            Box(
                modifier = Modifier
                    .height(15.dp)
                    .fillMaxWidth()
                    .background(Color(0xFFc7e6ac))
            )

            // Parte inferior con imágenes decorativas
            Box(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxWidth()
                    .background(Color(0xFFdeefae))
                    .clipToBounds() // <-- permite que las imágenes se salgan
            ) {
                // Imagen superior desplazada a la izquierda
                Image(
                    painter = painterResource(id = R.drawable.ic_bottom_decoration1),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(x = (-90).dp) // <-- ajusta este valor como quieras
                        .align(Alignment.TopCenter)
                )

                // Imagen inferior, pegada al fondo
                Image(
                    painter = painterResource(id = R.drawable.ic_bottom_decoration2),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                )
            }
        }

        // Contenido superpuesto
        content()
    }
}



@Preview(showBackground = true)
@Composable
fun BackgroundPreview() {
    Background {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Hola desde el contenido", fontSize = 18.sp)
        }
    }
}
