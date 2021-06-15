package com.veriff.sdk.identity.data.repository.local.face

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetector
import com.veriff.sdk.identity.data.repository.contracts.IFaceRecognitionRepository
import javax.inject.Inject

class FaceFaceRecognitionRepository @Inject constructor(private val faceDetectorClient: FaceDetector) :
    IFaceRecognitionRepository {
    override suspend fun detectInImage(image: InputImage): LiveData<List<Face>> {
        var resultsPostLiveData = MutableLiveData<List<Face>>()
        faceDetectorClient.process(image)
            .addOnSuccessListener { results ->
                resultsPostLiveData.postValue(results)
            }
            .addOnCanceledListener {
                resultsPostLiveData.postValue(null)
            }
        return resultsPostLiveData
    }

}