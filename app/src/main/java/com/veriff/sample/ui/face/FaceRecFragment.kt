package com.veriff.sample.ui.face

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.mlkit.vision.face.Face
import com.veriff.sample.MainActivity
import com.veriff.sample.R
import com.veriff.sample.databinding.FragmentFaceRecBinding
import com.veriff.sdk.core.app.VeriffApp
import com.veriff.sdk.core.util.saveToGallery
import com.veriff.sdk.identity.VeriffIdentityManager
import com.veriff.sdk.identity.callback.IdentityCallback
import kotlinx.coroutines.*
import java.io.IOException
import java.io.InputStream
import java.lang.Runnable

class FaceRecFragment : Fragment(), IdentityCallback<List<Face>> {

    private lateinit var faceRecViewModel: FaceRecViewModel
    private var _binding: FragmentFaceRecBinding? = null
    private val job = Job()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        faceRecViewModel =
            ViewModelProvider(this).get(FaceRecViewModel::class.java)
        _binding = FragmentFaceRecBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initVerifyIdentity()
        //val mSelectedImage = context?.let { getBitmapFromAsset(it, "grace_hopper.jpg") }
        //facRecFromBitmap(mSelectedImage)
    }

    private fun facRecFromBitmap(mSelectedImage: Bitmap?) {
        viewLifecycleOwner.lifecycleScope.launch {
            mSelectedImage?.let {
                faceRecViewModel.runFaceDetection(it)
                    ?.observe(viewLifecycleOwner, Observer { results ->
                        if (results.isEmpty()) {
                            Log.i("Anil", "No Face :")
                        } else {
                            Log.i("Anil", "Face :" + results.size)
                        }
                    })
            }
        }
    }

    private fun initVerifyIdentity() {
        faceRecViewModel.checkPermission =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                var allPermissionGranted = false
                permissions.entries.forEach {
                    allPermissionGranted = it.value
                }
                if (allPermissionGranted) {
                    faceRecViewModel.veriffIdentityManager?.onRequestPermissionsResult()
                }
            }

        faceRecViewModel.veriffIdentityManager = activity?.let {
            faceRecViewModel.checkPermission?.let { checkPermission ->
                VeriffIdentityManager(
                    VeriffApp.getInstance(),
                    it,
                    checkPermission,
                    binding.veriffScannerView,
                    faceRecViewModel.visionType,
                    this
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSuccess(results: List<Face>) {
        if (isVisible) {
            if (results.isEmpty()) {
                binding.textMessage.text = getText(R.string.no_face)
            } else {
                binding.textMessage.text = "Face :" + results.size
            }
        }
    }

    override fun onFailure(e: Exception) {
        if (isVisible) {
            Toast.makeText(context, "Exception :" + e.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onTakePictureSuccess(bitmap: Bitmap?) {
        facRecFromBitmap(bitmap)
        var uri:Uri? = null
        context?.let { uri = bitmap?.saveToGallery(it) }
        activity?.runOnUiThread(Runnable {
            if (isVisible) {
                Toast.makeText(
                    context,
                    "Picture Saved in  :" + uri?.encodedPath,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    fun getBitmapFromAsset(context: Context, filePath: String?): Bitmap? {
        val assetManager = context.assets
        val `is`: InputStream
        var bitmap: Bitmap? = null
        try {
            `is` = assetManager.open(filePath!!)
            bitmap = BitmapFactory.decodeStream(`is`)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bitmap
    }
}