package com.example.re_watch.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.re_watch.LoginViewModel
import com.example.re_watch.R
import com.example.re_watch.VideoPickerViewModel
import com.example.re_watch.isPermanentlyDenied
import com.example.re_watch.navigation.AppScreens
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


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



@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainProfileScreen(navController: NavHostController) {
    var userName by remember { mutableStateOf("") }
    fetchUsernameFromDatabase(
        onSuccess = { username ->
            // Use the username here
            userName = username
        },
        onFailure = { errorMessage ->
            // Handle the failure here
            userName  = errorMessage
        }
    )


    val permissionState = rememberPermissionState(permission = Manifest.permission.READ_MEDIA_VIDEO)
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(
        key1 = lifecycleOwner,
        effect = {
            val observer = LifecycleEventObserver{_, event ->
                if(event == Lifecycle.Event.ON_START){
                    permissionState.launchPermissionRequest()
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    )

    val context = LocalContext.current
    val viewModelvideo: VideoPickerViewModel = viewModel()
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                viewModelvideo.onVideoSelected(context ,uri)
            }
        }
    )
    Surface() {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${FirebaseAuth.getInstance().currentUser?.email}",
                modifier = Modifier
                    .background(color = Color.Black)
                    .width(300.dp),
                color = Color.White
            )
            Text(
                text = userName,
                modifier = Modifier
                    .background(color = Color.Black)
                    .width(300.dp),
                color = Color.White
            )
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
            Spacer(modifier = Modifier.padding(bottom = 20.dp))
            Button(
                onClick = {
                    if(permissionState.status.isGranted){
                        launcher.launch("video/*")
                    }
                    else if (permissionState.isPermanentlyDenied()){
                        Toast.makeText(
                            context,
                            "Grant Permission in App Settings",
                            Toast.LENGTH_LONG)
                            .show()
                    }
                    else{
                        Toast.makeText(
                            context,
                            " Permission Denied",
                            Toast.LENGTH_LONG)
                            .show()
                    }

                    
                },
                shape = MaterialTheme.shapes.large,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4372F1)
                ),
                modifier = Modifier
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
        }
    }
}

fun fetchUsernameFromDatabase(onSuccess: (String) -> Unit, onFailure: (String) -> Unit) {
    val user = FirebaseAuth.getInstance().currentUser
    val userId = user?.uid ?: ""

    val database = FirebaseDatabase.getInstance()
    val usersRef = database.getReference("users")

    usersRef.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val username = snapshot.child("username").getValue(String::class.java)
            if (username != null) {
                onSuccess(username)
            } else {
                onFailure("Username not found")
            }
        }

        override fun onCancelled(error: DatabaseError) {
            onFailure("Failed to retrieve username: ${error.message}")
        }
    })
}


