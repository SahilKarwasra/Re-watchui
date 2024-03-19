package com.example.re_watch

import android.content.Context
import android.util.Log
import android.widget.Toast
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
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.FirebaseFunctionsLegacyRegistrar


class LoginViewModel() : ViewModel() {


    val functions = FirebaseFunctions.getInstance()

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

        val passwordResult = Validator.validatePassword(loginUIState.value.password)

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
            .addOnCompleteListener {
                Log.d(TAG,"Inside_success_login")
                Log.d(TAG,"${it.isSuccessful}")
                if(it.isSuccessful){
                    loginInProgress.value  = true
                    navigateToHomeScreen(navController)

                }
            }
            .addOnFailureListener {
                loginInProgress.value = false
                Toast.makeText(
                    context,
                    it.message,
                    Toast.LENGTH_LONG)
                    .show()
                Log.d(TAG,"Inside_failure_login")
                it.message?.let { it1 -> Log.d(TAG, it1) }
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





}