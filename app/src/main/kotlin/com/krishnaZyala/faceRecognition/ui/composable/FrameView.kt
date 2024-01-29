package com.krishnaZyala.faceRecognition.ui.composable

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
@Composable
fun FrameView(frame: Bitmap, modifier: Modifier = Modifier) = Box(
    contentAlignment = Alignment.BottomEnd,
    modifier = modifier
        .background(MaterialTheme.colorScheme.secondaryContainer)
        .fillMaxWidth(),
) {
    Image(frame.asImageBitmap(), "Frame Bitmap", Modifier.fillMaxSize())
}