package com.universidad.ambientalkids.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.universidad.ambientalkids.navigation.AppScreens
import com.universidad.ambientalkids.ui.components.LoadingOverlay
import com.universidad.ambientalkids.viewmodel.AuthViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val authState by authViewModel.state.collectAsState()
    val isAuthChecking = authState.isAuthChecking

    LaunchedEffect(Unit) {
        // Tiempo mínimo para mostrar el splash (3 segundos)
        delay(3000)

        authViewModel.checkAuthState { isAuthenticated ->
            if (isAuthenticated) {
                // Usuario autenticado - ir a Avatar selection
                navController.navigate(AppScreens.AvatarDropScreen.route) {
                    popUpTo(AppScreens.SplashScreen.route) { inclusive = true }
                }
            } else {
                // No autenticado - ir a Main (login)
                navController.navigate(AppScreens.MainScreen.route) {
                    popUpTo(AppScreens.SplashScreen.route) { inclusive = true }
                }
            }
        }
    }

    Splash()

    if (isAuthChecking) {
        LoadingOverlay()
    }
}

@Composable
fun Splash() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF5b9848)), // Color de fondo
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "AMBIENTALKIDS",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(70.dp))
            Text(
                text = "...",
                fontSize = 18.sp,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    Splash()
}