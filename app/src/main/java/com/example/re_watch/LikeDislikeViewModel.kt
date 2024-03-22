package com.example.re_watch

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LikeDislikeViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    var likes = mutableStateOf("")
    var dislikes = mutableStateOf("")

    suspend fun hasUserInteracted(userId: String, videoId: String): String? {
        val querySnapshot = db.collection("userActions")
            .whereEqualTo("userId", userId)
            .whereEqualTo("videoId", videoId)
            .get()
            .await()

        return querySnapshot.documents.firstOrNull()?.getString("interactionType")
    }

    // Function to record the user's interaction with the video (like or dislike)
    suspend fun recordUserInteraction(userId: String, videoId: String, interactionType: String) {
        val userAction = hashMapOf(
            "userId" to userId,
            "videoId" to videoId,
            "interactionType" to interactionType
        )
        db.collection("userActions").document("$userId-$videoId").set(userAction).await()
    }

    // Function to undo the user's like interaction with the video
    fun deleteLikeVideo(userId: String, videoId: String) {
        viewModelScope.launch {
            if (hasUserInteracted(userId, videoId) == "like") {
                db.collection("userActions")
                    .whereEqualTo("userId", userId)
                    .whereEqualTo("videoId", videoId)
                    .whereEqualTo("interactionType", "like")
                    .get()
                    .await()
                    .documents
                    .forEach { document ->
                        document.reference.delete().await()
                    }
            }
        }
    }

    // Function to undo the user's dislike interaction with the video
    fun deleteDislikeVideo(userId: String, videoId: String) {
        viewModelScope.launch {
            if (hasUserInteracted(userId, videoId) == "dislike") {
                db.collection("userActions")
                    .whereEqualTo("userId", userId)
                    .whereEqualTo("videoId", videoId)
                    .whereEqualTo("interactionType", "dislike")
                    .get()
                    .await()
                    .documents
                    .forEach { document ->
                        document.reference.delete().await()
                    }
            }
        }
    }

    // Function to handle liking a video
    fun likeVideo(userId: String, videoId: String) {
        viewModelScope.launch {
            val videoRef = db.collection("videos").document(videoId)
            val existingInteractionType = hasUserInteracted(userId, videoId)
            existingInteractionType?.let { Log.d("like", it) }
            // If the user hasn't liked the video yet, record the like interaction
            if (existingInteractionType != "like") {

                if(existingInteractionType == "dislike"){
                    Log.d("like", "if alrady disliked${existingInteractionType}")
                    recordUserInteraction(userId, videoId, "like")
                    db.runTransaction { transaction ->
                        val snapshot = transaction.get(videoRef)
                        val currentDislikes = (snapshot.getString("dislike")?.toIntOrNull() ?: 0)

                        transaction.update(videoRef, "dislike", (currentDislikes - 1).toString())
                        currentDislikes - 1
                        likeAndDislikeCount(videoId,false)

                        val currentLikes = (snapshot.getString("like")?.toIntOrNull() ?: 0)

                        transaction.update(videoRef, "like", (currentLikes + 1).toString())
                        currentLikes + 1
                        likeAndDislikeCount(videoId,true)

                    }
                }
                else{
                    Log.d("like", "if not liked${existingInteractionType}")
                    recordUserInteraction(userId, videoId, "like")
                    db.runTransaction { transaction ->
                        val snapshot = transaction.get(videoRef)
                        val currentLikes = (snapshot.getString("like")?.toIntOrNull() ?: 0)

                        // Increment likeCount by 1
                        transaction.update(videoRef, "like", (currentLikes + 1).toString())
                        currentLikes + 1

                        likeAndDislikeCount(videoId,true)
                    }
                }
            }
            if(existingInteractionType == "like"){
                deleteLikeVideo(userId,videoId)
                db.runTransaction { transaction ->
                    val snapshot = transaction.get(videoRef)
                    val currentLikes = (snapshot.getString("like")?.toIntOrNull() ?: 0)

                    // Increment likeCount by 1
                    transaction.update(videoRef, "like", (currentLikes - 1 ).toString())
                    currentLikes - 1

                    likeAndDislikeCount(videoId,true)
                }
            }
        }
    }

    // Function to handle disliking a video
    fun dislikeVideo(userId: String, videoId: String) {
        viewModelScope.launch {
            val existingInteractionType = hasUserInteracted(userId, videoId)

            val videoRef = db.collection("videos").document(videoId)
            // If the user hasn't disliked the video yet, record the dislike interaction
            if (existingInteractionType != "dislike") {

                if(existingInteractionType == "like"){
                    recordUserInteraction(userId, videoId, "dislike")
                    db.runTransaction { transaction ->
                        val snapshot = transaction.get(videoRef)
                        val currentlikes= (snapshot.getString("like")?.toIntOrNull() ?: 0)

                        transaction.update(videoRef, "like", (currentlikes - 1).toString())
                        currentlikes - 1
                        likeAndDislikeCount(videoId,true)

                        val currentDislikes = (snapshot.getString("dislike")?.toIntOrNull() ?: 0)

                        transaction.update(videoRef, "dislike", (currentDislikes + 1).toString())
                        currentDislikes + 1
                        likeAndDislikeCount(videoId,false)
                    }
                }
                else{
                    recordUserInteraction(userId, videoId, "dislike")
                    db.runTransaction { transaction ->
                        val snapshot = transaction.get(videoRef)
                        val currentDislikes = (snapshot.getString("dislike")?.toIntOrNull() ?: 0)

                        // Increment dislikeCount by 1
                        transaction.update(videoRef, "dislike", (currentDislikes + 1).toString())
                        currentDislikes + 1
                        likeAndDislikeCount(videoId,false)
                    }
                }
            }
            if(existingInteractionType == "dislike"){
                deleteDislikeVideo(userId,videoId)
                db.runTransaction { transaction ->
                    val snapshot = transaction.get(videoRef)
                    val currentDislikes = (snapshot.getString("dislike")?.toIntOrNull() ?: 0)

                    // Increment dislikeCount by 1
                    transaction.update(videoRef, "dislike", (currentDislikes - 1 ).toString())
                    currentDislikes - 1
                    likeAndDislikeCount(videoId,false)
                }
            }
        }
    }
    fun likeAndDislikeCount(videoId: String, likeorNot: Boolean) {
        val db = FirebaseFirestore.getInstance()

        val videoRef = db.collection("videos").document(videoId)

        db.runTransaction { transaction ->
            val snapshot = transaction.get(videoRef)

            // Retrieve the appropriate count based on the value of likeorNot
            if (likeorNot) {
                var current = snapshot.getString("like") ?: "0" // Default to "0" if likeCount is null
                likes.value = current
                Log.d("likes", "${current}")
            } else {
                var current =snapshot.getString("dislike") ?: "0" // Default to "0" if dislikeCount is null
                dislikes.value = current
                Log.d("dislikes", "${current}")
            }


        }

    }
}
