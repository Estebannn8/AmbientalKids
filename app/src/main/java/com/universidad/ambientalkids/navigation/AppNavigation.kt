package com.universidad.ambientalkids.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.universidad.ambientalkids.ui.MainScreen
import com.universidad.ambientalkids.ui.SplashScreen
import com.universidad.ambientalkids.ui.auth.AuthScreen
import com.universidad.ambientalkids.ui.auth.RecoveryScreen
import com.universidad.ambientalkids.viewmodel.AuthViewModel
import com.universidad.ambientalkids.ui.HomeScreen
import com.universidad.ambientalkids.ui.ProfileScreen
import com.universidad.ambientalkids.ui.StoreScreen
import com.universidad.ambientalkids.ui.lesson.LessonScreen
import com.universidad.ambientalkids.viewmodel.LessonsViewModel
import com.universidad.ambientalkids.viewmodel.UserViewModel

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    // Crear los ViewModels aquí para que sean compartidos
    val authViewModel: AuthViewModel = viewModel()
    val userViewModel: UserViewModel = viewModel()
    val lessonsViewModel: LessonsViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = AppScreens.SplashScreen.route
    ) {

        composable(AppScreens.SplashScreen.route) {
            SplashScreen(
                navController = navController,
                authViewModel = authViewModel,
                userViewModel = userViewModel
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
                authViewModel = authViewModel,
                userViewModel = userViewModel
            )
        }

        composable(AppScreens.RecoveryScreen.route) {
            RecoveryScreen(navController)
        }

        composable(AppScreens.HomeScreen.route) {
            HomeScreen(
                navController = navController,
                userViewModel = userViewModel
            )
        }

        composable(AppScreens.ProfileScreen.route) {
            ProfileScreen(
                navController
            )
        }

        composable(AppScreens.StoreScreen.route) {
            StoreScreen(navController)
        }

        composable(
            route = AppScreens.LessonScreen.route,
            arguments = listOf(navArgument("category") { type = NavType.StringType })
        ) { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: "ahorro"
            LessonScreen(
                category = category,
                userViewModel = userViewModel,
                lessonsViewModel = lessonsViewModel,
                navController = navController
            )
        }

    }
}
