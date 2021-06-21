package com.veriff.sdk.identity.data.repository.local.face

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetector
import com.veriff.sdk.identity.data.repository.IFaceRecognitionRepository
import org.mockito.Mock

class FakeFaceRecognitionRepository : IFaceRecognitionRepository {
    val mFaces = MutableLiveData<List<Face>>()
    @Mock
    var detector: FaceDetector? = null
    override suspend fun detectInImage(image: InputImage): LiveData<List<Face>> {
        detector?.process(image)
            ?.addOnSuccessListener { results ->
                mFaces.postValue(results)
            }
            ?.addOnCanceledListener {
                mFaces.postValue(null)
            }
        return mFaces
    }
}