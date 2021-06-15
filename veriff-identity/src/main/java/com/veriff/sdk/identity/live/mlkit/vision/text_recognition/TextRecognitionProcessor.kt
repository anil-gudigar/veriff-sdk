package com.veriff.sdk.identity.live.mlkit.vision.text_recognition

import android.graphics.Rect
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.veriff.sdk.camerax.BaseImageAnalyzer
import com.veriff.sdk.camerax.GraphicOverlay
import com.veriff.sdk.identity.callback.IdentityCallback
import java.io.IOException

/**
 * Text recognition processor
 *
 * @property view
 * @property identityCallback
 * @constructor Create empty Text recognition processor
 */
class TextRecognitionProcessor(private val view: GraphicOverlay, private val identityCallback:IdentityCallback<Text>) : BaseImageAnalyzer<Text>() {

    private val recognizer = TextRecognition.getClient()

    /**
     * Detect in image
     *
     * @param image
     * @return Task<Text>
     */
    override fun detectInImage(image: InputImage): Task<Text> {
        return recognizer.process(image)
    }

    override val graphicOverlay: GraphicOverlay
        get() = view

    override fun stop() {
        try {
            recognizer.close()
        } catch (e: IOException) {
            Log.e(TAG, "Exception thrown while trying to close Text Recognition: $e")
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
        results: Text,
        graphicOverlay: GraphicOverlay,
        rect: Rect

    ) {
        identityCallback.onSuccess(results)
        graphicOverlay.clear()
        results.textBlocks.forEach {
            val textGraphic = TextRecognitionGraphic(graphicOverlay, it, rect)
            graphicOverlay.add(textGraphic)
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
        Log.w(TAG, "Text Recognition failed.$e")
    }

    companion object {
        private const val TAG = "TextProcessor"
    }

}