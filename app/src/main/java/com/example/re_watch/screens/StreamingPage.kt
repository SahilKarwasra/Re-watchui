package com.example.re_watch.screens

import VideoData
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_USER
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT
import android.content.res.Configuration
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.re_watch.CommentViewModel
import com.example.re_watch.FirestoreViewModel
import com.example.re_watch.LikeDislikeViewModel
import com.example.re_watch.Media
import com.example.re_watch.MediaState
import com.example.re_watch.R
import com.example.re_watch.ShowBuffering
import com.example.re_watch.Video
import com.example.re_watch.components.CommentSection
import com.example.re_watch.components.DescriptionBox
import com.example.re_watch.components.PlayerControlViewController
import com.example.re_watch.components.SimpleController
import com.example.re_watch.components.rememberManagedExoPlayer
import com.example.re_watch.navigation.AppScreens
import com.example.re_watch.rememberMediaState
import com.example.re_watch.utils.findActivity
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.auth.FirebaseAuth

private enum class ControllerType {
    None, Simple, PlayerControlView
}
@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun StreamingPage(navController: NavHostController, videoIdByDeepLink: String, param: VideoData?, ByLink :Boolean) {
    val commentViewModel: CommentViewModel = viewModel()
    val firestoreViewModel: FirestoreViewModel = viewModel()

    var videoId: String = ""
    var userId: String = ""
    var title: String = ""
    var description: String = ""
    var displayName: String = ""
    var userProfilePhoto: String = ""
    var userProfileUrl: String = ""
    var uri  = remember {
        mutableStateOf("")
    }
    var likes: String = ""
    var dislike: String = ""


    val videoItem by firestoreViewModel.video_data_By_Id.observeAsState(initial =
    Video("","","","",
        "","","",
        "", emptyList(),"",""))


    LaunchedEffect(Unit) {
//        Log.d("first", "comment get ")
        Log.d("deepLink", "without deeplink get :  ${param?.videoId} ::: using deeplink ${videoIdByDeepLink} ")
        firestoreViewModel.fetchVideoById(videoIdByDeepLink)
        commentViewModel.fetchComments()
    }


    if(ByLink){
        videoId = videoItem.videoId
        userId = videoItem.userId
        title = videoItem.videoTitle
        description = videoItem.videoDescription
        displayName = videoItem.userDisplayName
        userProfilePhoto = videoItem.userProfileImage
        userProfileUrl = videoItem.userProfileUrl
        uri.value = videoItem.videoUrl
        likes = videoItem.likes
        dislike = videoItem.dislikes
    }else{
        videoId = param?.videoId!!
        userId = param.userID
        title = param.videoTitle
        description = param.videoDescription
        displayName = param.userDisplayName
        userProfilePhoto = param.userProfileImage
        userProfileUrl = param.userProfileUrl
        uri.value = param.videoUrl
        likes = param.like
        dislike = param.dislike
    }
    var viewModel: FirestoreViewModel = viewModel()
    val likeDislikeViewModel: LikeDislikeViewModel = viewModel()

    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
    val context = (LocalContext.current as? Activity) ?: return
    currentUserId?.let { videoId.let { it1 -> commentViewModel.getVideoAndUserId(it1, it) } }
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    if(uri.value.isNotEmpty()){
        var setPlayer by rememberSaveable { mutableStateOf(true) }
        var playWhenReady by rememberSaveable { mutableStateOf(true) }
        var url by rememberSaveable { mutableStateOf(uri.value) }

        val systemUiController = rememberSystemUiController()
        SideEffect {
            systemUiController.isStatusBarVisible = !isLandscape
            systemUiController.isNavigationBarVisible = !isLandscape
        }

        var rememberedMediaItemIdAndPosition: Pair<String, Long>? by remember { mutableStateOf(null) }
        val player by rememberManagedExoPlayer()
        DisposableEffect(player, playWhenReady) {
            player?.playWhenReady = playWhenReady
            onDispose {}
        }


        val mediaItem = remember(url) { MediaItem.Builder().setMediaId(url).setUri(url).build() }
        DisposableEffect(mediaItem, player) {
            player?.run {
                setMediaItem(mediaItem)
                rememberedMediaItemIdAndPosition?.let { (id, position) ->
                    if (id == mediaItem.mediaId) seekTo(position)
                }?.also { rememberedMediaItemIdAndPosition = null }
                prepare()
            }
            onDispose {}
        }

        val mediaState = rememberMediaState(player = player.takeIf { setPlayer })
        val mediaContent = remember {
            movableContentOf { isLandscape: Boolean, modifier: Modifier ->
                MediaContent(mediaState, isLandscape, modifier)
            }
        }
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding(),
        ) { padding ->
            if (!isLandscape) {
                Column {
                    if(uri.value.isNotEmpty()){
                        Log.d("uri","${uri.value}")
                        mediaContent(
                            false,
                            Modifier
                                .padding(padding)
                                .fillMaxWidth()
                                .aspectRatio(16f / 9f)
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
                                    .clip(CircleShape)
                                    .clickable { navController.navigate(route = "${AppScreens.ChannelScreen.route}/${userId}") },
                            )
                            Column(
                                modifier = Modifier.padding(start = 7.dp, top = 10.dp),
                            ) {
                                Text(
                                    text = title,
                                    modifier = Modifier.padding(bottom = 5.dp),
                                    maxLines = 2,
                                    fontWeight = FontWeight(400),
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = displayName,
                                    maxLines = 1,
                                    fontWeight = FontWeight(300),
                                    fontSize = 16.sp
                                )
                            }
                            Share(text = "https://rewatch.online/video/${videoId}", context = context, modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.CenterVertically))

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
                                        currentUserId?.let { it1 ->
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
                                        (if (likeDislikeViewModel.likes.value.isNullOrEmpty()) {
                                            likes
                                        } else {
                                            likeDislikeViewModel.likes.value
                                        })!!,
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
                                    currentUserId?.let {
                                        videoId.let { it1 ->
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
                                        (if (likeDislikeViewModel.dislikes.value.isNullOrEmpty()) {
                                            dislike
                                        } else {
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
            }
        }
        if (isLandscape) {
            if(uri.value.isNotEmpty()){
                mediaContent(
                    true,
                    Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                )
            }
        }

    } else {
        Box(Modifier.fillMaxSize(), Alignment.Center) {
            CircularProgressIndicator()
        }
    }

}

@Composable
fun Share(text: String, context: Context , modifier: Modifier) {
    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    Box(modifier = modifier){
        Icon(imageVector = Icons.Outlined.Share, contentDescription = null,
            Modifier
                .clickable { startActivity(context, shareIntent, null) }
                .align(Alignment.CenterEnd)
                .padding(end = 10.dp)
                .size(30.dp))
    }
}
@Composable
private fun MediaContent(
    mediaState: MediaState,
    isLandscape: Boolean,
    modifier: Modifier = Modifier
) {
    val activity = LocalContext.current.findActivity()!!

    val enterFullscreen = {
        activity.requestedOrientation = SCREEN_ORIENTATION_USER_LANDSCAPE
        Log.d("MediaContent", "Entered fullscreen mode")
    }

    val exitFullscreen = {
        activity.requestedOrientation = SCREEN_ORIENTATION_USER_PORTRAIT
        Log.d("MediaContent", "Exited fullscreen mode")
    }
    var controllerHideOnTouch by rememberSaveable { mutableStateOf(true) }
    var controllerAutoShow by rememberSaveable { mutableStateOf(true) }
    var controllerType by rememberSaveable { mutableStateOf(ControllerType.Simple) }

    Box(modifier) {

        Media(
            mediaState,
            modifier = Modifier
                .fillMaxSize(),
            showBuffering = ShowBuffering.Always,
            buffering = {
                Box(Modifier.fillMaxSize(), Alignment.Center) {
                    CircularProgressIndicator()
                }
            },
            errorMessage = { error ->
                Box(Modifier.fillMaxSize(), Alignment.Center) {
                    Text(
                        error.message ?: "",
                        modifier = Modifier
                            .background(Color(0x80808080), RoundedCornerShape(16.dp))
                            .padding(horizontal = 12.dp, vertical = 4.dp),
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
            },
            controllerHideOnTouch = controllerHideOnTouch,
            controllerAutoShow = controllerAutoShow,
            controller = when (controllerType) {
                ControllerType.None -> null
                ControllerType.Simple -> @Composable { state ->
                    SimpleController(state, Modifier.fillMaxSize())
                }

                ControllerType.PlayerControlView -> @Composable { state ->
                    PlayerControlViewController(state, Modifier.fillMaxSize())
                }
            }
        )
        Box(modifier = Modifier.fillMaxWidth()){
            IconButton(
                modifier = Modifier
                    .padding(end = 5.dp)
                    .align(Alignment.TopEnd),
                onClick = { if (isLandscape) exitFullscreen() else enterFullscreen() }
            ) {
                Image(
                    contentScale = ContentScale.Crop,
                    painter = painterResource(id = if (isLandscape) R.drawable.ic_exit_full_screen else R.drawable.ic_fullscreen)  ,
                    contentDescription = "Enter/Exit fullscreen"
                )
            }
        }
    }
    val onBackPressedCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(isLandscape){
                    exitFullscreen()
                }
            }
        }
    }


    val onBackPressedDispatcher = checkNotNull(LocalOnBackPressedDispatcherOwner.current){
        "no owner dispatcher provided"
    }.onBackPressedDispatcher

    val lifecycleOwner = LocalLifecycleOwner.current


    Log.d("MediaContent", "onBackPressedDispatcher: $onBackPressedDispatcher")
    DisposableEffect(lifecycleOwner,onBackPressedDispatcher) {
        onBackPressedDispatcher.addCallback(lifecycleOwner,onBackPressedCallback)
        onDispose { onBackPressedCallback.remove() }
    }

    SideEffect {
        onBackPressedCallback.isEnabled = isLandscape
        if (isLandscape) {
            if (activity.requestedOrientation == SCREEN_ORIENTATION_USER) {
                activity.requestedOrientation = SCREEN_ORIENTATION_USER_LANDSCAPE
            }
        } else {
            activity.requestedOrientation = SCREEN_ORIENTATION_USER
        }
    }
}










