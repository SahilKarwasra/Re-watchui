package com.example.re_watch.components


import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerControlView
import com.example.re_watch.ControllerVisibility
import com.example.re_watch.MediaState


@OptIn(UnstableApi::class)
@Composable
fun PlayerControlViewController(
    mediaState: MediaState,
    modifier: Modifier = Modifier,
    showTimeoutMs: Int = PlayerControlView.DEFAULT_SHOW_TIMEOUT_MS
) {
    AndroidView(
        factory = { context ->
            PlayerControlView(context).apply {
                hideImmediately()
                addVisibilityListener {
                    mediaState.controllerVisibility = if (isVisible) {
                        if (isFullyVisible) ControllerVisibility.Visible
                        else ControllerVisibility.PartiallyVisible
                    } else ControllerVisibility.Invisible
                }
            }
        },
        modifier = modifier
    ) {
        it.player = mediaState.player
        it.showTimeoutMs =
            if (mediaState.shouldShowControllerIndefinitely) 0
            else showTimeoutMs

        if (mediaState.controllerVisibility == ControllerVisibility.Visible) {
            if (mediaState.controllerVisibility != ControllerVisibility.PartiallyVisible) {
                it.show()
            }
        } else if (mediaState.controllerVisibility == ControllerVisibility.Invisible) {
            if (it.isVisible) it.hide()
        }
    }
}
