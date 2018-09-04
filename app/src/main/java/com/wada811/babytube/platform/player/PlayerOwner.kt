package com.wada811.babytube.platform.player

import android.app.Activity
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayer.Provider

interface PlayerOwner {
    val activity: Activity
    val provider: Provider
    fun onInitialized(provider: Provider, player: YouTubePlayer, wasRestored: Boolean)

}