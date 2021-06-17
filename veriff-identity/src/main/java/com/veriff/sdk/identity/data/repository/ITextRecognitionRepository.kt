package com.veriff.sdk.identity.data.repository

import androidx.lifecycle.LiveData
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.common.InputImage

interface ITextRecognitionRepository {
     suspend fun detectInImage(image: InputImage): LiveData<Text>
}