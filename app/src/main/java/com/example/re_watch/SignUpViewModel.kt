package com.example.re_watch

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.re_watch.data.SignUpUIEvent
import com.example.re_watch.data.SignUpUIState
import com.example.re_watch.navigation.AppScreens
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.database
import com.google.firebase.firestore.FirebaseFirestore

data class User(
    val displayName: String,
    val email: String,
    val profileUrl: String,
    val profileImage: String // You can include any other user information you want to store
)
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
        Log.d("Signup","userName ${username}user ${email} is  ${password}")

        if (email != null && email.isNotEmpty() && password != null && password.isNotEmpty() && username != null && username.isNotEmpty()) {

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    signUpInProgress.value = false
                    if (task.isSuccessful) {
                        val user = FirebaseAuth.getInstance().currentUser
                        val userId = user?.uid
                        val displayName = username

                        val userData = user?.email?.let {
                            User(
                                displayName = displayName,
                                email = it,
                                profileUrl = "test",
                                profileImage = user.photoUrl.toString()
                            )
                        }


                        userId?.let { userData?.let { it1 -> saveUserData(userId = it, it1) } }
                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setDisplayName(displayName)
                            .setPhotoUri(Uri.parse("http://images.com/1.jpg"))
                            .build()

                        user?.updateProfile(profileUpdates)
                            ?.addOnCompleteListener { profileTask ->
                                if (profileTask.isSuccessful) {
                                    val updatedDisplayName = user.displayName
                                    val updatedPhotoUrl = user.photoUrl
                                    Log.d("Signup", "Username:  ${displayName} ${user?.displayName}")
                                } else {
                                    Log.d("Signup", "User name creation  ${task.exception?.message}")
                                }
                            }

                        userId?.let { storeUserData("@${displayName.lowercase()}", it) }

                        navigateToHomeScreen(navController)
                    } else {
                        Log.d("Signup", "User creation failed: ${task.exception?.message}")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("Signup", "User creation failed: ${exception.message}")
                }
        } else {
            Log.d("Signup", "Email or password is empty or null")

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




    fun saveUserData(userId: String, user: User) {
        val database = Firebase.database
        val usersRef = database.getReference("users")

        // Push user data to a new child node using the user's ID
        usersRef.child(userId).setValue(user)
            .addOnSuccessListener {
                // Data successfully saved
                println("User data saved successfully")
                Log.d("Signup", "User data saved successfully")
            }
            .addOnFailureListener { exception ->
                // Handle error
                Log.d("Signup", "Error saving user data: $exception")
            }
    }

    fun storeUserData(userSlug: String, userId: String) {
        // Reference to the Firestore database
        val db = FirebaseFirestore.getInstance()

        val userSlagCollection = db.collection("userSlag")

        // Create a document with the user slug as the document ID
        val documentRef = userSlagCollection.document(userSlug)

        // Data to be stored in the document
        val userData = hashMapOf(
            "userId" to userId
        )

        // Set the data in the document
        documentRef
            .set(userData)
            .addOnSuccessListener {
                // Document was successfully written
                println("User data stored successfully for $userSlug")
            }
            .addOnFailureListener { e ->
                // Error handling
                println("Error storing user data: ${e.message}")
            }
    }

}
