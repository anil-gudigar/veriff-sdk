package com.veriff.sdk.identity.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetector
import org.mockito.Mock

class FakeFaceRecognitionRepository : IFaceRecognitionRepository {
    var mFaces = MutableLiveData<List<Face>>()

    @Mock
    val face: Face? = null

    @Mock
    var detector: FaceDetector? = null

    //TODO:MLKitContext is null so added a hack for text case ( need to use Robolectric to mock MLKitContext)
    override suspend fun detectInImage(image: InputImage): LiveData<List<Face>> {
        detector?.let {
            it.process(image)
                .addOnSuccessListener { results ->
                    mFaces.postValue(results)
                }
                .addOnCanceledListener {
                    mFaces.postValue(null)
                }
        } ?: kotlin.run {
            val listOfFace = listOf<Face>()
            listOfFace.plus(face)
            mFaces.postValue(listOfFace)
        }
        return mFaces
    }
}