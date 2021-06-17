package com.veriff.sdk.identity.data.repository.local.text

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.veriff.sdk.identity.data.repository.ITextRecognitionRepository

/**
 * Text recognition repository
 *
 * @constructor Create empty Text recognition repository
 */
class TextRecognitionRepository : ITextRecognitionRepository {
    var recognizer: TextRecognizer = TextRecognition.getClient()

    /**
     * Detect in image
     *
     * @param image
     * @return
     */
    override suspend fun detectInImage(image: InputImage): LiveData<Text> {
        var resultsPostLiveData = MutableLiveData<Text>()
        recognizer.process(image)
            .addOnSuccessListener { results ->
                resultsPostLiveData.postValue(results)
            }
            .addOnCanceledListener {
                resultsPostLiveData.postValue(null)
            }
        return resultsPostLiveData
    }
}