package com.universidad.ambientalkids.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.universidad.ambientalkids.ui.HomeScreen1
import com.universidad.ambientalkids.ui.HomeScreen2
import com.universidad.ambientalkids.ui.HomeScreen3
import com.universidad.ambientalkids.ui.landing.LandingScreen1
import com.universidad.ambientalkids.ui.landing.LandingScreen2
import com.universidad.ambientalkids.ui.landing.LandingScreen3
import com.universidad.ambientalkids.ui.MainScreen
import com.universidad.ambientalkids.ui.SplashScreen
import com.universidad.ambientalkids.ui.auth.AuthScreen
import com.universidad.ambientalkids.ui.auth.RecoveryScreen
import com.universidad.ambientalkids.ui.avatar.AvatarBirdScreen
import com.universidad.ambientalkids.ui.avatar.AvatarDropScreen
import com.universidad.ambientalkids.ui.avatar.AvatarFoxScreen
import com.universidad.ambientalkids.ui.avatar.AvatarFrogScreen
import com.universidad.ambientalkids.ui.avatar.AvatarPorcupineScreen
import com.universidad.ambientalkids.viewmodel.AuthViewModel

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    // Crear los ViewModels aquí para que sean compartidos
    val authViewModel: AuthViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = AppScreens.SplashScreen.route
    ) {

        composable(AppScreens.SplashScreen.route) {
            SplashScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }

        composable(AppScreens.MainScreen.route) {
            MainScreen(navController)
        }

        composable(
            route = AppScreens.AuthScreen.route,
            arguments = listOf(navArgument("startInLogin") { type = NavType.BoolType })
        ) { backStackEntry ->
            val startInLogin = backStackEntry.arguments?.getBoolean("startInLogin") ?: true
            AuthScreen(
                startInLogin,
                navController = navController,
                authViewModel = authViewModel
            )
        }

        composable(AppScreens.RecoveryScreen.route) {
            RecoveryScreen(navController)
        }

        composable(AppScreens.AvatarDropScreen.route) {
            AvatarDropScreen(navController)
        }

        composable(AppScreens.AvatarFoxScreen.route) {
            AvatarFoxScreen(navController)
        }

        composable(AppScreens.AvatarFrogScreen.route) {
            AvatarFrogScreen(navController)
        }

        composable(AppScreens.AvatarPorcupineScreen.route) {
            AvatarPorcupineScreen(navController)
        }

        composable(AppScreens.AvatarBirdScreen.route) {
            AvatarBirdScreen(navController)
        }

        composable(AppScreens.LandingScreen1.route) {
            LandingScreen1(navController)
        }

        composable(AppScreens.LandingScreen2.route) {
            LandingScreen2(navController)
        }

        composable(AppScreens.LandingScreen3.route) {
            LandingScreen3(navController)
        }

        composable(AppScreens.HomeScreen1.route) {
            HomeScreen1(navController)
        }

        composable(AppScreens.HomeScreen2.route) {
            HomeScreen2(navController)
        }

        composable(AppScreens.HomeScreen3.route) {
            HomeScreen3(navController)
        }
    }
}
