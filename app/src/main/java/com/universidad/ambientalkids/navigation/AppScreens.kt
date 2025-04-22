package com.universidad.ambientalkids.navigation

sealed class  AppScreens(val route: String){
    object SplashScreen: AppScreens(route = "splash_screen")
    object MainScreen: AppScreens(route = "main_screen")
    object AuthScreen: AppScreens("auth_screen/{startInLogin}") {
        fun createRoute(startInLogin: Boolean) = "auth_screen/$startInLogin"
    }
    object RecoveryScreen: AppScreens(route = "recovery_screen")
    object AvatarDropScreen: AppScreens(route = "avatar_drop_screen")
    object AvatarFoxScreen: AppScreens(route = "avatar_fox_screen")
    object AvatarFrogScreen: AppScreens(route = "avatar_frog_screen")
    object AvatarPorcupineScreen: AppScreens(route = "avatar_porcupine_screen")
    object AvatarBirdScreen: AppScreens(route = "avatar_bird_screen")
    object LandingScreen1: AppScreens(route = "landing_Screen_1")
    object LandingScreen2: AppScreens(route = "landing_Screen_2")
    object LandingScreen3: AppScreens(route = "landing_Screen_3")
    object HomeScreen1: AppScreens(route = "home_Screen_1")
    object HomeScreen2: AppScreens(route = "home_Screen_2")
    object HomeScreen3: AppScreens(route = "home_Screen_3")
}

