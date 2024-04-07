package com.example.re_watch.screens

import VideoData
import android.app.Activity
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.re_watch.CommentViewModel
import com.example.re_watch.FirestoreViewModel
import com.example.re_watch.LikeDislikeViewModel
import com.example.re_watch.components.CommentSection
import com.example.re_watch.components.DescriptionBox
import com.example.re_watch.components.VideoPlayer
import com.example.re_watch.data.RememberWindowInfo
import com.example.re_watch.data.WindowInfo
import com.google.android.exoplayer2.ExoPlayer
import com.google.firebase.auth.FirebaseAuth


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun StreamingPage(navController: NavHostController, param: VideoData?) {
    val commentViewModel: CommentViewModel = viewModel()
    LaunchedEffect(Unit) {
        Log.d("first", "comment get ")

        commentViewModel.fetchComments()
    }
    val videoId = param?.videoId
    val title = param?.videoTitle
    val description = param?.videoDescription
    val displayName = param?.userDisplayName
    val userProfilePhoto = param?.userProfileImage
    val userProfileUrl = param?.userProfileUrl
    val uri = param?.videoUrl?.toUri()
    val likes = param?.like
    val dislike = param?.dislike


    var viewModel: FirestoreViewModel = viewModel()
    val likeDislikeViewModel: LikeDislikeViewModel = viewModel()

    val userId = FirebaseAuth.getInstance().currentUser?.uid
    val context = (LocalContext.current as? Activity) ?: return
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build()
    }
    userId?.let { videoId?.let { it1 -> commentViewModel.getVideoAndUserId(it1, it) } }
    val windowInfo = RememberWindowInfo()


    Log.d(
        "window",
        "height: ${windowInfo.screenHeight},,, width: ${windowInfo.screenWidth}  ,,, info width ${windowInfo.screenWidthInfo}"
    )
    if (windowInfo.screenWidthInfo is WindowInfo.WindowType.Compact) {
        Column {
            if (uri != null) {
                VideoPlayer(
                    uri = uri,
                    modifier = Modifier
                        .height(220.dp)
                        .fillMaxWidth(),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    exoPlayer = exoPlayer
                )
            }
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    GlideImage(
                        model = userProfilePhoto,
                        contentDescription = "ProfilePic",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(5.dp)
                            .size(70.dp)
                            .clip(CircleShape),
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
                                text = it,
                                maxLines = 1,
                                fontWeight = FontWeight(300),
                                fontSize = 16.sp
                            )
                        }
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    OutlinedButton(
                        modifier = Modifier
                            .padding(top = 7.dp)
                            .width(100.dp)
                            .height(45.dp)
                            .clip(RoundedCornerShape(25.dp)),
                        onClick = {
                            videoId?.let {
                                userId?.let { it1 ->
                                    likeDislikeViewModel.likeVideo(
                                        videoId = it,
                                        userId = it1
                                    )
                                }
                            }
                        }

                    ) {
                        Row {
                            Icon(
                                imageVector = Icons.Outlined.Favorite,
                                contentDescription = "Like icon",
                                modifier = Modifier
                                    .size(30.dp),
                                tint = Color.Red
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text =
                                (if(likeDislikeViewModel.likes.value.isNullOrEmpty()){
                                    likes
                                }else{
                                    likeDislikeViewModel.likes.value
                                })!!
                                ,
                                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(top = 5.dp),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                    OutlinedButton(
                        modifier = Modifier
                            .padding(top = 7.dp)
                            .width(100.dp)
                            .height(45.dp)
                            .clip(RoundedCornerShape(25.dp)),
                        onClick = {
                            userId?.let {
                                videoId?.let { it1 ->
                                    likeDislikeViewModel.dislikeVideo(
                                        userId = it,
                                        videoId = it1
                                    )
                                }
                            }
                        }
                    ) {
                        Row {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Dislike Icon",
                                modifier = Modifier
                                    .size(30.dp),
                                tint = Color.Gray
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text =
                                (if(likeDislikeViewModel.dislikes.value.isNullOrEmpty()){
                                    dislike
                                }else{
                                    likeDislikeViewModel.dislikes.value
                                })!!,
                                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(top = 5.dp),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }
                DescriptionBox(description = description.toString())
                CommentSection(commentViewModel = commentViewModel)
            }

        }
    } else {
        uri?.let {
            VideoPlayer(
                uri = it,
                modifier =
                Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background,
                exoPlayer = exoPlayer
            )
        }
    }
}













