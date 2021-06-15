package com.veriff.sdk.identity.live.camerax

import android.annotation.SuppressLint
import android.content.Context
import android.util.DisplayMetrics
import android.util.Log
import android.util.Size
import android.view.ScaleGestureDetector
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.face.Face
import com.veriff.sdk.camerax.GraphicOverlay
import com.veriff.sdk.identity.callback.IdentityCallback
import com.veriff.sdk.identity.live.mlkit.vision.VisionType
import com.veriff.sdk.identity.live.mlkit.vision.face_detection.FaceContourDetectionProcessor
import com.veriff.sdk.identity.live.mlkit.vision.text_recognition.TextRecognitionProcessor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Camera manager
 *
 * @param T
 * @property context
 * @property finderView
 * @property lifecycleOwner
 * @property graphicOverlay
 * @property identityCallback
 * @constructor Create empty Camera manager
 */
class CameraManager<T>(
    private val context: Context,
    private val finderView: PreviewView?,
    private val lifecycleOwner: LifecycleOwner,
    private val graphicOverlay: GraphicOverlay,
    private val identityCallback: IdentityCallback<T>?
) {

    private var preview: Preview? = null
    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private var imageAnalyzer: ImageAnalysis? = null

    lateinit var cameraExecutor: ExecutorService
    lateinit var imageCapture: ImageCapture
    lateinit var metrics: DisplayMetrics

    private var analyzerVisionType: VisionType = VisionType.Face
    var rotation: Float = 0f
    var cameraSelectorOption = CameraSelector.LENS_FACING_BACK

    init {
        createNewExecutor()
    }

    /**
     * Create new executor for Camera
     *
     */
    private fun createNewExecutor() {
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    /**
     * Select analyzer Face,OCR depending on users choice
     *
     * @return ImageAnalysis.Analyzer
     */
    private fun selectAnalyzer(): ImageAnalysis.Analyzer {
        return when (analyzerVisionType) {
            VisionType.OCR -> TextRecognitionProcessor(
                graphicOverlay,
                identityCallback as IdentityCallback<Text>
            )
            VisionType.Face -> FaceContourDetectionProcessor(
                graphicOverlay,
                identityCallback as IdentityCallback<List<Face>>
            )
        }
    }

    /**
     * Set camera config
     *
     * @param cameraProvider
     * @param cameraSelector
     */
    private fun setCameraConfig(
        cameraProvider: ProcessCameraProvider?,
        cameraSelector: CameraSelector
    ) {
        try {
            cameraProvider?.unbindAll()
            camera = cameraProvider?.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageCapture,
                imageAnalyzer
            )
            preview?.setSurfaceProvider(
                finderView?.createSurfaceProvider()
            )
        } catch (e: Exception) {
            Log.e(TAG, "Use case binding failed", e)
        }
    }

    /**
     * Set up pinch to zoom
     *
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun setUpPinchToZoom() {
        val listener = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                val currentZoomRatio: Float = camera?.cameraInfo?.zoomState?.value?.zoomRatio ?: 1F
                val delta = detector.scaleFactor
                camera?.cameraControl?.setZoomRatio(currentZoomRatio * delta)
                return true
            }
        }
        val scaleGestureDetector = ScaleGestureDetector(context, listener)
        finderView?.setOnTouchListener { _, event ->
            finderView.post {
                scaleGestureDetector.onTouchEvent(event)
            }
            return@setOnTouchListener true
        }
    }

    /**
     * Start camera with all configuration set
     *
     */
    fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener(
            {
                cameraProvider = cameraProviderFuture.get()
                preview = Preview.Builder().build()

                imageAnalyzer = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also {
                        it.setAnalyzer(cameraExecutor, selectAnalyzer())
                    }

                val cameraSelector = CameraSelector.Builder()
                    .requireLensFacing(cameraSelectorOption)
                    .build()

                metrics = DisplayMetrics().also { finderView?.display?.getRealMetrics(it) }

                imageCapture =
                    ImageCapture.Builder()
                        .setTargetAspectRatio(AspectRatio.RATIO_16_9)
                        .build()

                setUpPinchToZoom()
                setCameraConfig(cameraProvider, cameraSelector)

            }, ContextCompat.getMainExecutor(context)
        )
    }

    /**
     * Change camera selector front camera / back camera
     *
     */
    fun changeCameraSelector() {
        cameraProvider?.unbindAll()
        cameraSelectorOption =
            if (cameraSelectorOption == CameraSelector.LENS_FACING_BACK) CameraSelector.LENS_FACING_FRONT
            else CameraSelector.LENS_FACING_BACK
        startCamera()
    }

    /**
     * Change analyzer
     *
     * @param visionType
     */
    fun changeAnalyzer(visionType: VisionType) {
        if (analyzerVisionType != visionType) {
            cameraProvider?.unbindAll()
            analyzerVisionType = visionType
            startCamera()
        }
    }

    /**
     * Is horizontal mode
     *
     * @return Boolean
     */
    fun isHorizontalMode(): Boolean {
        return rotation == 90f || rotation == 270f
    }

    companion object {
        private const val TAG = "CameraXBasic"
    }


}