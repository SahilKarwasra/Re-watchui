package com.example.re_watch.screens

import VideoData
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.re_watch.FirestoreViewModel
import com.example.re_watch.R
import com.example.re_watch.components.VideoCard
import com.example.re_watch.navigation.AppScreens
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import com.google.gson.Gson

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {

    val viewModel: FirestoreViewModel = viewModel()


    val videoList by viewModel.videoList.observeAsState(initial = emptyList())

    LaunchedEffect(Unit) {
        viewModel.fetchAllVideosFromFirestore()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .padding(10.dp)
                    .clip(RoundedCornerShape(CornerSize(20.dp))),
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 5.dp)
                    ) {
                        Text(
                            stringResource(id = R.string.app_name),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                },
                actions = {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = "Search",
                        Modifier.size(30.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF20A6E2),
                    titleContentColor = Color(0xFFFFFFFF),
                    actionIconContentColor = Color(0xFFFCFCFC)
                ),

            )
        },
        floatingActionButton = {
            FloatingProfileButton {
                navController.navigate(route = AppScreens.ProfileScreen.route)
            }
        }



        ) {
        Surface(modifier = Modifier
            .padding(it)
            .fillMaxHeight()
            .fillMaxWidth()) {

            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {


                items(videoList) { video ->
                    VideoCard(video.userDisplayName, video.uploadTime, video.videoUrl, onClick = {
                        Log.d("videoList", "video card item clicked${video.videoUrl}")
                        val videodata = VideoData(video.userDisplayName, video.uploadTime, Uri.encode(video.videoUrl))

                        val videoDataJson = Gson().toJson(videodata)
                        navController.navigate(route = "${AppScreens.StreamingPage.route}/$videoDataJson")


                    })

                }
            }
        }


    }
}



@Composable
fun FloatingProfileButton(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = { onClick() },
        modifier = Modifier.padding(10.dp)
    ) {
        Icon(Icons.Filled.Person, "Floating Profile button.")
    }
}

@Preview(showSystemUi = true)
@Composable
fun VideoCardPreview() {
//    HomeScreen(navController = NavHostController(LocalContext.current),)
}
