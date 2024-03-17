package com.example.re_watch

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

data class Video(
    val userId: String,
    val userEmail: String,
    val userDisplayName: String,
    val uploadTime: String, // Change this to Date type
    val videoUrl: String
)

class FirestoreViewModel : ViewModel() {

    private val _videoList = MutableLiveData<List<Video>>()
    val videoList: LiveData<List<Video>> = _videoList

    fun fetchAllVideosFromFirestore() {
        val db = FirebaseFirestore.getInstance()

        db.collection("videos")
            .get()
            .addOnSuccessListener { result ->
                val videoLists = mutableListOf<Video>()
                for (document in result) {
                    val userId = document.getString("userId") ?: ""
                    val userEmail = document.getString("userEmail") ?: ""
                    val userDisplayName = document.getString("userDisplayName") ?: ""
                    val uploadTimeTimestamp = document.getTimestamp("uploadTime") ?: Timestamp.now()
                    val uploadTime = convertTimestampToDate(uploadTimeTimestamp)
                    val videoUrl = document.getString("videoUrl") ?: ""

                    val video = Video(userId, userEmail, userDisplayName, uploadTime, videoUrl)
                    videoLists.add(video)
                }
                _videoList.value = videoLists // Update the LiveData with the fetched videoList
            }
            .addOnFailureListener { exception ->
                // Handle failure
            }
    }

    private fun convertTimestampToDate(timestamp: Timestamp): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = Date(timestamp.seconds * 1000L + timestamp.nanoseconds / 1000000)
        return sdf.format(date)
    }
}


