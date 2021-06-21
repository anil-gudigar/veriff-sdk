package com.veriff.sdk.identity.data.repository.local.face

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.veriff.sdk.identity.data.repository.IFaceRecognitionRepository

/**
 * Face recognition repository
 *
 * @constructor Create empty Face recognition repository
 */
class FaceRecognitionRepository : IFaceRecognitionRepository {
    var options: FaceDetectorOptions
    var detector: FaceDetector

    init {
        options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
            .build()
        detector = FaceDetection.getClient(options)
    }

    /**
     * Detect in image
     *
     * @param image
     * @return
     */
    override suspend fun detectInImage(image: InputImage): LiveData<List<Face>> {
        var resultsPostLiveData = MutableLiveData<List<Face>>()
        detector.process(image)
            .addOnSuccessListener { results ->
                resultsPostLiveData.postValue(results)
            }
            .addOnCanceledListener {
                resultsPostLiveData.postValue(null)
            }
        return resultsPostLiveData
    }

}