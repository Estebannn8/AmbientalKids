package com.universidad.ambientalkids.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.universidad.ambientalkids.R
import com.universidad.ambientalkids.navigation.AppScreens
import com.universidad.ambientalkids.ui.components.LoadingOverlay
import com.universidad.ambientalkids.viewmodel.AuthViewModel
import com.universidad.ambientalkids.events.UserEvent
import com.universidad.ambientalkids.viewmodel.UserViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel
) {
    val userState by userViewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        delay(3000)
        val uid = authViewModel.getCurrentUserId()

        if (uid != null) {
            userViewModel.sendEvent(UserEvent.LoadUser(uid))
        } else {
            navController.navigate(AppScreens.MainScreen.route) {
                popUpTo(AppScreens.SplashScreen.route) { inclusive = true }
            }
        }
    }

    // Navegar
    LaunchedEffect(userState.isLoading) {
        if (
            !userState.isLoading &&
            userState.userData.uid.isNotEmpty()
        ) {
            navController.navigate(AppScreens.HomeScreen.route) {
                popUpTo(AppScreens.SplashScreen.route) { inclusive = true }
            }
        }
    }

    Splash()

    if (userState.isLoading) {
        LoadingOverlay()
    }
}

@Composable
fun Splash(){
    Box(
        modifier = Modifier
            .fillMaxSize(), // Color de fondo
        contentAlignment = Alignment.BottomCenter
    ) {


        Image(
            painter = painterResource(id = R.drawable.ic_background),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )


        //
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 25.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_logo),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .height(400.dp)
                        .aspectRatio(1.2f)
                        .offset(y = (-4).dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    Splash()
}