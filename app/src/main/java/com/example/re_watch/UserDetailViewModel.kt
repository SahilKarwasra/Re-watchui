package com.example.re_watch

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore

data class UserDetails(
    val userID: String,
    val userDisplayName: String,
    val userProfileUrl:String,
    val userProfileImage: String
)
class UserDetailViewModel : ViewModel() {

    lateinit var context: Context
    lateinit var navController: NavHostController
    val userDetails = MutableLiveData<UserDetails>()
    var userIdget = mutableStateOf("")

    fun setNavController(navController: NavHostController, context: Context) {
        this.navController = navController
        this.context = context
    }

    fun fetchUserDetails(userId: String,userSlug: String) {
        userIdget.value = userId
        if(userIdget.value.isNullOrEmpty()){
            Log.d("user","${userId}+${userSlug}")
            fetchUserId(userSlug)
            Log.d("user","${userId}+${userIdget.value}")
        }
        val usersRef = FirebaseDatabase.getInstance().getReference("users")
        Log.d("user","${userId}+${userIdget.value}")
        usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userDataSnapshot = dataSnapshot.child(userIdget.value)
                val userDisplayName = userDataSnapshot.child("displayName").getValue(String::class.java) ?: ""
                val userProfileImage = userDataSnapshot.child("profileImage").getValue(String::class.java) ?: ""
                val userProfileUrl = userDataSnapshot.child("userProfileUrl").getValue(String::class.java) ?: ""

                userDetails.value = UserDetails(userIdget.value,userDisplayName,userProfileUrl, userProfileImage)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle failure
            }
        })
    }


    fun fetchUserId(userSlugId: String){
        val db = FirebaseFirestore.getInstance()
        val userSlag = db.collection("userSlag")

        userSlag.document(userSlugId).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val userIdfetch = document.getString("userId") ?: ""
                    userIdget.value= userIdfetch
                    Log.d("user","::::::${userIdget.value}")
                }
            }
            .addOnFailureListener { exception ->
                // Handle any errors
                Log.d("user", "${userSlugId} ++:++ ${exception}")
            }
    }
}
