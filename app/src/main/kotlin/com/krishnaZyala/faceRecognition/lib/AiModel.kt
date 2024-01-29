package com.krishnaZyala.faceRecognition.lib

import android.content.Context
import android.graphics.Bitmap
import com.krishnaZyala.faceRecognition.data.model.ProcessedImage
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import kotlin.math.sqrt

object AiModel {
    private const val FACE_NET_MODEL_PATH = "face_net_512.tflite"
    private const val ANTI_SPOOF_MODEL_PATH = "anti_spoof_model.tflite"
    private const val MOBILE_NET_MODEL_PATH = "mobile_net.tflite"

    private const val FACE_NET_IMAGE_SIZE = 160
    private const val FACE_NET_EMBEDDING_SIZE = 512
    private const val MOBILE_NET_IMAGE_SIZE = 224

    private const val IMAGE_MEAN = 128.0f
    private const val IMAGE_STD = 128.0f
    const val DEFAULT_SIMILARITY = 0.8f
    private var isRunning = false

    private val Context.faceNetInterpreter
        get(): Interpreter {
            val fileDescriptor = assets.openFd(FACE_NET_MODEL_PATH)
            val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
            val fileChannel = inputStream.channel
            val startOffset = fileDescriptor.startOffset
            val declaredLength = fileDescriptor.declaredLength
            val modelBuffer: MappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
            return Interpreter(modelBuffer)
        }

    private val Context.mobileNetInterpreter
        get(): Interpreter {
            val fileDescriptor = assets.openFd(MOBILE_NET_MODEL_PATH)
            val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
            val fileChannel = inputStream.channel
            val startOffset = fileDescriptor.startOffset
            val declaredLength = fileDescriptor.declaredLength
            val modelBuffer: MappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
            return Interpreter(modelBuffer)
        }


    fun Context.mobileNet(face: ProcessedImage, interpreter: Interpreter = mobileNetInterpreter): Result<Float> = runCatching {
        // Preprocess the reference bitmap
        val referenceInput = face.faceBitmap?.let { bitmap -> preprocessBitmap(bitmap, MOBILE_NET_IMAGE_SIZE).getOrNull()?.let { arrayOf(it) } }
            ?: throw Throwable("Unable to preprocess Bitmap")
        // Allocate output buffer for the reference embedding
        val referenceOutputBuffer = ByteBuffer.allocateDirect(4).apply { order(ByteOrder.nativeOrder()) }
        val referenceOutputs: MutableMap<Int, Any> = mutableMapOf(0 to referenceOutputBuffer)
        interpreter.runForMultipleInputsOutputs(referenceInput, referenceOutputs)
        referenceOutputBuffer.rewind()
        val data = referenceOutputBuffer.float
        data
    }.onFailure { LOG.e(it, it.message) }

    private fun preprocessBitmap(bitmap: Bitmap, size: Int, isModelQuantized: Boolean = false): Result<ByteBuffer> = runCatching {
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, size, size, true)
        val inputBuffer = ByteBuffer.allocateDirect(size * size * 3 * 4).apply { order(ByteOrder.nativeOrder()) }
        for (y in 0 until size) {
            for (x in 0 until size) {
                val pixelValue = resizedBitmap.getPixel(x, y)
                if (isModelQuantized) {
                    // Quantized model
                    inputBuffer.put((pixelValue shr 16 and 0xFF).toByte())
                    inputBuffer.put((pixelValue shr 8 and 0xFF).toByte())
                    inputBuffer.put((pixelValue and 0xFF).toByte())
                } else {
                    // Float model
                    inputBuffer.putFloat(((pixelValue shr 16 and 0xFF) - IMAGE_MEAN) / IMAGE_STD)
                    inputBuffer.putFloat(((pixelValue shr 8 and 0xFF) - IMAGE_MEAN) / IMAGE_STD)
                    inputBuffer.putFloat(((pixelValue and 0xFF) - IMAGE_MEAN) / IMAGE_STD)
                }
            }
        }
        inputBuffer
    }.onFailure { LOG.e(it, it.message) }

}