package com.example.re_watch.components

import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.example.re_watch.data.rememberManagedPlayer


@OptIn(UnstableApi::class)
@Composable
fun rememberManagedExoPlayer(): State<Player?> = rememberManagedPlayer { context ->
    val builder = ExoPlayer.Builder(context)
    builder.setMediaSourceFactory(ProgressiveMediaSource.Factory(DefaultDataSource.Factory(context)))
    builder.build().apply {
        playWhenReady = true
    }
}