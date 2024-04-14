package com.example.re_watch.screens

import VideoData
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.re_watch.FirestoreViewModel
import com.example.re_watch.R
import com.example.re_watch.components.AdvancedVideoCard
import com.example.re_watch.navigation.AppScreens
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemoveVideoScreen(navController: NavHostController) {

    val viewModel: FirestoreViewModel = viewModel()
    val currentUser = FirebaseAuth.getInstance().currentUser?.uid


    val videoList by viewModel.userVideoList.observeAsState(initial = emptyList())


    LaunchedEffect(Unit) {
        viewModel.fetchVideosByUserId(currentUser)
    }

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

                        }
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF20A6E2),
                titleContentColor = Color(0xFFFFFFFF),
                actionIconContentColor = Color(0xFFFCFCFC)
            ),
        )



        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(5.dp)
        ) {
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
                AdvancedVideoCard(username = video.userProfileUrl, title = video.videoTitle, videoUrl = video.videoUrl,videoData = videoData) {

                    val videoDataJson = Gson().toJson(videoData)
                    navController.navigate(route = "${AppScreens.StreamingPage.route}/$videoDataJson")
                }

            }
        }
    }
}



@Preview(showSystemUi = true)
@Composable
fun RemoveVideoScreenPreview() {
    RemoveVideoScreen(
        navController = NavHostController(LocalContext.current)
    )
}
