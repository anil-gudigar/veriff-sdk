package com.veriff.sample

import android.app.Application
import com.veriff.sdk.core.app.VeriffApp
import com.veriff.sdk.identity.data.repository.local.face.FaceRecognitionRepository
import com.veriff.sdk.identity.data.repository.local.text.TextRecognitionRepository

class SampleApp: Application() {
    val faceRecognitionRepository: FaceRecognitionRepository
        get() = ServiceLocator.provideFaceRecognitionRepository(this)
    val textRecognitionRepository: TextRecognitionRepository
        get() = ServiceLocator.provideTextRecognitionRepository(this)
    override fun onCreate() {
        super.onCreate()
        VeriffApp.initialize(this)
    }
}