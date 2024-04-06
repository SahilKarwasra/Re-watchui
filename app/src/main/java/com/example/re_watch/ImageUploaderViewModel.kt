package com.example.re_watch

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class ImageUploaderViewModel : ViewModel() {
    private val TAG = ImageUploaderViewModel::class.simpleName

    private val storage = Firebase.storage
    private val auth = Firebase.auth
    private val currentUser = auth.currentUser
    var uploaded = mutableStateOf(false)

    fun uploadProfileImageToFirebase(imageUri: Uri) {
        currentUser?.let { user ->

            val profileImageRef = storage.reference.child("profile_images/${user.uid}")

            // Delete existing profile image if exists
            profileImageRef.delete().addOnSuccessListener {

                profileImageRef.putFile(imageUri)
                    .addOnSuccessListener { _ ->

                        profileImageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                            uploadImageUri(downloadUri)

                        }.addOnFailureListener { exception ->
                            // Handle any errors that may occur while getting the download URL
                            Log.e("FirebaseUpload", "Error getting download URL", exception)
                            uploaded.value = false

                        }


                    }
                    .addOnFailureListener { exception ->
                        Log.d("image","image upload error:  ${exception.message}")
                        uploaded.value = false
                    }
            }.addOnFailureListener {

                profileImageRef.putFile(imageUri)
                    .addOnSuccessListener { _ ->
                        profileImageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                            uploadImageUri(downloadUri)

                        }.addOnFailureListener { exception ->

                            Log.e("FirebaseUpload", "Error getting download URL", exception)
                            uploaded.value = false
                        }

                    }
                    .addOnFailureListener { exception ->
                        Log.d("image","first time image upload error:  ${exception.message}")
                        uploaded.value = false
                    }
            }
        }
    }

    fun uploadImageUri(imageUri: Uri) {
        val user = FirebaseAuth.getInstance().currentUser
        val userId = user?.uid
        val database = Firebase.database
        val usersRef = database.getReference("users")

        val profileUpdates = UserProfileChangeRequest.Builder()
            .setPhotoUri(imageUri)
            .build()

        user?.updateProfile(profileUpdates)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("image","image upload new one ")
                    uploaded.value = true
                } else {
                    Log.d("image","image upload new one error:  ${task.exception?.message}")
                    uploaded.value = false
                }
            }

        val updatedUserData = hashMapOf<String, Any>(
            "profileImage" to imageUri.toString()
        )


        userId?.let {
            usersRef.child(it).updateChildren(updatedUserData)
                .addOnSuccessListener {
                    Log.d("image", "User profile image URL updated in the database")
                }
                .addOnFailureListener { exception ->
                    Log.e("image", "Error updating user profile image URL in the database", exception)
                }
        }
    }


}

