package com.example.re_watch

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.re_watch.data.Comment
import com.example.re_watch.data.CommentUIEvent
import com.example.re_watch.data.CommentUIState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class CommentViewModel : ViewModel() {


    private val TAG = CommentViewModel::class.simpleName

    var commentUIState = mutableStateOf(CommentUIState())

    private val db = FirebaseFirestore.getInstance()
    lateinit var userId :String
    lateinit var videoId : String
    val orgUserId = FirebaseAuth.getInstance().currentUser?.uid
    fun getVideoAndUserId(videoId: String, userId: String ) {
        this.userId = userId
        this.videoId = videoId
    }


    fun onEvent(event: CommentUIEvent){
        when(event){

            is CommentUIEvent.CommentChanged ->{
                commentUIState.value = commentUIState.value.copy(
                    comment = event.comment
                )
            }

            is CommentUIEvent.CommentButtonClicked ->{
                commentVideo()
            }

            else -> {

            }
        }
    }

    fun commentVideo() {
        val comment = commentUIState.value.comment

        if(comment.isNotEmpty()){
            viewModelScope.launch {
                val videoRef = db.collection("videos").document(videoId)
                db.runTransaction { transaction ->
                    val videoDoc = transaction.get(videoRef)
                    var comments = videoDoc.get("comments") as? MutableList<HashMap<String, Any>>
                    if (comments == null) {
                        comments = mutableListOf()
                    }
                    val newComment: HashMap<String, Any> = hashMapOf(
                        "userId" to userId,
                        "comment" to comment

                    )
                    comments.add(newComment)
                    transaction.update(videoRef, "comments", comments)
                }.addOnSuccessListener {
                    // clear the text field
                    commentUIState.value = commentUIState.value.copy(comment = "")
                    fetchComments()
                }
            }
        }
        else{
            //enter value in the text field error
        }
    }
    fun fetchComments() {
        val videoRef = db.collection("videos").document(videoId)
        videoRef.get().addOnSuccessListener { document ->
            val comments = document["comments"] as? List<HashMap<String, Any>> ?: emptyList()
            val fetchedComments = mutableListOf<Comment>()

            // Fetch user data for comment user ID
            for (commentMap in comments) {
                val userId = commentMap["userId"].toString()

                // data fetch from realtime database
                FirebaseDatabase.getInstance().getReference("users").child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val userName = dataSnapshot.child("displayName").getValue(String::class.java) ?: ""
                        val userProfileImage = dataSnapshot.child("profileImage").getValue(String::class.java) ?: ""
                        val comment = Comment(
                            userId = userId,
                            comment = commentMap["comment"].toString(),
                            userName = userName,
                            userProfileImage = userProfileImage
                        )
                        fetchedComments.add(0,comment)


                        if (fetchedComments.size == comments.size) {
                            commentUIState.value = commentUIState.value.copy(comments = fetchedComments)
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Handle failure
                    }
                })
            }
        }.addOnFailureListener { exception ->
            // Handle fetch failure
        }
    }

    fun editComment(commentId: String, newComment: String) {
        val videoRef = db.collection("videos").document(videoId)
        videoRef.get().addOnSuccessListener { document ->
            val comments = document["comments"] as? List<HashMap<String, Any>> ?: emptyList()
            val updatedComments = comments.map { commentMap ->
                if (commentMap["commentId"] == commentId && commentMap["userId"] == orgUserId) {
                    commentMap.toMutableMap().apply {
                        this["comment"] = newComment
                    }
                } else {
                    commentMap
                }
            }
            videoRef.update("comments", updatedComments)
        }.addOnFailureListener { exception ->
            // Handle fetch failure
        }
    }

    fun deleteComment(commentId: String) {
        val videoRef = db.collection("videos").document(videoId)
        videoRef.get().addOnSuccessListener { document ->
            val comments = document["comments"] as? List<HashMap<String, Any>> ?: emptyList()
            val updatedComments = comments.filterNot { commentMap ->
                commentMap["commentId"] == commentId && commentMap["userId"] == orgUserId
            }
            videoRef.update("comments", updatedComments)
        }.addOnFailureListener { exception ->
            // Handle fetch failure
        }
    }


}


