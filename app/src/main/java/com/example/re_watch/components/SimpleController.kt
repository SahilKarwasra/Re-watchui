package com.example.re_watch.components


import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.systemGestureExclusion
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.re_watch.MediaState
import com.example.re_watch.R
import com.example.re_watch.TimeBar
import com.example.re_watch.rememberControllerState
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

@Composable
fun SimpleController(
    mediaState: MediaState,
    modifier: Modifier = Modifier,
) {
    Crossfade(targetState = mediaState.isControllerShowing, modifier, label = "") { isShowing ->
        if (isShowing) {
            val controllerState = rememberControllerState(mediaState)
            var scrubbing by remember { mutableStateOf(false) }
            val hideWhenTimeout = !mediaState.shouldShowControllerIndefinitely && !scrubbing
            var hideEffectReset by remember { mutableStateOf(0) }
            LaunchedEffect(hideWhenTimeout, hideEffectReset) {
                if (hideWhenTimeout) {
                    // hide after 3s
                    delay(3000)
                    mediaState.isControllerShowing = false
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x98000000))
            ) {
                Image(
                    painter = painterResource(
                        if (controllerState.showPause) R.drawable.ic_pause
                        else R.drawable.ic_play
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .size(52.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ) {
                            hideEffectReset++
                            controllerState.playOrPause()
                        }
                        .align(Alignment.Center),
                    colorFilter = ColorFilter.tint(Color.White)
                )

                val videoTime by rememberUpdatedState(newValue = controllerState.positionMs)
                Box(modifier = Modifier.align(Alignment.BottomEnd)) {
                    Text(
                        modifier = Modifier.padding(start = 14.dp).align(Alignment.CenterStart),
                        text = videoTime.formatMinSec(),
                        color = Color.White
                    )
                    TimeBar(
                        controllerState.durationMs,
                        controllerState.positionMs,
                        controllerState.bufferedPositionMs,
                        modifier = Modifier
                            .systemGestureExclusion()
                            .height(28.dp)
                            .padding(end = 40.dp, start = 40.dp)
                            .align(Alignment.BottomCenter),
                        contentPadding = PaddingValues(12.dp),
                        scrubberCenterAsAnchor = true,
                        onScrubStart = { scrubbing = true },
                        onScrubStop = { positionMs ->
                            scrubbing = false
                            controllerState.seekTo(positionMs)
                        }
                    )

                    Text(
                        modifier = Modifier
                            .padding(end = 14.dp)
                            .align(Alignment.CenterEnd),
                        text = controllerState.durationMs.formatMinSec(),
                        color = Color.White
                    )
                }


                LaunchedEffect(Unit) {
                    while (true) {
                        delay(200)
                        controllerState.triggerPositionUpdate()
                    }
                }

            }
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