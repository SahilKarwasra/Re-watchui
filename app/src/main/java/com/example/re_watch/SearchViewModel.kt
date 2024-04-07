package com.example.re_watch

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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
                val userIds = mutableSetOf<String>() // To store unique user ids

                // Collect video data and user ids
                for (document in documents) {
                    val videoId = document.id
                    val userId = document.getString("userId") ?: ""
                    val videoUrl = document.getString("videoUrl") ?: ""
                    val videoTitle = document.getString("title") ?: ""
                    val videoDescription = document.getString("description") ?: ""
                    val videoTags = document.get("videoTags") as? List<String> ?: listOf()
                    val likes = document.getString("like") ?:"0"
                    val dislikes = document.getString("dislike") ?:"0"


                    userIds.add(userId)

                    val video = Video(videoId, userId, "", videoUrl, videoTitle, videoDescription, "", "", videoTags,likes,dislikes)
                    videoLists.add(video)
                }

                // Fetch user details for each unique user id
                val usersRef = FirebaseDatabase.getInstance().getReference("users")
                val userDisplayNameMap = mutableMapOf<String, String>()
                val userProfileImageMap = mutableMapOf<String, String>()
                val userProfileUrlMap = mutableMapOf<String, String>()

                usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        // Collect user details
                        for (userId in userIds) {
                            val userDataSnapshot = dataSnapshot.child(userId)
                            val userDisplayName = userDataSnapshot.child("displayName").getValue(String::class.java) ?: ""
                            val userProfileImage = userDataSnapshot.child("profileImage").getValue(String::class.java) ?: ""
                            val userProfileUrl = userDataSnapshot.child("userProfileUrl").getValue(String::class.java) ?: ""

                            userDisplayNameMap[userId] = userDisplayName
                            userProfileImageMap[userId] = userProfileImage
                            userProfileUrlMap[userId] = userProfileUrl
                        }

                        // Update videoList with fetched user details
                        for (video in videoLists) {
                            val userId = video.userId
                            video.userDisplayName = userDisplayNameMap[userId] ?: ""
                            video.userProfileImage = userProfileImageMap[userId] ?: ""
                            video.userProfileUrl = userProfileUrlMap[userId] ?: ""
                        }

                        // Update _videoList LiveData
                        _videoSearchList.value = videoLists
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Handle failure
                    }
                })
            }
            .addOnFailureListener { exception ->
                Log.e("error", "${exception.message} error")
            }

            }
    }

