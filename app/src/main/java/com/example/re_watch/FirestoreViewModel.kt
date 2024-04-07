package com.example.re_watch

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore


data class Video(
    var videoId: String,
    var userId:String,
    var userDisplayName: String,
    var videoUrl: String,
    var videoTitle: String,
    var videoDescription: String,
    var userProfileUrl: String,
    var userProfileImage: String,
    var videotags: List<String>,
    var likes: String,
    var dislikes: String,
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
                val userIds = mutableSetOf<String>() // To store unique user ids

                // Collect video data and user ids
                for (document in result) {
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
                        _videoList.value = videoLists
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
        val userIds = mutableSetOf<String>()
        val videoLists = mutableListOf<Video>()

        for (videoId in videoIds) {
            videosRef.document(videoId).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val userId = document.getString("userId") ?: ""
                        val videoUrl = document.getString("videoUrl") ?: ""
                        val videoTitle = document.getString("title") ?: ""
                        val videoDescription = document.getString("description") ?: ""
                        val videoTags = document.get("videoTags") as? List<String> ?: listOf()
                        val likes = document.getString("like") ?:"0"
                        val dislikes = document.getString("dislike") ?:"0"


                        val video = Video(videoId, userId, "", videoUrl, videoTitle, videoDescription, "", "",videoTags,likes,dislikes)
                        videoLists.add(video)
                        userIds.add(userId)
                        _videoLikedList.value = videoLists
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle any errors
                    println("Error getting video with ID $videoId: $exception")
                }
        }

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
                _videoLikedList.value = videoLists
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle failure
            }
        })

    }

}



