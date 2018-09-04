package com.wada811.babytube.platform

import android.app.Application

class BabyTubeApplication : Application() {
    companion object {
        const val YouTubeApiKey = "AIzaSyDmoX6riKb0Lqd3LUzmOrbhOP-BecH2Gy8"
    }

    override fun onCreate() {
        super.onCreate()
        FullscreenManager(this).start()
    }

    override fun onTerminate() {
        super.onTerminate()

    }
}