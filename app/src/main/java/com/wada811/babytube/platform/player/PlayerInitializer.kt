package com.wada811.babytube.platform.player

import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener
import com.google.android.youtube.player.YouTubePlayer.Provider

class PlayerInitializer(private val apiKey: String, private val owner: PlayerOwner) : OnInitializedListener {

    companion object {
        const val REQUEST_RECOVERY = 100
    }

    fun initialize() {
        owner.provider.initialize(apiKey, this@PlayerInitializer)
    }

    override fun onInitializationFailure(provider: Provider, result: YouTubeInitializationResult) {
        Log.d("wada811", "onInitializationFailure(provider: $provider, result: $result)")
        val reason = when (result) {
            YouTubeInitializationResult.CLIENT_LIBRARY_UPDATE_REQUIRED -> "YouTube API サービスに接続するために使用されているクライアントライブラリのバージョンが古くなっています。"
            YouTubeInitializationResult.DEVELOPER_KEY_INVALID -> "初期化関数に提供されたデベロッパーキーが無効です。"
            YouTubeInitializationResult.ERROR_CONNECTING_TO_SERVICE -> "YouTube API サービスへの接続エラーが発生しました。"
            YouTubeInitializationResult.INTERNAL_ERROR -> "内部エラーが発生しました。"
            YouTubeInitializationResult.INVALID_APPLICATION_SIGNATURE -> "アプリケーションの APK の署名が正しくありません。"
            YouTubeInitializationResult.NETWORK_ERROR -> "ネットワークへの接続にエラーが発生し、YouTube Player API サービスを初期化できませんでした。"
            YouTubeInitializationResult.SERVICE_DISABLED -> "この端末で、インストールされている YouTube API サービスのバージョンが無効化されました。"
            YouTubeInitializationResult.SERVICE_INVALID -> "この端末にインストールされた YouTube API サービスのバージョンは無効です。"
            YouTubeInitializationResult.SERVICE_MISSING -> "この端末で、YouTube API サービスが見つかりません。"
            YouTubeInitializationResult.SERVICE_VERSION_UPDATE_REQUIRED -> "インストールされたバージョンの YouTube API サービスは古くなっています。"
            YouTubeInitializationResult.SUCCESS -> "初期化に成功しました。"
            YouTubeInitializationResult.UNKNOWN_ERROR -> "このエラーの原因はわかりません。クライアントライブラリが YouTube API サービスよりも古く、このサービスが返している YouTubeInitializationResult を知らない可能性があります。再試行すると、問題が解決される可能性があります。"
        }
        if (result.isUserRecoverableError) {
            result.getErrorDialog(owner.activity, REQUEST_RECOVERY).show()
        } else {
            Toast.makeText(owner.activity, reason, Toast.LENGTH_LONG).show()
            Log.d("wada811", "$result: $reason")
            owner.activity.finish()
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("wada811", "onActivityResult(requestCode: $requestCode, resultCode: $resultCode, data: $data)")
        if (requestCode == REQUEST_RECOVERY) {
            initialize()
        }
    }

    override fun onInitializationSuccess(provider: Provider, player: YouTubePlayer, wasRestored: Boolean) {
        Log.d("wada811", "onInitializationSuccess(owner: $provider, player: $player, wasRestored: $wasRestored)")
        owner.onInitialized(provider, player, wasRestored)
    }

}