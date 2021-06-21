package com.veriff.sample

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.veriff.sdk.identity.data.repository.IFaceRecognitionRepository
import com.veriff.sdk.identity.data.repository.ITextRecognitionRepository
import com.veriff.sdk.identity.data.repository.local.face.FaceRecognitionRepository
import com.veriff.sdk.identity.data.repository.local.text.TextRecognitionRepository

object ServiceLocator {

    @Volatile
    lateinit var faceRecRepository: IFaceRecognitionRepository
        @VisibleForTesting set
    @Volatile
    lateinit var textRecRepository: ITextRecognitionRepository
        @VisibleForTesting set

    fun provideFaceRecognitionRepository(context: Context): IFaceRecognitionRepository {
        synchronized(this) {
            return faceRecRepository ?: createFaceRecognitionRepository()
        }
    }
    fun provideTextRecognitionRepository(context: Context): ITextRecognitionRepository {
        synchronized(this) {
            return textRecRepository ?: createTextRecognitionRepository()
        }
    }

    private fun createFaceRecognitionRepository(): FaceRecognitionRepository {
        faceRecRepository = FaceRecognitionRepository()
        return faceRecRepository as FaceRecognitionRepository
    }

    private fun createTextRecognitionRepository(): TextRecognitionRepository {
        textRecRepository = TextRecognitionRepository()
        return textRecRepository as TextRecognitionRepository
    }
}
