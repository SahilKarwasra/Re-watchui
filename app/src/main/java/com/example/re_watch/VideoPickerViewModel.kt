package com.example.re_watch

import androidx.lifecycle.ViewModel
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.Calendar
import java.util.UUID

class VideoPickerViewModel : ViewModel() {
    fun onVideoSelected(context: Context, uri: Uri) {
        Log.d("uri",uri.toString())
        uploadVideo(context,uri)

    }
    fun uploadVideo(context: Context, uri: Uri) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: return
        val userDisplayName = FirebaseAuth.getInstance().currentUser?.displayName ?: "Unknown"
        val currentTime = Calendar.getInstance().time


        val storageReference = FirebaseStorage.getInstance().reference
        val videoRef: StorageReference = storageReference.child("videos/${UUID.randomUUID()}")


        val db = FirebaseFirestore.getInstance()
        val videoInfo = hashMapOf(
            "userId" to userId,
            "userEmail" to userEmail,
            "userDisplayName" to userDisplayName,
            "uploadTime" to currentTime
        )

        // Upload video to Firebase Storage
        videoRef.putFile(uri)
            .addOnSuccessListener { taskSnapshot ->

                Toast.makeText(
                    context,
                    "Upload successful",
                    Toast.LENGTH_LONG)
                    .show()

                Log.d("Upload", "Upload successful")

                // Get the download URL of the uploaded video
                taskSnapshot.storage.downloadUrl.addOnSuccessListener { downloadUri ->
                    // Add download URL to videoInfo
                    videoInfo["videoUrl"] = downloadUri.toString()

                    // Save videoInfo to Firestore
                    db.collection("videos").add(videoInfo)
                        .addOnSuccessListener { documentReference ->
                            Log.d("Firestore", "DocumentSnapshot added with ID: ${documentReference.id}")
                        }
                        .addOnFailureListener { e ->
                            Log.e("Firestore", "Error adding document${e}")
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.e("Upload", "Upload failed", e)
                Toast.makeText(
                    context,
                    "Upload failed",
                    Toast.LENGTH_LONG)
                    .show()
            }
    }
}
