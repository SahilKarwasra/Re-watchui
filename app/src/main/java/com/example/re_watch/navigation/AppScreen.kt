package com.example.re_watch.navigation

enum class AppScreens {
    WelcomeScreen,
    LoginScreen,
    SignUpScreen,
    HomeScreen,
    ProfileScreen;

    companion object {
        fun fromRoute(route: String?): AppScreens =
            when (route?.substringBefore("/")) {
                WelcomeScreen.name -> WelcomeScreen
                LoginScreen.name -> LoginScreen
                SignUpScreen.name -> SignUpScreen
                HomeScreen.name -> HomeScreen
                ProfileScreen.name -> ProfileScreen
                null -> WelcomeScreen
                else -> throw IllegalArgumentException("Route $route is not Recognised")
            }
    }

}