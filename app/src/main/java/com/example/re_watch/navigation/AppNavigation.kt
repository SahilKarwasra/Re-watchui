package com.example.re_watch.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.re_watch.screens.HomeScreen
import com.example.re_watch.screens.LoginScreen
import com.example.re_watch.screens.ProfileScreen
import com.example.re_watch.screens.SignUpScreen
import com.example.re_watch.screens.WelcomeScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = AppScreens.WelcomeScreen.name
        ){
        composable(AppScreens.WelcomeScreen.name) {
            WelcomeScreen(navController = navController)
        }
        composable(AppScreens.LoginScreen.name) {
            LoginScreen(navController = navController)
        }
        composable(AppScreens.SignUpScreen.name) {
            SignUpScreen(navController = navController)
        }
        composable(AppScreens.HomeScreen.name) {
            HomeScreen(navController = navController)
        }
        composable(AppScreens.ProfileScreen.name) {
            ProfileScreen(navController = navController)
        }


    }
}