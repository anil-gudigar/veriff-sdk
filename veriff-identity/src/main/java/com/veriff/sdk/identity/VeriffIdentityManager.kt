package com.veriff.sdk.identity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.media.Image
import android.util.Log
import android.view.OrientationEventListener
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.veriff.sdk.components.view.VeriffScannerView
import com.veriff.sdk.core.app.VeriffApp
import com.veriff.sdk.core.util.*
import com.veriff.sdk.identity.callback.IdentityCallback
import com.veriff.sdk.identity.live.camerax.CameraManager
import com.veriff.sdk.identity.data.repository.local.face.FaceRecognitionRepository
import com.veriff.sdk.identity.data.repository.local.text.TextRecognitionRepository
import com.veriff.sdk.identity.domain.usecases.FaceRecognitionUseCase
import com.veriff.sdk.identity.domain.usecases.TextRecognitionUseCase
import com.veriff.sdk.identity.live.mlkit.vision.VisionType

/**
 * Veriff identity manager
 *
 * @param T
 * @property veriffApp Veriff App instance
 * @property activity host activity
 * @property checkPermission check permission object from fragment
 * @property scannerView VeriffScannerView view instance
 * @property visionType visionType FACE / OCR
 * @property identityCallback call back from analyzer
 * @constructor Create empty Veriff identity manager
 */
class VeriffIdentityManager<T>(
    private val veriffApp: VeriffApp,
    private val activity: Activity,
    private val checkPermission: ActivityResultLauncher<Array<String>>,
    private val scannerView: VeriffScannerView,
    private val visionType: VisionType,
    private var identityCallback: IdentityCallback<T>
) {
    private var baseContext: Context = veriffApp.applicationContext
    var cameraManager: CameraManager<T>? = null

    init {
        createCameraManager(visionType)
        checkForPermissionsGranted(activity)
    }

    /**
     * Check All permissions granted
     *
     */
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ActivityCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    /**
     * Create camera manager with vision Type as input ( FACE / OCR)
     *
     * @param visionType vision Type as input ( FACE / OCR)
     */
    private fun createCameraManager(visionType: VisionType) {
        cameraManager = scannerView.previewViewFinder?.let { previewViewFinder ->
            scannerView.graphicOverlayFinder?.let { graphicOverlayFinder ->
                CameraManager(
                    baseContext,
                    previewViewFinder,
                    activity as LifecycleOwner,
                    graphicOverlayFinder,
                    identityCallback
                )
            }
        }
        cameraManager?.changeAnalyzer(visionType)

        scannerView.cameraSelector?.setOnClickListener {
            cameraManager?.changeCameraSelector()
        }

        scannerView.cameraShutter?.setOnClickListener {
            takePicture()
        }
    }

    /**
     * Check for permissions granted
     *
     * @param activity
     */
    private fun checkForPermissionsGranted(activity: Activity) {
        if (allPermissionsGranted()) {
            cameraManager?.startCamera()
        } else {
            checkPermission.launch(REQUIRED_PERMISSIONS)
        }
    }

    /**
     * On request permissions result
     *
     */
    fun onRequestPermissionsResult() {
        if (allPermissionsGranted()) {
            cameraManager?.startCamera()
        } else {
            activity.finish()
        }
    }

    /**
     * Take picture
     *
     */
    private fun takePicture() {
        Toast.makeText(baseContext, "take a picture!", Toast.LENGTH_SHORT).show()
        cameraManager?.cameraExecutor?.let { cameraExecutor ->
            cameraManager?.imageCapture?.takePicture(
                cameraExecutor,
                object : ImageCapture.OnImageCapturedCallback() {
                    @SuppressLint("UnsafeExperimentalUsageError", "RestrictedApi")
                    override fun onCaptureSuccess(image: ImageProxy) {
                        image.image?.let {
                            imageToBitmapSaveGallery(it)
                        }
                        super.onCaptureSuccess(image)
                    }

                    override fun onError(exception: ImageCaptureException) {
                        super.onError(exception)
                        Log.i("Anil","Error on Capture :"+exception.message)
                    }
                })
        }
    }

    /**
     * Image to bitmap save gallery
     *
     * @param image
     */
    private fun imageToBitmapSaveGallery(image: Image) {
        scannerView.previewViewFinder?.let { previewViewFinder ->
            cameraManager?.let { cameraManager ->
                image.imageToBitmap()
                    ?.scaleImage(
                        previewViewFinder,
                        cameraManager.isHorizontalMode()
                    )
                    ?.let { bitmap ->
                        scannerView.graphicOverlayFinder?.processCanvas?.drawBitmap(
                            bitmap,
                            0f,
                            bitmap.getBaseYByView(
                                scannerView.graphicOverlayFinder!!,
                                cameraManager.isHorizontalMode()
                            ),
                            Paint().apply {
                                xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OVER)
                            })
                        identityCallback.onTakePictureSuccess(scannerView.graphicOverlayFinder?.processBitmap)
                    }
            }
        }
    }


    suspend fun runFaceContourDetection(repository: FaceRecognitionRepository,mSelectedImage: Bitmap): LiveData<List<Face>> {
        val image = InputImage.fromBitmap(mSelectedImage, 0)
        return FaceRecognitionUseCase(repository).execute(
            FaceRecognitionUseCase.Params(image)
        )
    }

    suspend fun runTextRecognition(recognitionRepository: TextRecognitionRepository,mSelectedImage: Bitmap): LiveData<Text> {
        val image = InputImage.fromBitmap(mSelectedImage, 0)
        return TextRecognitionUseCase(recognitionRepository).execute(
            TextRecognitionUseCase.Params(image)
        )
    }
    private fun setOrientationEvent() {
        val orientationEventListener = object : OrientationEventListener(this as Context) {
            override fun onOrientationChanged(orientation: Int) {
                val rotation: Float = when (orientation) {
                    in 45..134 -> 270f
                    in 135..224 -> 180f
                    in 225..314 -> 90f
                    else -> 0f
                }
                cameraManager?.rotation = rotation
            }
        }
        orientationEventListener.enable()
    }

}