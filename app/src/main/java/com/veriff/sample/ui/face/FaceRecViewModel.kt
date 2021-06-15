package com.veriff.sample.ui.face

import android.graphics.Bitmap
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.*
import com.google.mlkit.vision.face.Face
import com.veriff.sdk.identity.VeriffIdentityManager
import com.veriff.sdk.identity.live.mlkit.vision.VisionType

class FaceRecViewModel : ViewModel() {
    internal var checkPermission: ActivityResultLauncher<Array<String>>? = null
    var veriffIdentityManager: VeriffIdentityManager<List<Face>>? = null
    val visionType: VisionType = VisionType.Face

    suspend fun runFaceDetection(image: Bitmap): LiveData<List<Face>>? {
          return  veriffIdentityManager?.runFaceContourDetection(image)
    }
}