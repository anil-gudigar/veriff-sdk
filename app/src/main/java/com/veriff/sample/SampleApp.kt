package com.veriff.sample

import android.app.Application
import com.veriff.sdk.core.app.VeriffApp
import com.veriff.sdk.identity.data.repository.IFaceRecognitionRepository
import com.veriff.sdk.identity.data.repository.ITextRecognitionRepository
import com.veriff.sdk.identity.data.repository.local.face.FaceRecognitionRepository
import com.veriff.sdk.identity.data.repository.local.text.TextRecognitionRepository

class SampleApp: Application() {
    val faceRecognitionRepository: IFaceRecognitionRepository
        get() = ServiceLocator.provideFaceRecognitionRepository(this)
    val textRecognitionRepository: ITextRecognitionRepository
        get() = ServiceLocator.provideTextRecognitionRepository(this)
    override fun onCreate() {
        super.onCreate()
        VeriffApp.initialize(this)
    }
}