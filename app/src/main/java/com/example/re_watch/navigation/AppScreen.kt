package com.example.re_watch.navigation

sealed class AppScreens(val route: String) {
    data object WelcomeScreen: AppScreens("welcome_screen")
    data object LoginScreen: AppScreens("login_screen")
    data object SignUpScreen: AppScreens("signUp_screen")
    data object HomeScreen: AppScreens("home_screen")
    data object ProfileScreen: AppScreens("profile_screen")
    data object StreamingPage: AppScreens("video")
    data object LikedScreen: AppScreens("Liked Screen")
}