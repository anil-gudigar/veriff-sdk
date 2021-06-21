package com.veriff.sdk.identity.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognizer
import org.mockito.Mock

class FakeTextRecognitionRepository : ITextRecognitionRepository {
    var mText = MutableLiveData<Text>()
    @Mock
    val recognizer: TextRecognizer?= null
    @Mock
    val text: Text?= null

    //TODO:MLKitContext is null so added a hack for text case ( need to use Robolectric to mock MLKitContext)
    override suspend fun detectInImage(image: InputImage): LiveData<Text> {
        recognizer?.let {
            it.process(image)?.addOnSuccessListener { results ->
                mText.postValue(results)
            }.addOnCanceledListener {
                mText.postValue(null)
            }
        }?: kotlin.run {
            mText.postValue(text)
        }
        return mText
    }
}