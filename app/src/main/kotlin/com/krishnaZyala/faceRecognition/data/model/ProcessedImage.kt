package com.krishnaZyala.faceRecognition.data.model

import android.graphics.Bitmap
import androidx.annotation.Keep
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceLandmark
import java.nio.ByteBuffer

@Keep
data class ProcessedImage(
    var id: Int? = null,
    var name: String = "",
    val face: Face? = null,
    var spoof: Float? = null,
    val image: Bitmap? = null,
    var frame: Bitmap? = null,
    val distance: Float? = null,
    val trackingId: Int? = null,
    val similarity: Float? = null,
    val faceBitmap: Bitmap? = null,
    val faceSignature: ByteBuffer? = null,
    var landmarks: List<FaceLandmark> = listOf(),
    val time: Long = System.currentTimeMillis(),
) {

}
