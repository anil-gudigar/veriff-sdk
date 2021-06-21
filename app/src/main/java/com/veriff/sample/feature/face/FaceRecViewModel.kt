package com.veriff.sample.feature.face

import android.graphics.Bitmap
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.*
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.veriff.sdk.identity.VeriffIdentityManager
import com.veriff.sdk.identity.data.repository.IFaceRecognitionRepository
import com.veriff.sdk.identity.data.repository.local.face.FaceRecognitionRepository
import com.veriff.sdk.identity.domain.usecases.FaceRecognitionUseCase
import com.veriff.sdk.identity.live.mlkit.vision.VisionType
import javax.inject.Inject

class FaceRecViewModel @Inject constructor(private val repository: IFaceRecognitionRepository) :
    ViewModel() {
    internal var checkPermission: ActivityResultLauncher<Array<String>>? = null
    var veriffIdentityManager: VeriffIdentityManager<List<Face>>? = null
    val visionType: VisionType = VisionType.Face
    val faceRecData = MutableLiveData<List<Face>>()

    suspend fun runFaceDetection(image: Bitmap) :LiveData<List<Face>>{
        val inputImage = InputImage.fromBitmap(image, 0)
       return FaceRecognitionUseCase((repository as FaceRecognitionRepository)).execute(
            FaceRecognitionUseCase.Params(inputImage)
        )
    }
}

@Suppress("UNCHECKED_CAST")
class FaceRecViewModelFactory(
    private val faceRecognitionRepository: FaceRecognitionRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (FaceRecViewModel(faceRecognitionRepository) as T)
}
