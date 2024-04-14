package com.example.re_watch.screens

import VideoData
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.re_watch.SearchViewModel
import com.example.re_watch.components.VideoCard
import com.example.re_watch.navigation.AppScreens
import com.google.gson.Gson

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavHostController) {
    var searchQuery by remember { mutableStateOf("") }
    val searchViewModel: SearchViewModel = viewModel()
    val videoList by searchViewModel.videoSearchList.observeAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .padding(10.dp)
                    .clip(RoundedCornerShape(CornerSize(20.dp))),
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Back Button",
                            modifier = Modifier
                                .size(30.dp)
                                .clickable {
                                    navController.popBackStack()
                                }
                        )
                        TextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color.Transparent,
                                focusedTextColor = Color(0xFFFFFFFF),
                                unfocusedTextColor = Color(0xFFFFFFFF),
                                focusedIndicatorColor = Color(0xFFBEC2C2),
                                unfocusedIndicatorColor = Color(0xFFBEC2C2)
                            ),
                            maxLines = 1,
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Search // Set the IME action to Search
                            ),
                            keyboardActions = KeyboardActions {
                                searchViewModel.searchVideos(searchQuery)
                            }
                        )
                    }
                },
                actions = {

                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = "Search",
                        Modifier
                            .padding(end = 20.dp, start = 20.dp)
                            .size(30.dp)
                            .clickable {
                                Log.d("check", "clicked")
                                searchViewModel.searchVideos(searchQuery)
                            }
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
        }) {
        Surface(
            modifier = Modifier
                .padding(it)
                .fillMaxHeight()
                .fillMaxWidth()
        ) {

            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                items(videoList) { video ->
                    VideoCard(video.userDisplayName, video.videoTitle, video.videoUrl, onClick = {
                        Log.d("videoList", "video card item clicked${video.userDisplayName}")
                        val videodata =
                            VideoData(
                                userDisplayName = video.userDisplayName,
                                userID = video.userId,
                                videoUrl = Uri.encode(video.videoUrl),
                                userProfileImage = Uri.encode(video.userProfileImage),
                                userProfileUrl = Uri.encode(video.userProfileUrl),
                                videoTitle = video.videoTitle,
                                videoDescription = video.videoDescription,
                                videoId = video.videoId,
                                like = video.likes,
                                dislike = video.dislikes
                            )

                        val videoDataJson = Gson().toJson(videodata)
                        navController.navigate(route = "${AppScreens.StreamingPage.route}/$videoDataJson")
                    })
                }
            }
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun VideoCardPrev() {
    SearchScreen(navController = rememberNavController())
}
