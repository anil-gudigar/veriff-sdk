package com.veriff.sdk.identity.data.repository.local.text

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognizer
import com.veriff.sdk.identity.data.repository.contracts.ITextRecognitionRepository
import javax.inject.Inject

class TextFaceRecognitionRepository @Inject constructor(private val textRecognizerClient: TextRecognizer) :
    ITextRecognitionRepository {
    override suspend fun detectInImage(image: InputImage): LiveData<Text> {
        var resultsPostLiveData = MutableLiveData<Text>()
        textRecognizerClient.process(image)
            .addOnSuccessListener { results ->
                resultsPostLiveData.postValue(results)
            }
            .addOnCanceledListener {
                resultsPostLiveData.postValue(null)
            }
        return resultsPostLiveData
    }
}