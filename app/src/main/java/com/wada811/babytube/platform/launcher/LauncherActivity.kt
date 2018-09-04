package com.wada811.babytube.platform.launcher

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.wada811.babytube.platform.player.PlayerActivity

class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(PlayerActivity.createIntent(this))
        finish()
    }
}