package com.example.re_watch

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.google.firebase.firestore.FirebaseFirestore

var usernameDoesNotExist =   mutableStateOf(true)
class Validator {
    companion object {

        fun validateProfileusername(profileusername: String, validationCallback: (Boolean) -> Unit) {
            isExistInFirebase(profileusername) { exists ->
                Log.e("Username", "$exists")
                validationCallback(exists)
            }
        }

        fun validateUsername(username: String): Boolean {

            return (!username.isNullOrEmpty()&& username.length >=2)
        }
        fun validateEmail(email: String): Boolean {

            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        fun validatePassword(password: String): Boolean {

            return (!password.isNullOrEmpty() && password.length >= 8)
        }
        fun validatePassForLogin(password: String):Boolean{
            return (!password.isNullOrEmpty())
        }
    }
}
private fun isExistInFirebase(profileusername: String, callback: (Boolean) -> Unit) {
    val firestore = FirebaseFirestore.getInstance()
    val userSlagRef = firestore.collection("userSlag")

    userSlagRef.document("@$profileusername").get()
        .addOnSuccessListener { documentSnapshot ->
            // If document snapshot is not null, username exists
            val exists = documentSnapshot.exists()
            Log.d("Username", "$profileusername' existence checked. Exists: $exists")
            callback(!exists)
        }
        .addOnFailureListener { exception ->
            // Log any errors
            Log.d("Username", "Error checking username existence: $exception")
            callback(true) // Assume username exists in case of failure
        }
}
