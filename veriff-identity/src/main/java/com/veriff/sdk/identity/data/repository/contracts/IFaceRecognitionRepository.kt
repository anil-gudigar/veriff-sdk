package com.veriff.sdk.identity.data.repository.contracts

import androidx.lifecycle.LiveData
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face

interface IFaceRecognitionRepository {
     suspend fun detectInImage(image: InputImage): LiveData<List<Face>>
}