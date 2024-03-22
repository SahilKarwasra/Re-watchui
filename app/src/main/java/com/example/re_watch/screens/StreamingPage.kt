package com.example.re_watch.screens

import VideoData
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.re_watch.FirestoreViewModel
import com.example.re_watch.LikeDislikeViewModel
import com.example.re_watch.R
import com.example.re_watch.components.VideoPlayer
import com.google.firebase.auth.FirebaseAuth


@Composable
fun StreamingPage(navController: NavHostController, param: VideoData?) {
    val videoId = param?.videoId
    val title = param?.videoTitle
    val description = param?.videoDescription
    val displayName = param?.userDisplayName
    val userPhoto = param?.userPhoto
    val userProfileUrl = param?.userProfileUrl
    val uploadTime = param?.uploadTime
    val uri = param?.videoUrl?.toUri()
    var viewModel: FirestoreViewModel = viewModel()
    var likeDislikeViewModel: LikeDislikeViewModel = viewModel()
    val userId = FirebaseAuth.getInstance().currentUser?.uid

    Column {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
            color = MaterialTheme.colorScheme.onPrimaryContainer
        ) {
            if (uri != null) {
                VideoPlayer(uri = uri)
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(id = R.drawable.profilepng),
                contentDescription = "Profile Pic",
                modifier = Modifier
                    .padding(5.dp)
                    .size(70.dp),
                tint = Color.Unspecified
            )
            Column(
                modifier = Modifier.padding(start = 7.dp, top = 10.dp),
            ) {
                title?.let {
                    Text(
                        text = it,
                        modifier = Modifier.padding(bottom = 5.dp),
                        maxLines = 2,
                        fontWeight = FontWeight(400),
                        fontSize = 16.sp
                    )
                }
                displayName?.let {
                    Text(
                        text = it, maxLines = 1, fontWeight = FontWeight(300), fontSize = 16.sp
                    )
                }
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Button(
                modifier = Modifier
                    .padding(top = 7.dp)
                    .width(100.dp)
                    .height(45.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .background(color = MaterialTheme.colorScheme.primary),
                onClick = {
                    videoId?.let { userId?.let { it1 -> likeDislikeViewModel.likeVideo(videoId = it, userId = it1) } }

                }

            ) {
                Row {
                    Icon(
                        imageVector = Icons.Outlined.Favorite,
                        contentDescription = "Like icon",
                        modifier = Modifier
                            .size(30.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = likeDislikeViewModel.likes.value,
                        style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(top = 5.dp),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
            Button(
                modifier = Modifier
                    .padding(top = 7.dp)
                    .width(100.dp)
                    .height(45.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .background(color = MaterialTheme.colorScheme.primary),
                onClick = {
                    userId?.let { videoId?.let { it1 -> likeDislikeViewModel.dislikeVideo(userId = it,videoId = it1) } }
                }
            ) {
                Row {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Dislike Icon",
                        modifier = Modifier
                            .size(30.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = likeDislikeViewModel.dislikes.value,
                        style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(top = 5.dp),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }

    }
}


@Preview(showSystemUi = true)
@Composable
fun StreamingPagePreview() {
//    StreamingPage(navController = rememberNavController())
}







