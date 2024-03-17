package com.example.re_watch

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.re_watch.data.SignUpUIEvent
import com.example.re_watch.data.SignUpUIState
import com.example.re_watch.navigation.AppScreens
import com.google.firebase.auth.FirebaseAuth

class SignUpViewModel() : ViewModel() {

    private val TAG = SignUpViewModel::class.simpleName

    var signUpUIState = mutableStateOf(SignUpUIState())

    var allValidationPassed = mutableStateOf(false)

    var signUpInProgress = mutableStateOf(false)

    private lateinit var navController: NavHostController

    fun setNavController(navController: NavHostController) {
        this.navController = navController
    }

    fun onEvent(event:SignUpUIEvent){
        when(event){
            is SignUpUIEvent.UsernameChanged ->{
                signUpUIState.value = signUpUIState.value.copy(
                    username = event.username
                )
            }
            is SignUpUIEvent.EmailChanged ->{
                signUpUIState.value = signUpUIState.value.copy(
                    email = event.email
                )
            }
            is SignUpUIEvent.PasswordChanged ->{
                signUpUIState.value = signUpUIState.value.copy(
                    password = event.password
                )
            }
            is SignUpUIEvent.SignUpButtonClicked ->{
                createUserInFirebase()
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

    fun createUserInFirebase(){
        val email = signUpUIState.value.email
        val password = signUpUIState.value.password
        signUpInProgress.value = true
        Log.d("tag","user ${email} is  ${password}")
        if (email != null && email.isNotEmpty() && password != null && password.isNotEmpty()) {
            // Call createUserWithEmailAndPassword only if email and password are not empty or null
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    Log.d("tag", "inside_oncomplete")
                    Log.d("tag", "${task.isSuccessful}")
                    signUpInProgress.value = false
                    if (task.isSuccessful) {
                        navigateToHomeScreen(navController)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("tag", "inside_onfailure")
                    Log.d("tag", "${exception.message}")
                }
        } else {
            Log.d("tag", "Email or password is empty or null")
            // Handle the case where email or password is empty or null
        }

    }


    private fun navigateToHomeScreen(navController: NavController) {
        try {
            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser != null) {
                navController.popBackStack(AppScreens.WelcomeScreen.route, inclusive = true)
                navController.popBackStack(AppScreens.SignUpScreen.route, inclusive = true)
                navController.navigate(route = AppScreens.HomeScreen.route)
            }
        } catch (e: Exception) {
            // Handle the exception appropriately
            Log.e(TAG, "Error navigating to HomeScreen: ${e.message}", e)
        }
    }





}