package com.universidad.ambientalkids.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.zIndex
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
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.background_main),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Logo
        Image(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = "Logo",
            modifier = Modifier
                .size(240.dp)
                .align(Alignment.TopCenter)
                .offset(y = 5.dp, x = 10.dp),
            contentScale = ContentScale.Fit
        )

        // Sección media con zorro, gota y conejo
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.6f)
                .align(Alignment.Center),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.zorro_main),
                contentDescription = "Zorro",
                modifier = Modifier
                    .size(160.dp)
                    .offset(x = (-70).dp, y = (150).dp)
                    .zIndex(2f)
            )
            Image(
                painter = painterResource(id = R.drawable.gota_main),
                contentDescription = "Gota",
                modifier = Modifier
                    .size(150.dp)
                    .offset(x = (0).dp, y = (30).dp)
                    .zIndex(1f)
            )
            Image(
                painter = painterResource(id = R.drawable.conejo_main),
                contentDescription = "Conejo",
                modifier = Modifier
                    .size(200.dp)
                    .offset(x = (80).dp, y = (90).dp)
                    .zIndex(2.5f)
            )
        }

        // Botones
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .zIndex(1f)
                .align(Alignment.BottomCenter)
                .padding(bottom = 60.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomButton(
                modifier = Modifier.zIndex(1f),
                buttonText = "REGISTRARSE",
                gradientLight = Color(0xFFBEEDB2),
                gradientDark = Color(0xFF41A429),
                baseColor = Color(0xFF62D744),
                onClick = { navController.navigate(AppScreens.AuthScreen.createRoute(startInLogin = false)) }
            )
            Spacer(modifier = Modifier.height(25.dp))
            CustomButton(
                buttonText = "INICIAR SESIÓN",
                gradientLight = Color(0xFFBEEDB2),
                gradientDark = Color(0xFF41A429),
                baseColor = Color(0xFF62D744),
                onClick = { navController.navigate(AppScreens.AuthScreen.createRoute(startInLogin = true)) }
            )
        }

        // 🐸 Rana encima de los botones
        Image(
            painter = painterResource(id = R.drawable.rana_main),
            contentDescription = "Rana",
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.BottomCenter)
                .offset(x = 110.dp, y = (-130).dp)
                .zIndex(5f)
        )
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