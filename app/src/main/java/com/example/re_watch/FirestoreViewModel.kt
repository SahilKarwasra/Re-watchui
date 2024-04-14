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
import com.google.firebase.storage.FirebaseStorage


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

    val db = FirebaseFirestore.getInstance()
    private val _videoList = MutableLiveData<List<Video>>()
    val videoList: LiveData<List<Video>> = _videoList

    private val _videoLikedList = MutableLiveData<List<Video>>()
    val videoLikedList: LiveData<List<Video>> = _videoLikedList

    private val _videoByUserList = MutableLiveData<List<Video>>()
    val userVideoList: LiveData<List<Video>> = _videoByUserList

    fun removeItem(indexToRemove: Int) {
        val currentList = _videoByUserList.value.orEmpty().toMutableList()
        if (indexToRemove in currentList.indices) {
            currentList.removeAt(indexToRemove)
            _videoByUserList.value = currentList
        }
    }

    fun fetchAllVideosFromFirestore() {

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
                    val likes = document.getString("like") ?: "0"
                    val dislikes = document.getString("dislike") ?: "0"

                    userIds.add(userId)

                    val video = Video(
                        videoId,
                        userId,
                        "",
                        videoUrl,
                        videoTitle,
                        videoDescription,
                        "",
                        "",
                        videoTags,
                        likes,
                        dislikes
                    )
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
                            val userDisplayName =
                                userDataSnapshot.child("displayName").getValue(String::class.java)
                                    ?: ""
                            val userProfileImage =
                                userDataSnapshot.child("profileImage").getValue(String::class.java)
                                    ?: ""
                            val userProfileUrl = userDataSnapshot.child("userProfileUrl")
                                .getValue(String::class.java) ?: ""

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
                        val likes = document.getString("like") ?: "0"
                        val dislikes = document.getString("dislike") ?: "0"


                        val video = Video(
                            videoId,
                            userId,
                            "",
                            videoUrl,
                            videoTitle,
                            videoDescription,
                            "",
                            "",
                            videoTags,
                            likes,
                            dislikes
                        )
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
                    val userDisplayName =
                        userDataSnapshot.child("displayName").getValue(String::class.java) ?: ""
                    val userProfileImage =
                        userDataSnapshot.child("profileImage").getValue(String::class.java) ?: ""
                    val userProfileUrl =
                        userDataSnapshot.child("userProfileUrl").getValue(String::class.java) ?: ""

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

    fun fetchVideosByUserId(userId: String?) {
        val userId = userId ?: ""
        val currentUser = FirebaseAuth.getInstance().currentUser
        val currUID = currentUser?.uid
        val displayName = currentUser?.displayName ?: ""
        val userProfileUrl = "@${currentUser}"
        val userProfileImage = currentUser?.photoUrl.toString()

        val usersRef = FirebaseDatabase.getInstance().getReference("users")

        db.collection("videos")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { result ->
                val videoLists = mutableListOf<Video>()
                if (currUID == userId) {
                    for (document in result) {
                        val videoId = document.id
                        val videoUrl = document.getString("videoUrl") ?: ""
                        val videoTitle = document.getString("title") ?: ""
                        val videoDescription = document.getString("description") ?: ""
                        val videoTags = document.get("videoTags") as? List<String> ?: listOf()
                        val likes = document.getString("like") ?: "0"
                        val dislikes = document.getString("dislike") ?: "0"

                        val video = Video(
                            videoId,
                            userId,
                            displayName,
                            videoUrl,
                            videoTitle,
                            videoDescription,
                            userProfileUrl,
                            userProfileImage,
                            videoTags,
                            likes,
                            dislikes
                        )
                        videoLists.add(video)

                    }
                    usersRef.child(userId)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {

                                val userProfileUrl = dataSnapshot.child("profileUrl")
                                    .getValue(String::class.java) ?: ""

                                for (video in videoLists) {
                                    video.userProfileUrl = userProfileUrl
                                }
                                Log.e("user", ":::$userProfileUrl")

                                _videoByUserList.value = videoLists
                            }

                            override fun onCancelled(databaseError: DatabaseError) {

                            }
                        })
                } else {
                    for (document in result) {
                        val videoId = document.id
                        val videoUrl = document.getString("videoUrl") ?: ""
                        val videoTitle = document.getString("title") ?: ""
                        val videoDescription = document.getString("description") ?: ""
                        val videoTags = document.get("videoTags") as? List<String> ?: listOf()
                        val likes = document.getString("like") ?: "0"
                        val dislikes = document.getString("dislike") ?: "0"

                        val video = Video(
                            videoId,
                            userId,
                            "",
                            videoUrl,
                            videoTitle,
                            videoDescription,
                            "",
                            "",
                            videoTags,
                            likes,
                            dislikes
                        )
                        videoLists.add(video)
                    }
                    // Fetch user details

                    usersRef.child(userId)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                val userDisplayName =
                                    dataSnapshot.child("displayName").getValue(String::class.java)
                                        ?: ""
                                val userProfileImage =
                                    dataSnapshot.child("profileImage").getValue(String::class.java)
                                        ?: ""
                                val userProfileUrl = dataSnapshot.child("profileUrl")
                                    .getValue(String::class.java) ?: ""

                                for (video in videoLists) {
                                    video.userDisplayName = userDisplayName
                                    video.userProfileImage = userProfileImage
                                    video.userProfileUrl = userProfileUrl
                                }
                                Log.e("user", ":::$userProfileUrl")

                                // Update _videoList LiveData
                                _videoByUserList.value = videoLists
                            }

                            override fun onCancelled(databaseError: DatabaseError) {

                            }
                        })
                }
            }
            .addOnFailureListener { exception ->
                Log.e("error", "${exception.message} error")
            }
    }


    fun deleteVideo(videoId: String, userId: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val storage = FirebaseStorage.getInstance()
        Log.d("delete", "$videoId")

        if (currentUser != null && currentUser.uid == userId) {

            getFileName(videoId) { filename ->
                if (filename.isNotEmpty()) {
                    val storageRef = storage.reference
                    val videoRef = storageRef.child("videos/$filename")
                    videoRef.delete()
                        .addOnSuccessListener {
                            Log.d("delete", "video Deleted: $filename")
                            db.collection("videos").document(videoId).delete()
                                .addOnSuccessListener {
                                    Log.d("delete", "$videoId")
                                }
                                .addOnFailureListener { e ->
                                    Log.e("delete", "delete error :: ${e.message}")
                                }
                        }
                        .addOnFailureListener { e ->
                            Log.e("delete", "delete video from  error :: ${e.message} ++$filename")
                        }

                } else {
                    Log.e("delete","Failed to fetch or extract filename $filename.")
                }
            }
        } else {
            Log.e("delete", "unauthorized ::")
        }
    }

    fun getFileName(videoId: String, callback: (String) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val videoRef = db.collection("videos").document(videoId)

        videoRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val videoUrl = documentSnapshot.getString("videoUrl") ?: ""
                    val filename = extractFilenameFromUrl(videoUrl)

                    callback(filename)
                } else {
                    callback("")
                }
            }
            .addOnFailureListener { e ->

                callback("")
            }
    }

    fun extractFilenameFromUrl(videoUrl: String): String {
        val parts = videoUrl.split("videos%2F")
        val filenamePart = parts.getOrNull(1)
        return filenamePart?.split("?")?.firstOrNull() ?: ""
    }
}
