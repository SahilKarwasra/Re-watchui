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
import com.google.firebase.database.FirebaseDatabase

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
        validateSignupUIDataWithRules()
    }



    private fun validateSignupUIDataWithRules(){
        val usernameResult = Validator.validateUsername(signUpUIState.value.username)

        val emailResult = Validator.validateEmail(signUpUIState.value.email)

        val passwordResult = Validator.validatePassword(signUpUIState.value.password)

        signUpUIState.value = signUpUIState.value.copy(
            emailError = emailResult,
            passwordError = passwordResult,
            usernameError = usernameResult

        )
        allValidationPassed.value = emailResult && passwordResult && usernameResult
    }

    fun createUserInFirebase(){
        val username = signUpUIState.value.username
        val email = signUpUIState.value.email
        val password = signUpUIState.value.password
        signUpInProgress.value = true
        Log.d("tag","user ${email} is  ${password}")

        if (email != null && email.isNotEmpty() && password != null && password.isNotEmpty() && username != null && username.isNotEmpty()) {

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    signUpInProgress.value = false
                    if (task.isSuccessful) {
                        val user = FirebaseAuth.getInstance().currentUser
                        val userId = user?.uid ?: ""


                        val database = FirebaseDatabase.getInstance()
                        val usersRef = database.getReference("users")
                        val userDetails = hashMapOf(
                            "username" to username,
                        )
                        usersRef.child(userId).setValue(userDetails)
                        navigateToHomeScreen(navController)
                    } else {
                        Log.d("tag", "User creation failed: ${task.exception?.message}")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("tag", "User creation failed: ${exception.message}")
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