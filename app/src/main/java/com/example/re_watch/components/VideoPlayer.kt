package com.example.re_watch.components

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.re_watch.R
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerView
import java.util.concurrent.TimeUnit


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun VideoPlayer(modifier: Modifier = Modifier, uri: Uri) {
    var isPlaying by remember { mutableStateOf(true) }
    val context = LocalContext.current
    val playerViewRef = remember { mutableStateOf(PlayerView(context)) }

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(uri))
            prepare()
            playWhenReady = true
        }
    }
    var totalDuration by remember { mutableLongStateOf(0L) }

    var currentTime by remember { mutableLongStateOf(0L) }

    var bufferedPercentage by remember { mutableIntStateOf(0) }

    Box(modifier = Modifier) {
        AndroidView(
            factory = { context ->
                PlayerView(context).also{view->
                    playerViewRef.value = view
                    view.useController = false
                }.apply {
                    player = exoPlayer

                }
            },
            modifier = Modifier.fillMaxSize()
        )


        DisposableEffect(key1 = Unit) {
            val listener =
                object : Player.Listener {
                    override fun onEvents(player: Player, events: Player.Events) {
                        super.onEvents(player, events)
                        totalDuration = player.duration.coerceAtLeast(0L)
                        currentTime = player.currentPosition.coerceAtLeast(0L)
                        bufferedPercentage = player.bufferedPercentage
                    }
                }

            exoPlayer.addListener(listener)

            onDispose {
                exoPlayer.removeListener(listener)
                exoPlayer.release()
            }
        }
        AnimatedVisibility(
            modifier = modifier,
            visible = true,
            enter = fadeIn(),
            exit = fadeOut()
        ){
            Box (Modifier.fillMaxSize()){
                TopControl(Modifier.fillMaxWidth().align(Alignment.TopCenter))
                CenterControls(
                    isPlaying = { isPlaying },
                    onReplayClick = { exoPlayer.seekBack() },
                    onPauseToggle = {
                        if (exoPlayer.isPlaying) {
                            exoPlayer.pause()
                        } else {
                            exoPlayer.play()
                        }
                        isPlaying = isPlaying.not()
                    },
                    onForwardClick = { exoPlayer.seekForward() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                )
                BottomControls(
                    totalDuration = { totalDuration },
                    currentTime = { currentTime },
                    bufferPercentage = { bufferedPercentage },
                    onSeekChanged = { timeMs: Float -> exoPlayer.seekTo(timeMs.toLong()) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .animateEnterExit(
                            enter =
                            slideInVertically(
                                initialOffsetY = { fullHeight: Int ->
                                    fullHeight
                                }
                            ),
                            exit =
                            slideOutVertically(
                                targetOffsetY = { fullHeight: Int ->
                                    fullHeight
                                }
                            )
                        ),
                )
            }
        }

    }
}





@Composable
fun TopControl(modifier: Modifier) {
    Box(modifier = Modifier.fillMaxWidth()){
        IconButton(
            modifier = Modifier
                .padding(end = 5.dp)
                .align(Alignment.TopEnd),
            onClick = {}
        ) {
            Image(
                contentScale = ContentScale.Crop,
                painter = painterResource(id = R.drawable.ic_fullscreen),
                contentDescription = "Enter/Exit fullscreen"
            )
        }
    }

}


@Composable
fun CenterControls(
    modifier: Modifier = Modifier,
    isPlaying: () -> Boolean,
    onReplayClick: () -> Unit,
    onPauseToggle: () -> Unit,
    onForwardClick: () -> Unit
) {
    val isVideoPlaying = remember(isPlaying()) { isPlaying() }

    Row(modifier = modifier, horizontalArrangement = Arrangement.SpaceEvenly) {
        IconButton(modifier = Modifier.size(40.dp), onClick = onReplayClick) {
            Image(
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                painter = painterResource(id = R.drawable.ic_replay_10),
                contentDescription = "Replay 5 seconds"
            )
        }

        IconButton(modifier = Modifier.size(40.dp), onClick = onPauseToggle) {
            Image(
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                painter =
                if (isVideoPlaying) {
                    painterResource(id = R.drawable.ic_pause)
                } else {
                    painterResource(id = R.drawable.ic_play)
                },
                contentDescription = "Play/Pause"
            )
        }

        IconButton(modifier = Modifier.size(40.dp), onClick = onForwardClick) {
            Image(
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                painter = painterResource(id = R.drawable.ic_forward_10),
                contentDescription = "Forward 10 seconds"
            )
        }
    }
}


@Composable
fun BottomControls(
    modifier: Modifier = Modifier,
    totalDuration: () -> Long,
    currentTime: () -> Long,
    bufferPercentage: () -> Int,
    onSeekChanged: (timeMs: Float) -> Unit
) {

    val duration = remember(totalDuration()) { totalDuration() }

    val videoTime = remember(currentTime()) { currentTime() }

    val buffer = remember(bufferPercentage()) { bufferPercentage() }

    Row(modifier = modifier.padding(bottom = 5.dp)) {
        Text(
            modifier = Modifier.padding(start = 5.dp, top = 16.dp),
            text = videoTime.formatMinSec(),
            color = Color.LightGray
        )
        Box(modifier = Modifier) {
            // buffer bar
            Slider(
                modifier= Modifier
                    .align(Alignment.Center)
                    .padding(start = 10.dp, end = 50.dp),
                value = buffer.toFloat(),
                enabled = false,
                onValueChange = { /*do nothing*/},
                valueRange = 0f..100f,
                colors =
                SliderDefaults.colors(
                    disabledThumbColor = Color.Transparent,
                    disabledActiveTrackColor = Color.Gray
                )
            )

            // seek bar
            Slider(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(start = 10.dp, end = 50.dp),
                value = videoTime.toFloat(),
                onValueChange = onSeekChanged,
                valueRange = 0f..duration.toFloat(),
                colors =
                SliderDefaults.colors(
                    thumbColor = Color.White,
                    activeTickColor = Color.LightGray
                )
            )

            Text(
                modifier = Modifier
                    .padding(end = 5.dp)
                    .align(Alignment.BottomEnd),
                text = duration.formatMinSec(),
                color = Color.LightGray
            )
        }


    }
}

fun Long.formatMinSec(): String {
    return if (this == 0L) {
        "..."
    } else {
        String.format(
            "%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(this),
            TimeUnit.MILLISECONDS.toSeconds(this) -
                    TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(this)
                    )
        )
    }
}



@Preview(showSystemUi = true)
@Composable
fun VideoPlayerview() {
//
//    PlayerControls(
//        modifier = Modifier.fillMaxSize(),
//        isPlaying = { true },
//        onReplayClick = {  },
//        onForwardClick = { },
//        onPauseToggle = {
//        },
//        totalDuration = { 1010 },
//        currentTime = { 110 },
//        bufferPercentage = { 99 },
//        onSeekChanged = {  },
//        isVisible = {true}
//    )
    BottomControls(
        totalDuration = { 100 },
        currentTime = { 1110 },
        bufferPercentage = { 11 },
        onSeekChanged = {}
    )
//    TopControl(Modifier)
}
