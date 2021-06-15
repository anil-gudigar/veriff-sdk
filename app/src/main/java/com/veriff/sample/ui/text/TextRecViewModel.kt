package com.veriff.sample.ui.text

import android.graphics.Bitmap
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.text.Text
import com.veriff.sdk.identity.VeriffIdentityManager
import com.veriff.sdk.identity.live.mlkit.vision.VisionType

class TextRecViewModel : ViewModel() {
    internal var checkPermission: ActivityResultLauncher<Array<String>>? = null
    var veriffIdentityManager: VeriffIdentityManager<Text>? = null
    val visionType: VisionType = VisionType.OCR

    suspend fun runTextRecognition(image: Bitmap): LiveData<Text>? {
        return  veriffIdentityManager?.runTextRecognition(image)
    }
}