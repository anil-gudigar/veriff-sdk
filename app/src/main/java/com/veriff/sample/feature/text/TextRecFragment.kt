package com.veriff.sample.feature.text

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.google.mlkit.vision.text.Text
import com.veriff.sample.R
import com.veriff.sample.SampleApp
import com.veriff.sample.databinding.FragmentTextRecBinding
import com.veriff.sdk.core.app.VeriffApp
import com.veriff.sdk.core.util.saveToGallery
import com.veriff.sdk.identity.VeriffIdentityManager
import com.veriff.sdk.identity.callback.IdentityCallback
import com.veriff.sdk.identity.data.repository.local.text.TextRecognitionRepository
import kotlinx.coroutines.launch

class TextRecFragment : Fragment(), IdentityCallback<Text> {

    private val textRecViewModel by viewModels<TextRecViewModel> {
        TextRecViewModelFactory((requireContext().applicationContext as SampleApp).textRecognitionRepository as TextRecognitionRepository)
    }
    private var _binding: FragmentTextRecBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTextRecBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initVerifyIdentity()
    }


    private fun initVerifyIdentity() {

        textRecViewModel.checkPermission =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                var allPermissionGranted = false
                permissions.entries.forEach {
                    allPermissionGranted = it.value
                }
                if (allPermissionGranted) {
                    textRecViewModel.veriffIdentityManager?.onRequestPermissionsResult()
                }
            }

        textRecViewModel.veriffIdentityManager = activity?.let {
            textRecViewModel.checkPermission?.let { checkPermission ->
                VeriffIdentityManager(
                    VeriffApp.getInstance(),
                    it,
                    checkPermission,
                    binding.veriffScannerView,
                    textRecViewModel.visionType,
                    this
                )
            }
        }
    }

    private fun textRecFromBitmap(mSelectedImage: Bitmap?) {
        viewLifecycleOwner.lifecycleScope.launch {
            mSelectedImage?.let {
                textRecViewModel.runTextRecognition(mSelectedImage).observe(viewLifecycleOwner, Observer { results ->
                    textRecViewModel.textRecData.postValue(results)
                    if (results.text.isEmpty()) {
                        Log.i(TAG, "No Text :")
                    } else {
                        Log.i(TAG, "Text :" + results.text)
                    }
                })
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSuccess(results: Text) {
        if (isVisible) {
            if (results.text.isEmpty()) {
                binding.textMessage.text = getText(R.string.no_text)
            } else {
                binding.textMessage.text = results.text
            }
        }
    }

    override fun onFailure(e: Exception) {
        if (isVisible) {
            Toast.makeText(context, "Exception :" + e.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onTakePictureSuccess(bitmap: Bitmap?) {
        textRecFromBitmap(bitmap)
        var uri: Uri? = null
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
    companion object {
        private const val TAG = "veriff-sdk"
    }
}