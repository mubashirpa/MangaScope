package com.evaluation.mangascope.core

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.SystemClock
import android.util.Log
import androidx.camera.core.ImageProxy
import androidx.core.graphics.createBitmap
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.framework.image.MPImage
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.core.Delegate
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.facedetector.FaceDetector
import com.google.mediapipe.tasks.vision.facedetector.FaceDetectorResult

class FaceDetectorHelper(
    val context: Context,
    var threshold: Float = THRESHOLD_DEFAULT,
    var delegate: Delegate = Delegate.CPU,
    var faceDetectorListener: DetectorListener? = null,
) {
    private var faceDetector: FaceDetector? = null

    init {
        initFaceDetector()
    }

    fun initFaceDetector() {
        try {
            val modelName = "blaze_face_short_range.tflite"

            val baseOptionsBuilder =
                BaseOptions
                    .builder()
                    .setModelAssetPath(modelName)
                    .setDelegate(delegate)
            val baseOptions = baseOptionsBuilder.build()

            val optionsBuilder =
                FaceDetector.FaceDetectorOptions
                    .builder()
                    .setBaseOptions(baseOptions)
                    .setMinDetectionConfidence(threshold)
                    .setResultListener(::returnLiveStreamResult)
                    .setErrorListener(::returnLiveStreamError)
                    .setRunningMode(RunningMode.LIVE_STREAM)
            val options = optionsBuilder.build()

            faceDetector = FaceDetector.createFromOptions(context, options)
        } catch (e: IllegalStateException) {
            faceDetectorListener?.onError("Face detector failed to initialize. See error logs for details")
            Log.e(TAG, "TFLite failed to load model with error: ${e.message}", e)
        } catch (e: RuntimeException) {
            faceDetectorListener?.onError("Face detector failed to initialize. See error logs for details")
            Log.e(TAG, "Face detector failed to load model with error: ${e.message}", e)
        } catch (e: Exception) {
            faceDetectorListener?.onError("Face detector failed to initialize. See error logs for details")
            Log.e(TAG, "Face detector failed to load model with error: ${e.message}", e)
        }
    }

    fun closeFaceDetector() {
        faceDetector?.close()
        faceDetector = null
    }

    fun isClosed() = faceDetector == null

    fun detectLivestreamFrame(imageProxy: ImageProxy) {
        val bitmapBuffer = createBitmap(imageProxy.width, imageProxy.height)
        imageProxy.use { bitmapBuffer.copyPixelsFromBuffer(imageProxy.planes.first().buffer) }
        imageProxy.close()

        val matrix =
            Matrix().apply {
                postRotate(imageProxy.imageInfo.rotationDegrees.toFloat())
                postScale(-1f, 1f, imageProxy.width.toFloat(), imageProxy.height.toFloat())
            }

        val rotatedBitmap =
            Bitmap.createBitmap(
                bitmapBuffer,
                0,
                0,
                bitmapBuffer.width,
                bitmapBuffer.height,
                matrix,
                true,
            )

        val mpIMage = BitmapImageBuilder(rotatedBitmap).build()
        val frameTime = SystemClock.uptimeMillis()

        try {
            faceDetector?.detectAsync(mpIMage, frameTime)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun returnLiveStreamResult(
        result: FaceDetectorResult,
        input: MPImage,
    ) {
        val finishedTimeMs = SystemClock.uptimeMillis()
        val inferenceTime = finishedTimeMs - result.timestampMs()

        faceDetectorListener?.onResults(
            ResultBundle(
                results = listOf(result),
                inferenceTime = inferenceTime,
                inputImageHeight = input.height,
                inputImageWidth = input.width,
            ),
        )
    }

    private fun returnLiveStreamError(error: RuntimeException) {
        faceDetectorListener?.onError(error = error.message ?: "An unknown error has occurred")
    }

    companion object {
        val TAG: String = FaceDetectorHelper::class.java.simpleName
        const val THRESHOLD_DEFAULT = 0.5f
        const val UNKNOWN_ERROR_CODE = 0
    }

    interface DetectorListener {
        fun onError(
            error: String,
            errorCode: Int = UNKNOWN_ERROR_CODE,
        )

        fun onResults(result: ResultBundle)
    }

    data class ResultBundle(
        val results: List<FaceDetectorResult>,
        val inferenceTime: Long,
        val inputImageHeight: Int,
        val inputImageWidth: Int,
    )
}
