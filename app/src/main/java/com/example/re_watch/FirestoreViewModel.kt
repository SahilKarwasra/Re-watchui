package com.example.re_watch

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Video(
    val videoId: String,
    val userId: String,
    val userEmail: String,
    val userDisplayName: String,
    val uploadTime: String,
    val videoUrl: String,
    val videoTitle: String,
    val videoDescription: String,
    val userProfileUrl: String,
    val userPhotoUrl: String,
    val videotags: List<String>
)

class FirestoreViewModel : ViewModel() {


    private val _videoList = MutableLiveData<List<Video>>()
    val videoList: LiveData<List<Video>> = _videoList

    private val _videoLikedList = MutableLiveData<List<Video>>()
    val videoLikedList: LiveData<List<Video>> = _videoLikedList


    fun fetchAllVideosFromFirestore() {
        val db = FirebaseFirestore.getInstance()

        db.collection("videos")
            .get()
            .addOnSuccessListener { result ->
                val videoLists = mutableListOf<Video>()
                for (document in result) {
                    val videoId = document.id
                    val userId = document.getString("userId") ?: ""
                    val userEmail = document.getString("userEmail") ?: ""
                    val userDisplayName = document.getString("userDisplayName") ?: ""
                    val uploadTimeTimestamp = document.getTimestamp("uploadTime") ?: Timestamp.now()
                    val uploadTime = convertTimestampToDate(uploadTimeTimestamp)
                    val videoUrl = document.getString("videoUrl") ?: ""
                    val videoTitle = document.getString("title") ?: ""
                    val userProfileUrl = document.getString("userProfileUrl") ?: ""
                    val userPhotoUrl = document.getString("userPhotoUrl") ?: ""
                    val videoDescription = document.getString("description") ?: ""
                    val videoTags = document.get("videoTags") as? List<String> ?: listOf()



                    val video = Video(videoId,userId,userEmail, userDisplayName, uploadTime, videoUrl, videoTitle, videoDescription, userProfileUrl, userPhotoUrl,videoTags)
                    videoLists.add(video)
                }
                _videoList.value = videoLists
            }
            .addOnFailureListener { exception ->
                Log.e("error" ,"${exception.message} error")
            }
    }



    fun fetchLikedVideos() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val db = FirebaseFirestore.getInstance()
        val userActionsRef = db.collection("userActions")

        userActionsRef
            .whereEqualTo("userId", userId)
            .whereEqualTo("interactionType", "like")
            .get()
            .addOnSuccessListener { documents ->
                val videoIds = documents.mapNotNull { it.getString("videoId") }
                fetchVideosByIds(videoIds)
            }
            .addOnFailureListener { exception ->
                // Handle any errors
                println("Error getting liked videos: $exception")
            }
    }

    private fun fetchVideosByIds(videoIds: List<String>) {
        val db = FirebaseFirestore.getInstance()
        val videosRef = db.collection("videos")

        val videoLists = mutableListOf<Video>()

        for (videoId in videoIds) {
            videosRef.document(videoId).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val userId = document.getString("userId") ?: ""
                        val userEmail = document.getString("userEmail") ?: ""
                        val userDisplayName = document.getString("userDisplayName") ?: ""
                        val uploadTimeTimestamp = document.getTimestamp("uploadTime") ?: Timestamp.now()
                        val uploadTime = convertTimestampToDate(uploadTimeTimestamp)
                        val videoUrl = document.getString("videoUrl") ?: ""
                        val videoTitle = document.getString("title") ?: ""
                        val userProfileUrl = document.getString("userProfileUrl") ?: ""
                        val userPhotoUrl = document.getString("userPhotoUrl") ?: ""
                        val videoDescription = document.getString("description") ?: ""
                        val videoTags = document.get("videoTags") as? List<String> ?: listOf()


                        val video = Video(videoId, userId, userEmail, userDisplayName, uploadTime, videoUrl, videoTitle, videoDescription, userProfileUrl, userPhotoUrl,videoTags)
                        videoLists.add(video)

                        _videoLikedList.value = videoLists
                    }
                }
                .addOnFailureListener { exception ->
                    println("Error getting video with ID $videoId: $exception")
                }
        }
    }

}
fun convertTimestampToDate(timestamp: Timestamp): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val date = Date(timestamp.seconds * 1000L + timestamp.nanoseconds / 1000000)
    return sdf.format(date)
}


