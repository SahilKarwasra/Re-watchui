package com.example.re_watch

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore

class SearchViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val _videoSearchList = MutableLiveData<List<Video>>()
    val videoSearchList: LiveData<List<Video>> = _videoSearchList
    fun searchVideos(searchQuery: String) {
        searchQuery.lowercase()
        db.collection("videos")
            .whereArrayContains("tags", searchQuery) // Search by tags
            .get()
            .addOnSuccessListener { documents ->
                val videoLists = mutableListOf<Video>()
                for (document in documents) {
                    // Process each document
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
                    Log.d("check","Video found: ${ videoId } - Title: ${videoTitle} - User: ${userDisplayName}")
                }
                _videoSearchList.value = videoLists
            }
            .addOnFailureListener { exception ->
                // Handle any errors
                Log.d("check","Error searching videos: $exception")
            }
    }
}
