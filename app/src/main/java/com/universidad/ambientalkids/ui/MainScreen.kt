package com.universidad.ambientalkids.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
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
import com.universidad.ambientalkids.ui.theme.AppTypography

@Composable
fun MainScreen(navController: NavController) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    Background {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(WindowInsets.statusBars.asPaddingValues()),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            // Encabezado
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight * 0.14f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_logo),
                    contentDescription = "Ilustración central",
                    modifier = Modifier.fillMaxSize()
                        .offset(y = 50.dp),
                    contentScale = ContentScale.Fit
                )
            }

            // Sección media
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f) // Ocupa espacio flexible entre header y footer
                    .padding(horizontal = 20.dp, vertical = 15.dp),
                contentAlignment = Alignment.Center
            ) {

                Image(
                    painter = painterResource(id = R.drawable.ic_pesitos_inicio),
                    contentDescription = "Ilustración central",
                    modifier = Modifier.fillMaxSize()
                        .offset(y = 60.dp),
                    contentScale = ContentScale.Fit
                )

            }

            // Botones
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight * 0.29f)
                    .offset(y = (-70).dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CustomButton(
                    buttonText = "REGISTRARSE",
                    gradientLight = Color(0xFF4cff91),
                    gradientDark = Color(0xFF33932a),
                    baseColor = Color(0xFF5b9848),
                    onClick = { navController.navigate(AppScreens.AuthScreen.createRoute(startInLogin = false)) }
                )
                Spacer(modifier = Modifier.height(12.dp))
                CustomButton(
                    buttonText = "INICIAR SESIÓN",
                    gradientLight = Color(0xFF4cff91),
                    gradientDark = Color(0xFF33932a),
                    baseColor = Color(0xFF5b9848),
                    onClick = { navController.navigate(AppScreens.AuthScreen.createRoute(startInLogin = true)) }
                )
            }
        }
    }
}


@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "MainScreen Preview"
)
@Composable
fun MainScreenPreview() {
    val navController = rememberNavController()

    MainScreen(navController = navController)
}