package com.example.re_watch.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.re_watch.FirestoreViewModel
import com.example.re_watch.screens.HomeScreen
import com.example.re_watch.screens.LoginScreen
import com.example.re_watch.screens.ProfileScreen
import com.example.re_watch.screens.SignUpScreen
import com.example.re_watch.screens.WelcomeScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val currentUser = FirebaseAuth.getInstance().currentUser
    NavHost(
        navController = navController,
        startDestination =
        if (currentUser != null) {
            AppScreens.HomeScreen.route
        }else{
            AppScreens.WelcomeScreen.route
        }
        ){
        composable(AppScreens.WelcomeScreen.route) {
            WelcomeScreen(navController = navController)
        }
        composable(AppScreens.LoginScreen.route) {
            LoginScreen(navController = navController)
        }
        composable(AppScreens.SignUpScreen.route) {
            SignUpScreen(navController = navController)
        }
        composable(AppScreens.HomeScreen.route) {
            HomeScreen(navController = navController)
        }
        composable(AppScreens.ProfileScreen.route) {
            ProfileScreen(navController = navController)
        }


    }
}