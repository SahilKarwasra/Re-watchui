package com.example.re_watch

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.re_watch.data.UploadUIEvent
import com.example.re_watch.data.UploadUiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.Calendar
import java.util.UUID

class VideoPickerViewModel : ViewModel() {

    private val TAG = VideoPickerViewModel::class.simpleName

    var uploadValidate = mutableStateOf(false)

    var uploadUIState = mutableStateOf(UploadUiState())


    var uploadInProgress = mutableStateOf(false)

    private lateinit var context: Context


    fun onVideoSelect(context: Context) {
        this.context = context
    }


    fun onEvent(event: UploadUIEvent){
        when(event){
            is UploadUIEvent.VideoUriChanged ->{
                uploadUIState.value = uploadUIState.value.copy(
                    videoUri = event.videoUriSelect
                )
            }
            is UploadUIEvent.TitleChanged ->{
                uploadUIState.value = uploadUIState.value.copy(
                    title = event.videoTitle
                )
            }
            is UploadUIEvent.DescriptionChanged ->{
                uploadUIState.value = uploadUIState.value.copy(
                    description = event.videoDescription
                )
            }
            is UploadUIEvent.UploadButtonClicked ->{
                uploadVideo()
            }

            else -> {

            }
        }
    }



    fun uploadVideo() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: return
        val userDisplayName = FirebaseAuth.getInstance().currentUser?.displayName ?: "Unknown"
        val currentTime = Calendar.getInstance().time
        val title = uploadUIState.value.title
        val description = uploadUIState.value.description
        val uri = Uri.parse(uploadUIState.value.videoUri)
        val photoUrl = FirebaseAuth.getInstance().currentUser?.photoUrl
        val userProfileUrl = "https://re-watch.com/${FirebaseAuth.getInstance().currentUser?.displayName}"

        val storageReference = FirebaseStorage.getInstance().reference
        val videoRef: StorageReference = storageReference.child("videos/${UUID.randomUUID()}")


        val db = FirebaseFirestore.getInstance()
        val videoInfo = hashMapOf(
            "userId" to userId,
            "userEmail" to userEmail,
            "userDisplayName" to userDisplayName,
            "uploadTime" to currentTime,
            "title" to title,
            "description" to description,
            "photoUrl" to photoUrl,
            "userProfileUrl" to userProfileUrl

        )
        Log.d("Upload", "Uri.parse(uri.value)")

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

                    videoInfo["videoUrl"] = downloadUri.toString()


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
