package com.wada811.babytube.platform

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.view.View
import android.view.WindowManager


class FullscreenManager(private val application: Application) {
    fun start() {
        application.registerActivityLifecycleCallbacks(activityLifecycleCallbacks)
    }

    private fun stop() {
        application.unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks)
    }

    @Suppress("NAME_SHADOWING")
    private val activityLifecycleCallbacks = object : Application.ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
            activity?.makeFullscreen()
            activity?.let { activity ->
                activity.window.decorView.setOnSystemUiVisibilityChangeListener {
                    activity.makeFullscreen()
                }
            }
        }

        override fun onActivityStarted(activity: Activity?) {
        }

        override fun onActivityResumed(activity: Activity?) {
            activity?.makeFullscreen()
        }

        override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
        }

        override fun onActivityPaused(activity: Activity?) {
        }

        override fun onActivityStopped(activity: Activity?) {
        }

        override fun onActivityDestroyed(activity: Activity?) {
        }
    }

    companion object {
        fun Activity.makeFullscreen() {
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or // hide nav bar
                    View.SYSTEM_UI_FLAG_FULLSCREEN or // hide status bar
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }
    }
}