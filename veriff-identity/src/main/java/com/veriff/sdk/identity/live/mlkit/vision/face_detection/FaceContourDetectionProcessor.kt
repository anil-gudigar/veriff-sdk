package com.veriff.sdk.identity.live.mlkit.vision.face_detection

import android.graphics.Rect
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.veriff.sdk.camerax.BaseImageAnalyzer
import com.veriff.sdk.camerax.GraphicOverlay
import com.veriff.sdk.identity.callback.IdentityCallback
import java.io.IOException

/**
 * Face contour detection processor
 *
 * @property view
 * @property identityCallback
 * @constructor Create empty Face contour detection processor
 */
class FaceContourDetectionProcessor(private val view: GraphicOverlay, private val identityCallback: IdentityCallback<List<Face>>) :
    BaseImageAnalyzer<List<Face>>() {

    private val realTimeOpts = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
        .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
        .build()

    override val graphicOverlay: GraphicOverlay
        get() = view

    private val detector = FaceDetection.getClient(realTimeOpts)

    /**
     * Detect in image
     *
     * @param image
     * @return Task<List<Face>>
     */
    override fun detectInImage(image: InputImage): Task<List<Face>> {
        return detector.process(image)
    }

    override fun stop() {
        try {
            detector.close()
        } catch (e: IOException) {
            Log.e(TAG, "Exception thrown while trying to close Face Detector: $e")
        }
    }

    /**
     * On success
     *
     * @param results
     * @param graphicOverlay
     * @param rect
     */
    override fun onSuccess(
        results: List<Face>,
        graphicOverlay: GraphicOverlay,
        rect: Rect
    ) {
        identityCallback.onSuccess(results)
        graphicOverlay.clear()
        results.forEach {
            val faceGraphic = FaceContourGraphic(graphicOverlay, it, rect)
            graphicOverlay.add(faceGraphic)
        }
        graphicOverlay.postInvalidate()
    }

    /**
     * On failure
     *
     * @param exception
     */
    override fun onFailure(e: Exception) {
        identityCallback.onFailure(e)
        Log.w(TAG, "Face Detector failed.$e")
    }

    companion object {
        private const val TAG = "FaceDetectorProcessor"
    }

}