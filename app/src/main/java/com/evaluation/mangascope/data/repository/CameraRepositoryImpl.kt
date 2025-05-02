package com.evaluation.mangascope.data.repository

import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.UseCase
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.lifecycle.LifecycleOwner
import com.evaluation.mangascope.domain.repository.CameraRepository
import kotlinx.coroutines.awaitCancellation

class CameraRepositoryImpl(
    private val context: Context,
) : CameraRepository {
    override suspend fun bindToCamera(
        lifecycleOwner: LifecycleOwner,
        cameraPreviewUseCase: UseCase,
        imageAnalysis: ImageAnalysis,
    ) {
        val cameraProvider = ProcessCameraProvider.awaitInstance(context)
        val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            cameraPreviewUseCase,
            imageAnalysis,
        )

        // Cancellation signals we're done with the camera
        try {
            awaitCancellation()
        } finally {
            cameraProvider.unbindAll()
        }
    }
}
