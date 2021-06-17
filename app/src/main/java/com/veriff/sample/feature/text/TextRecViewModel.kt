package com.veriff.sample.feature.text

import android.graphics.Bitmap
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.veriff.sdk.identity.VeriffIdentityManager
import com.veriff.sdk.identity.data.repository.ITextRecognitionRepository
import com.veriff.sdk.identity.data.repository.local.text.TextRecognitionRepository
import com.veriff.sdk.identity.live.mlkit.vision.VisionType
import javax.inject.Inject

class TextRecViewModel @Inject constructor(private val repository: ITextRecognitionRepository) :
    ViewModel() {
    internal var checkPermission: ActivityResultLauncher<Array<String>>? = null
    var veriffIdentityManager: VeriffIdentityManager<Text>? = null
    val visionType: VisionType = VisionType.OCR
    var textRecData: MutableLiveData<Text> = MutableLiveData<Text>()

    suspend fun runTextRecognition(image: Bitmap) {
        val inputImage = InputImage.fromBitmap(image, 0)
        textRecData.postValue((repository as TextRecognitionRepository).detectInImage(inputImage).value)
    }
}

@Suppress("UNCHECKED_CAST")
class TextRecViewModelFactory(
    private val textRepository: TextRecognitionRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (TextRecViewModel(textRepository) as T)
}