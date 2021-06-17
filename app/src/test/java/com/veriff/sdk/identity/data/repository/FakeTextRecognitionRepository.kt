package com.veriff.sdk.identity.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.text.Text
import org.mockito.Mock

class FakeTextRecognitionRepository : ITextRecognitionRepository {
    val mText = MutableLiveData<Text>()
    @Mock
    val text: Text ?=null
    override suspend fun detectInImage(image: InputImage): LiveData<Text> {
        mText.postValue(text)
        return mText
    }
}