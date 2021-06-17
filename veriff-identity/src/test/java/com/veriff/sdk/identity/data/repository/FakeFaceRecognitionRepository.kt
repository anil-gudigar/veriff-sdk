package com.veriff.sdk.identity.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face

class FakeFaceRecognitionRepository:IFaceRecognitionRepository {
    private val mFaces = MutableLiveData<List<Face>>()
    override suspend fun detectInImage(image: InputImage): LiveData<List<Face>> {
        mFaces.postValue(listOf<Face>())
       return mFaces
    }
}