package com.krishnaZyala.faceRecognition.ui.screen.selfie

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krishnaZyala.faceRecognition.data.Repository
import com.krishnaZyala.faceRecognition.data.model.ProcessedImage
import com.krishnaZyala.faceRecognition.lib.AiModel.mobileNet
import com.krishnaZyala.faceRecognition.lib.LOG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelfieViewModel @Inject constructor(private val repo: Repository) : ViewModel() {
    private val cameraProvider: ProcessCameraProvider by lazy { repo.cameraProviderFuture.get() }
    val showSaveDialog: MutableState<Boolean> = mutableStateOf(false)
    val image: MutableState<ProcessedImage> = mutableStateOf(ProcessedImage())
    val lensFacing: MutableState<Int> = mutableStateOf(CameraSelector.LENS_FACING_FRONT)
    private val cameraSelector get(): CameraSelector = repo.cameraSelector(lensFacing.value)
    private val paint = Paint().apply {
        strokeWidth = 3f
        color = Color.GREEN
    }
    private val Context.imageAnalysis
        get() = repo.imageAnalysis(lensFacing.value, paint) { result ->
            runCatching {
                val data = result.getOrNull() ?: return@runCatching
                var faceAccepted = false
                data.face?.let {
                    if (it.leftEyeOpenProbability != null) {
                        if (it.rightEyeOpenProbability != null) {
                            faceAccepted = ((-12.0 < it.headEulerAngleX && it.headEulerAngleX < 12.0)
                                    && (-12.0 < it.headEulerAngleY && it.headEulerAngleY < 12.0)
                                    && (-12.0 < it.headEulerAngleZ && it.headEulerAngleZ < 12.0) && it.leftEyeOpenProbability!! >= 0.8 && it.rightEyeOpenProbability!! >= 0.8)
                        }
                    }
                }
                if(faceAccepted){
                    stopCamera()
                   // cameraProvider.unbindAll()
                }
                data.landmarks = data.face?.allLandmarks ?: listOf()
                data.spoof = mobileNet(data).getOrNull()
                image.value = data
            }.onFailure { LOG.e(it, it.message) }
        }
    fun onCompose(context: Context, lifecycleOwner: LifecycleOwner) = viewModelScope.launch {
        runCatching {
            if (showSaveDialog.value) return@runCatching
            bindCamera(lifecycleOwner, context.imageAnalysis)
            delay(1000)
            bindCamera(lifecycleOwner, context.imageAnalysis)
            LOG.d("Add Face Screen Composed")
        }.onFailure { LOG.e(it, it.message) }
    }
    fun onDispose() = runCatching {
        cameraProvider.unbindAll()
        LOG.d("Add Face Screen Disposed")
    }.onFailure { LOG.e(it, it.message) }
    private fun bindCamera(lifecycleOwner: LifecycleOwner, imageAnalysis: ImageAnalysis) = runCatching {
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, imageAnalysis)
        LOG.d("Camera is bound to lifecycle.")
    }.onFailure { LOG.e(it, it.message) }
    private fun stopCamera() = runCatching {
        showSaveDialog.value = true
        cameraProvider.unbindAll()
    }.onFailure {
        LOG.e(it, it.message)
    }

}
