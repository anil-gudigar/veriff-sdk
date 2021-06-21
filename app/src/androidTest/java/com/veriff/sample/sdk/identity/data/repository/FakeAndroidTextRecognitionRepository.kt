package com.veriff.sample.sdk.identity.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognizer
import com.veriff.sdk.identity.data.repository.ITextRecognitionRepository
import org.mockito.Mock

class FakeAndroidTextRecognitionRepository :ITextRecognitionRepository{
    var mText = MutableLiveData<Text>()
    @Mock
    val recognizer: TextRecognizer?= null
    override suspend fun detectInImage(image: InputImage): LiveData<Text> {
        recognizer?.process(image)?.addOnSuccessListener { results ->
            mText.postValue(results)
        }?.addOnCanceledListener {
            mText.postValue(null)
        }
        return mText
    }
}