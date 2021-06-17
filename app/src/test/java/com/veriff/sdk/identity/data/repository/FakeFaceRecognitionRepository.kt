package com.veriff.sdk.identity.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import org.mockito.Mock

class FakeFaceRecognitionRepository: IFaceRecognitionRepository {
    val mFaces = MutableLiveData<List<Face>>()
    @Mock
    val face: Face?= null
    override suspend fun detectInImage(image: InputImage): LiveData<List<Face>> {
        val listOfFace = listOf<Face>()
        listOfFace.plus(face)
        mFaces.postValue(listOfFace)
       return mFaces
    }
}