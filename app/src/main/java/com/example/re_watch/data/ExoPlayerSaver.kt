package com.example.re_watch.data

import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import com.google.android.exoplayer2.ExoPlayer

data class PlayerState(val position: Int, val isPlaying: Boolean)

class ExoPlayerSaver(private val exoPlayer: ExoPlayer) : Saver<ExoPlayer, PlayerState>{
    override fun restore(value: PlayerState): ExoPlayer? {
        TODO("Not yet implemented")
    }

    override fun SaverScope.save(value: ExoPlayer): PlayerState? {
        TODO("Not yet implemented")
    }


}
