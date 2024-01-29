package com.krishnaZyala.faceRecognition.ui.screen.selfie

import android.content.Context
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import com.krishnaZyala.faceRecognition.data.model.ProcessedImage
import com.krishnaZyala.faceRecognition.ui.composable.FrameView

@Composable
fun AddFaceScreen(
    vm: SelfieViewModel = hiltViewModel()
) {
    val image: ProcessedImage by vm.image
    val lensFacing: Int by vm.lensFacing
    val showSaveDialog: Boolean by vm.showSaveDialog
    val context: Context = LocalContext.current
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(showSaveDialog, lensFacing) {
        vm.onCompose(context, lifecycleOwner)
        onDispose { vm.onDispose() }
    }
    val content: @Composable (PaddingValues) -> Unit = { padding ->
        image.frame?.let {
            FrameView(frame = it)

        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = contentColorFor(MaterialTheme.colorScheme.background),
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets,
        content = content
    )
}

