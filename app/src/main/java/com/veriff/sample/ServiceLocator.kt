package com.veriff.sample

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.veriff.sdk.identity.data.repository.IFaceRecognitionRepository
import com.veriff.sdk.identity.data.repository.ITextRecognitionRepository
import com.veriff.sdk.identity.data.repository.local.face.FaceRecognitionRepository
import com.veriff.sdk.identity.data.repository.local.text.TextRecognitionRepository

object ServiceLocator {

    @Volatile
    var faceRecRepository: IFaceRecognitionRepository? = null
        @VisibleForTesting set
    @Volatile
    var textRecRepository: ITextRecognitionRepository? = null
        @VisibleForTesting set

    fun provideFaceRecognitionRepository(context: Context): IFaceRecognitionRepository {
        synchronized(this) {
            return faceRecRepository ?: createFaceRecognitionRepository(context)
        }
    }
    fun provideTextRecognitionRepository(context: Context): ITextRecognitionRepository {
        synchronized(this) {
            return textRecRepository ?: createTextRecognitionRepository(context)
        }
    }

    private fun createFaceRecognitionRepository(context: Context): FaceRecognitionRepository {
        faceRecRepository = FaceRecognitionRepository()
        return faceRecRepository as FaceRecognitionRepository
    }

    private fun createTextRecognitionRepository(context: Context): TextRecognitionRepository {
        textRecRepository = TextRecognitionRepository()
        return textRecRepository as TextRecognitionRepository
    }
}
