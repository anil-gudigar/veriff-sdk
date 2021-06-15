package com.veriff.sdk.identity.data.repository.contracts

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.veriff.sdk.core.domain.IOTaskResult
import kotlinx.coroutines.flow.Flow

interface IFaceRecognitionRepository {
     suspend fun detectInImage(image: InputImage): LiveData<List<Face>>
}