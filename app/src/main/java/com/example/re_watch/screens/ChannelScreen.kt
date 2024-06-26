package com.example.re_watch.screens

import VideoData
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.re_watch.FirestoreViewModel
import com.example.re_watch.R
import com.example.re_watch.UserDetailViewModel
import com.example.re_watch.components.AdvancedVideoCard
import com.example.re_watch.navigation.AppScreens
import com.google.gson.Gson

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun ChannelScreen(navController: NavHostController, userId: String, userSlug: String) {
    var userId = remember {
        mutableStateOf(userId)
    }
    var userSlug = remember {
        mutableStateOf(userSlug)
    }
    val viewModel: UserDetailViewModel = viewModel()
    val userDetails by viewModel.userDetails.observeAsState(initial = null)
    val userName = userDetails?.userDisplayName
    val userProfileImage = userDetails?.userProfileImage
    val userProfileUrl = userDetails?.userProfileUrl
    val userIdBySlug = viewModel.userIdget.value
    val firestoreViewModel: FirestoreViewModel = viewModel()
    var shouldFetchVideos by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val videoList by firestoreViewModel.userVideoList.observeAsState(initial = emptyList())
    LaunchedEffect(Unit) {
        viewModel.fetchUserDetails(userId.value, userSlug.value)
        if(userId.value.isNotEmpty()){
            firestoreViewModel.fetchVideosByUserId(userId.value)
            shouldFetchVideos = true
            Log.d("userSlag","user id :  ${userId}")

        }
        else{
            userId.value = userIdBySlug
        }
    }
    LaunchedEffect(userIdBySlug.isNotEmpty()) {
        if (!shouldFetchVideos && userIdBySlug.isNotEmpty()) {
            firestoreViewModel.fetchVideosByUserId(userIdBySlug)
            shouldFetchVideos = true
        }
    }


    if(shouldFetchVideos){
        Column {
            TopAppBar(
                modifier = Modifier
                    .padding(10.dp)
                    .clip(RoundedCornerShape(CornerSize(20.dp))),
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 5.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            stringResource(id = R.string.app_name),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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

                )

            LazyColumn {
                item {
                    Row {
                        GlideImage(
                            model = userProfileImage,
                            contentDescription = "Profile Pic",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .padding(start = 20.dp, top = 30.dp)
                                .size(110.dp)
                                .clip(CircleShape)
                        )
                        Column {
                            userName?.let {
                                Text(
                                    text = it,
                                    fontSize = 28.sp,
                                    color = Color(0xFF999BAD),
                                    modifier = Modifier.padding(start = 20.dp, top = 40.dp)
                                )
                            }
                            Text(
                                text = "${videoList.size} Videos",
                                modifier = Modifier.padding(start = 20.dp, top = 10.dp),
                                fontSize = 16.sp,
                                color = Color(0xFF87979E),
                            )
                        }
                        Share(text = "https://rewatch.online/user/${userProfileUrl}", context = context, modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterVertically))
                    }
                }

                item {
                    Text(
                        text = "Videos",
                        fontSize = 28.sp,
                        modifier = Modifier
                            .padding(start = 20.dp, top = 40.dp),
                        color = Color(0xFF6B7E8D),
                    )
                }

                items(videoList) { video ->
                    val videoData =
                        VideoData(
                            userDisplayName = video.userDisplayName,
                            userID = video.userId,
                            videoUrl =  Uri.encode(video.videoUrl),
                            userProfileImage = Uri.encode(video.userProfileImage),
                            userProfileUrl = Uri.encode(video.userProfileUrl),
                            videoTitle = video.videoTitle,
                            videoDescription = video.videoDescription,
                            videoId = video.videoId,
                            like = video.likes,
                            dislike = video.dislikes
                        )
                    AdvancedVideoCard(username = video.userProfileUrl, title = video.videoTitle, videoUrl = video.videoUrl, onDeleteClicked ={} , remove = false, onEditClicked = {}) {
                        Log.e("user", "${video.userProfileUrl}")
                        val videoDataJson = Gson().toJson(videoData)
                        navController.navigate(route = "${AppScreens.AinimationStream.route}/$videoDataJson")
                    }

                }
            }

        }
    }
    else{
        Box(Modifier.fillMaxSize(), Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun ChannelScreenPreview() {

}