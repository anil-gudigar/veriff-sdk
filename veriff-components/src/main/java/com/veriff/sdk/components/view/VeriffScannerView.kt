package com.veriff.sdk.components.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.camera.view.CameraView
import androidx.camera.view.PreviewView
import com.veriff.sdk.camerax.GraphicOverlay
import com.veriff.sdk.components.R

/**
 * Veriff scanner view
 *
 * @constructor Create empty Veriff scanner view
 */
class VeriffScannerView : LinearLayout {
    var previewViewFinder: PreviewView? = null
    var graphicOverlayFinder: GraphicOverlay? = null
    var cameraSelector: ImageButton? = null
    var cameraShutter: ImageButton? = null

    constructor(context: Context) : super(context) {
        View.inflate(context, R.layout.veriff_scanner_view, this)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        View.inflate(context, R.layout.veriff_scanner_view, this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        previewViewFinder = findViewById(R.id.previewView_finder)
        graphicOverlayFinder = findViewById(R.id.graphicOverlay_finder)
        cameraSelector = findViewById(R.id.camera_selector)
        cameraShutter = findViewById(R.id.camera_shutter)
    }
}