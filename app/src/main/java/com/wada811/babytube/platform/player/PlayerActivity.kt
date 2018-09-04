package com.wada811.babytube.platform.player

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayer.ErrorReason
import com.google.android.youtube.player.YouTubePlayer.PlayerStateChangeListener
import com.google.android.youtube.player.YouTubePlayer.Provider
import com.google.android.youtube.player.YouTubePlayerSupportFragment
import com.wada811.babytube.R
import com.wada811.babytube.platform.BabyTubeApplication


class PlayerActivity : AppCompatActivity(),
        PlayerOwner,
        PlayerStateChangeListener {

    companion object {
        fun createIntent(context: Context) = Intent(context, PlayerActivity::class.java)
        const val TestVideoId = "-mqW0ZRGl3g"
    }

    private val initializer: PlayerInitializer = PlayerInitializer(BabyTubeApplication.YouTubeApiKey, this)
    override val activity: Activity
        get() = this
    override val provider: Provider
        get() = playerFragment

    private val playerFragment: YouTubePlayerSupportFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.player_fragment) as YouTubePlayerSupportFragment
    }
    private lateinit var player: YouTubePlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.player_activity)
        initializer.initialize()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        initializer.onActivityResult(requestCode, resultCode, data)
    }

    override fun onInitialized(provider: Provider, player: YouTubePlayer, wasRestored: Boolean) {
        startLockTask()
        this.player = player
        if (!wasRestored) {
            player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE)
            player.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS)
            player.setPlayerStateChangeListener(this)
            player.cueVideo(TestVideoId)
        }
    }

    override fun onAdStarted() {
        Log.d("wada811", "onAdStarted()")
    }

    override fun onLoading() {
        Log.d("wada811", "onLoading()")
    }

    override fun onVideoStarted() {
        Log.d("wada811", "onVideoStarted()")
    }

    override fun onLoaded(videoId: String) {
        Log.d("wada811", "onLoaded(videoId: $videoId)")
        player.play()
    }

    override fun onVideoEnded() {
        Log.d("wada811", "onVideoEnded()")
        player.cueVideo(TestVideoId)
    }

    override fun onError(errorReason: ErrorReason) {
        val reason = when (errorReason) {
            YouTubePlayer.ErrorReason.AUTOPLAY_DISABLED -> "autoplay の制約のため、プレーヤーは再生リストにある次の動画に自動的に進むことができませんでした。"
            YouTubePlayer.ErrorReason.BLOCKED_FOR_APP -> "現在の動画を読み込むことができませんでした。コンテンツ所有者は動画を埋め込むことができないようにこのアプリをブロックしています。"
            YouTubePlayer.ErrorReason.EMBEDDING_DISABLED -> "現在の動画を読み込むことができませんでした。コンテンツ所有者はこの動画の埋め込みを無効にしています。"
            YouTubePlayer.ErrorReason.EMPTY_PLAYLIST -> "再生を開始できませんでした。読み込まれた再生リストには動画が含まれていません。"
            YouTubePlayer.ErrorReason.INTERNAL_ERROR -> "内部エラーのため、現在の動画を読み込むことができませんでした。"
            YouTubePlayer.ErrorReason.NETWORK_ERROR -> "ネットワーク リクエストに失敗したため、エラーが発生しました。"
            YouTubePlayer.ErrorReason.NOT_PLAYABLE -> "現在の動画は再生可能な状態ではないため、読み込むことができませんでした。"
            YouTubePlayer.ErrorReason.PLAYER_VIEW_TOO_SMALL -> "再生が停止されました。プレーヤーのビューが小さすぎます。"
            YouTubePlayer.ErrorReason.UNAUTHORIZED_OVERLAY -> "再生が停止されました。プレーヤーとビューが重なっています。"
            YouTubePlayer.ErrorReason.UNEXPECTED_SERVICE_DISCONNECTION -> "YouTube API サービスから突然切断されたため、再生がキャンセルされ、プレーヤーは解放されました。"
            YouTubePlayer.ErrorReason.UNKNOWN -> "このエラーの原因はわかりません。"
            YouTubePlayer.ErrorReason.USER_DECLINED_HIGH_BANDWIDTH -> "再生はキャンセルされました。ユーザーは、高帯域幅ストリームでの再生を許可しませんでした。"
            YouTubePlayer.ErrorReason.USER_DECLINED_RESTRICTED_CONTENT -> "再生はキャンセルされました。制限のあるコンテンツの再生は却下されました。"
            YouTubePlayer.ErrorReason.PLAYER_VIEW_NOT_VISIBLE -> "再生が停止されました。プレーヤーのビューが表示されていません。"
        }
        Log.d("wada811", "onError(reason: $errorReason: $reason)")
        Toast.makeText(this, reason, Toast.LENGTH_LONG).show()
    }

}