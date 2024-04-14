package com.example.re_watch.navigation

import VideoData
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.re_watch.screens.ChannelScreen
import com.example.re_watch.screens.HomeScreen
import com.example.re_watch.screens.LikedScreen
import com.example.re_watch.screens.LoginScreen
import com.example.re_watch.screens.ProfileScreen
import com.example.re_watch.screens.RemoveVideoScreen
import com.example.re_watch.screens.SearchScreen
import com.example.re_watch.screens.SettingsScreen
import com.example.re_watch.screens.SignUpScreen
import com.example.re_watch.screens.StreamingPage
import com.example.re_watch.screens.WelcomeScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson

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
        composable(route = "${AppScreens.StreamingPage.route}/{videoDataJson}",
            arguments = listOf(
                navArgument("videoDataJson") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val videoData = Gson().fromJson(backStackEntry.arguments?.getString("videoDataJson") ?: "", VideoData::class.java)

            StreamingPage(navController = navController, param = videoData)
        }
        composable(AppScreens.LikedScreen.route) {
            LikedScreen(navController = navController)
        }
        composable(AppScreens.SearchScreen.route) {
            SearchScreen(navController = navController)
        }
        composable(AppScreens.SettingScreen.route) {
            SettingsScreen(navController = navController)
        }
        composable("${AppScreens.ChannelScreen.route}/{userId}",
            deepLinks =
            listOf(
                navDeepLink {
                uriPattern = "https://rewatch.com/user/{argument}"
            }),
        ){
            val userId = it.arguments?.getString("userId") ?: ""
            val userSlug = it.arguments?.getString("argument") ?: ""
            ChannelScreen(navController = navController, userId = userId,userSlug)
        }
        composable(
            route = "${AppScreens.ChannelScreen.route}/{userId}",
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "https://rewatch.online/{user}/{userSlug}"
                }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            val userSlug = backStackEntry.arguments?.getString("userSlug") ?: ""
            ChannelScreen(navController = navController, userId = userId, userSlug = userSlug)
        }
        composable(AppScreens.RemoveScreen.route) {
            RemoveVideoScreen(navController = navController)
        }


    }
}


