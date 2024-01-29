package com.krishnaZyala.faceRecognition.app

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.krishnaZyala.faceRecognition.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import com.krishnaZyala.faceRecognition.lib.LOG
import com.krishnaZyala.faceRecognition.ui.screen.home.HomeScreen

object MAIN {
    @HiltAndroidApp
    class HiltApp : Application()

    @AndroidEntryPoint
    class AppActivity : ComponentActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            setContent { AppContent(this) }
        }
    }
    @Composable
    fun AppContent(activity: Activity) = AppTheme(dynamicColors = true, statusBar = true) {
        runCatching {
            Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
               HomeScreen(activity)
            }
        }
            .onFailure { LOG.e(it, it.message) }.exceptionOrNull()?.printStackTrace()
    }
}



