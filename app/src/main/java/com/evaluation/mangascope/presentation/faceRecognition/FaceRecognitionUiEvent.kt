package com.evaluation.mangascope.presentation.faceRecognition

import androidx.lifecycle.LifecycleOwner

sealed class FaceRecognitionUiEvent {
    data class BindToCamera(
        val lifecycleOwner: LifecycleOwner,
    ) : FaceRecognitionUiEvent()

    data object OnPause : FaceRecognitionUiEvent()

    data object OnResume : FaceRecognitionUiEvent()

    data object UserMessageShown : FaceRecognitionUiEvent()
}
