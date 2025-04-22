package com.universidad.ambientalkids.ui.landing

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
fun LandingScreen3(navController: NavController){
    Background {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable { navController.navigate(AppScreens.HomeScreen1.route) {
                    popUpTo(AppScreens.LandingScreen3.route) {
                        inclusive = true
                    }
                } }
                .padding(WindowInsets.statusBars.asPaddingValues()),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center, // Centra verticalmente
                horizontalAlignment = Alignment.CenterHorizontally // Centra horizontalmente
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_landing3),
                    contentDescription = "Ilustración central",
                    modifier = Modifier.size(500.dp)
                        .offset(y = 70.dp),
                    contentScale = ContentScale.Crop // Mantiene proporciones sin recortar
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(26.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White.copy(alpha = 0.95f),
                    tonalElevation = 4.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    Text(
                        text = "\uD83D\uDE80 ¡Vamos, comencemos esta aventura juntos! \uD83C\uDF08\uD83C\uDFAE",
                        modifier = Modifier.padding(16.dp),
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 25.sp,
                            color = Color.Black,
                            lineHeight = 25.sp
                        )
                    )


                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LandingScreen3Preview() {
    // Pasamos un NavController falso para que compile en preview
    LandingScreen3(navController = rememberNavController())
}