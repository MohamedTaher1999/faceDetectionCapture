package com.krishnaZyala.faceRecognition.data.model

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.mlkit.vision.face.FaceLandmark
import com.krishnaZyala.faceRecognition.app.Utils

@Keep
@Entity
data class FaceInfo(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String,
    val width: Int = 0,
    val height: Int = 0,
    val faceWidth: Int = 0,
    val faceHeight: Int = 0,
    val top: Int = 0,
    val left: Int = 0,
    val right: Int = 0,
    val bottom: Int = 0,
    val landmarks: List<FaceLandmark> = listOf(),
    val smilingProbability: Float = 0f,
    val leftEyeOpenProbability: Float = 0f,
    val rightEyeOpenProbability: Float = 0f,
    val timestamp: String = Utils.timestamp(),
    val time: Long = System.currentTimeMillis(),
)
