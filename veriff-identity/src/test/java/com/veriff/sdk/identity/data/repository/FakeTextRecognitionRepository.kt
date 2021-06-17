package com.veriff.sdk.identity.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text

class FakeTextRecognitionRepository : ITextRecognitionRepository {
    private val mText = MutableLiveData<Text>()
    override suspend fun detectInImage(image: InputImage): LiveData<Text> {
        return mText
    }
}