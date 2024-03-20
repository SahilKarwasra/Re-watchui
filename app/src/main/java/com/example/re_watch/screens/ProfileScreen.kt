package com.example.re_watch.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.re_watch.R
import com.example.re_watch.components.UploadVideoPopUp
import com.example.re_watch.navigation.AppScreens
import com.google.firebase.auth.FirebaseAuth


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavHostController) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .padding(10.dp)
                    .clip(RoundedCornerShape(CornerSize(20.dp))),
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        Alignment.Center
                    ) {
                        Text(
                            stringResource(id = R.string.app_name),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Go Back",
                        modifier = Modifier
                            .size(30.dp)
                            .clickable {
                                navController.popBackStack()
                            }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF20A6E2),
                    titleContentColor = Color(0xFFFFFFFF),
                    actionIconContentColor = Color(0xFFFCFCFC)
                ),
                scrollBehavior = scrollBehavior
            )
        },
    ) {
        Surface(modifier = Modifier.padding(it)) {
            MainProfileScreen(navController)
        }
    }
}




@Composable
fun MainProfileScreen(navController: NavHostController) {
    var dialogVisible by remember { mutableStateOf(false) }



    Surface() {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = R.drawable.profilepng),
                contentDescription = "Profile pic",
                tint = Color.Unspecified,
                modifier = Modifier
                    .padding(top = 40.dp, bottom = 20.dp)
                    .size(100.dp)
                    .clickable {

                    }
            )
            Text(
                text = "Email: ${FirebaseAuth.getInstance().currentUser?.email}",
                modifier = Modifier
                    .padding(start = 20.dp, top = 10.dp, bottom = 10.dp)
                    .width(300.dp),
                color = Color.DarkGray,
                fontSize = 18.sp
            )
            FirebaseAuth.getInstance().currentUser?.displayName?.let {
                Text(
                    text = "Username: $it",
                    modifier = Modifier
                        .padding(start = 20.dp, top = 10.dp, bottom = 10.dp)
                        .width(300.dp),
                    color = Color.DarkGray,
                    fontSize = 18.sp
                )
            }
            
            Button(
                onClick = {
                    dialogVisible = true
                },
                shape = MaterialTheme.shapes.large,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4372F1)
                ),
                modifier = Modifier
                    .padding(top = 20.dp, bottom = 5.dp)
                    .width(100.dp)
                    .height(52.dp)
            ) {

                Text(
                    text = "Upload Video",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight(500),
                        color = Color.White
                    )
                )
            }
            
            Button(
                onClick = { /*TODO*/ },
                shape = MaterialTheme.shapes.large,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4372F1)
                ),
                modifier = Modifier
                    .padding(top = 20.dp, bottom = 5.dp)
                    .width(100.dp)
                    .height(52.dp)
            ) {
                Text(text = "Remove Videos")
            }
            
            Button(
                onClick = {
                    FirebaseAuth.getInstance().signOut()
                    navController.popBackStack(AppScreens.HomeScreen.route, inclusive = true)
                    navController.popBackStack(AppScreens.ProfileScreen.route, inclusive = true)
                    navController.navigate(route = AppScreens.WelcomeScreen.route)

                },
                shape = MaterialTheme.shapes.large,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4372F1)
                ),
                modifier = Modifier
                    .padding(top = 20.dp, bottom = 5.dp)
                    .width(100.dp)
                    .height(52.dp)
            ) {

                Text(
                    text = "SignOut",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight(500),
                        color = Color.White
                    )
                )
            }
            if (dialogVisible) {
                UploadVideoPopUp(onDismiss = { dialogVisible = false })
            }
        }
    }
}

//fun fetchUsernameFromDatabase(onSuccess: (String) -> Unit, onFailure: (String) -> Unit) {
//    val user = FirebaseAuth.getInstance().currentUser
//    val userId = user?.uid ?: ""
//
//    val database = FirebaseDatabase.getInstance()
//    val usersRef = database.getReference("users")
//
//    usersRef.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
//        override fun onDataChange(snapshot: DataSnapshot) {
//            val username = snapshot.child("username").getValue(String::class.java)
//            if (username != null) {
//                onSuccess(username)
//            } else {
//                onFailure("Username not found")
//            }
//        }
//
//        override fun onCancelled(error: DatabaseError) {
//            onFailure("Failed to retrieve username: ${error.message}")
//        }
//    })
//}
//


//val user = FirebaseAuth.getInstance().currentUser
//val userId = user?.uid ?: ""
//val  photoUrl
//val profileUpdates = UserProfileChangeRequest.Builder()
//    .setPhotoUri(Uri.parse(photoUrl))
//    .build()

//user?.updateProfile(profileUpdates)
//?.addOnCompleteListener { profileTask ->
//    if (profileTask.isSuccessful) {
//
//        val updatedUser = FirebaseAuth.getInstance().currentUser
//        val updatedDisplayName = updatedUser?.displayName
//
//    } else {
//    }
//}