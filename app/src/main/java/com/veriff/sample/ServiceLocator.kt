package com.veriff.sample

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.veriff.sdk.identity.data.repository.local.face.FaceRecognitionRepository
import com.veriff.sdk.identity.data.repository.local.text.TextRecognitionRepository

object ServiceLocator {

    @Volatile
    var faceRecRepository: FaceRecognitionRepository? = null
        @VisibleForTesting set
    @Volatile
    var textRecRepository: TextRecognitionRepository? = null
        @VisibleForTesting set

    fun provideFaceRecognitionRepository(context: Context): FaceRecognitionRepository {
        synchronized(this) {
            return faceRecRepository ?: createFaceRecognitionRepository()
        }
    }
    fun provideTextRecognitionRepository(context: Context): TextRecognitionRepository {
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
