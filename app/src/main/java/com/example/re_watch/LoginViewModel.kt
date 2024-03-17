package com.example.re_watch

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.Navigation
import androidx.navigation.compose.NavHost
import com.example.re_watch.data.LoginUIEvent
import com.example.re_watch.data.LoginUIState
import com.example.re_watch.navigation.AppNavigation
import com.example.re_watch.navigation.AppScreens
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel() : ViewModel() {

    private val TAG = LoginViewModel::class.simpleName

    var loginUIState = mutableStateOf(LoginUIState())

    var allValidationPassed = mutableStateOf(false)

    var loginInProgress = mutableStateOf(false)

    private lateinit var navController: NavHostController

    fun setNavController(navController: NavHostController) {
        this.navController = navController
    }

    fun onEvent(event:LoginUIEvent){
        when(event){
            is LoginUIEvent.EmailChanged ->{
                loginUIState.value = loginUIState.value.copy(
                    email = event.email
                )
            }
            is LoginUIEvent.PasswordChanged ->{
                loginUIState.value = loginUIState.value.copy(
                    password = event.password
                )
            }
            is LoginUIEvent.LoginButtonClicked ->{
                login()
            }

            else -> {

            }
        }
        validateLoginUIDataWithRules()
    }



    private fun validateLoginUIDataWithRules(){
//        val emailResult = Validator.validateEmail(){
//            email = loginUIState.value.email
//        }
//
//        val passwordResult = Validator.validatePassword(){
//            password = loginUIState.value.password
//        }

//        loginUIState.value = loginUIState.value.copy(
//            emailError = emailResult.status,
//            passwordError = passwordResult.status
//        )
//        allValidationPassed.value = emailResult.status && passwordResult.status
    }

    private fun login() {
        var email = loginUIState.value.email
        var password = loginUIState.value.password
        FirebaseAuth
            .getInstance()
            .signInWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                Log.d(TAG,"Inside_success_login")
                Log.d(TAG,"${it.isSuccessful}")
                if(it.isSuccessful){
                    navigateToHomeScreen(navController)
//
                }
            }
            .addOnFailureListener {
                Log.d(TAG,"Inside_failure_login")
                Log.d(TAG,"${it.message}")
            }
    }

    private fun navigateToHomeScreen(navController: NavController) {
        try {
            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser != null) {
//                navController.navigate(route = AppScreens.HomeScreen.route)
                navController.popBackStack(AppScreens.WelcomeScreen.route, inclusive = true)
                navController.popBackStack(AppScreens.LoginScreen.route, inclusive = true)
                navController.navigate(route = AppScreens.HomeScreen.route)
            }
        } catch (e: Exception) {
            // Handle the exception appropriately
            Log.e(TAG, "Error navigating to HomeScreen: ${e.message}", e)
        }
    }





}