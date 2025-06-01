package com.example.re_watch

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.re_watch.data.LoginUIEvent
import com.example.re_watch.data.LoginUIState
import com.example.re_watch.navigation.AppScreens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException


class LoginViewModel() : ViewModel() {


    val firebaseAuth = FirebaseAuth.getInstance()


    private val TAG = LoginViewModel::class.simpleName

    var loginUIState = mutableStateOf(LoginUIState())

    var allValidationPassed = mutableStateOf(false)

    var loginInProgress = mutableStateOf(false)
    var emailErrorText = mutableStateOf(true)
    var passwordErrorText = mutableStateOf(true)

    private lateinit var navController: NavHostController
    lateinit var context: Context

    fun setNavController(navController: NavHostController,context: Context) {
        this.navController = navController
        this.context = context
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
        val emailResult = Validator.validateEmail(loginUIState.value.email)

        val passwordResult = Validator.validatePassForLogin(loginUIState.value.password)

        loginUIState.value = loginUIState.value.copy(
            emailError = emailResult,
            passwordError = passwordResult

        )
        allValidationPassed.value = emailResult && passwordResult
    }

    private fun login() {

        var email = loginUIState.value.email
        var password = loginUIState.value.password
        FirebaseAuth
            .getInstance()
            .signInWithEmailAndPassword(email,password)
            .addOnCompleteListener {task ->
                if(task.isSuccessful){
                    loginInProgress.value  = true
                    navigateToHomeScreen(navController)
                }
                else{
                    loginInProgress.value = false
                    handleLoginFailure(task.exception,email)
                    task.exception?.let { Log.e(TAG, it.toString()) }
                }
            }

    }

    private fun navigateToHomeScreen(navController: NavController) {
        try {
            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser != null) {

                navController.popBackStack(AppScreens.WelcomeScreen.route, inclusive = true)
                navController.popBackStack(AppScreens.LoginScreen.route, inclusive = true)
                navController.navigate(route = AppScreens.HomeScreen.route)
            }
        } catch (e: Exception) {
            // Handle the exception appropriately
            Log.e(TAG, "Error navigating to HomeScreen: ${e.message}", e)
        }
    }

    fun handleLoginFailure(exception: Exception?, email: String) {
        if (exception is FirebaseAuthInvalidCredentialsException) {
                val message = "Invalid Credentials"
                Toast.makeText(
                    context,
                    message,
                    Toast.LENGTH_LONG
                ).show()

        } else {
            // Handle other types of exceptions
            val message = "An error occurred"
            Toast.makeText(
                context,
                message,
                Toast.LENGTH_LONG
            ).show()
        }
    }


    fun showSignupSuggestion() {

        val message = "This email is not registered. Would you like to sign up?"

        Toast.makeText(
            context,
            message,
            Toast.LENGTH_LONG
        ).show()

    }



}