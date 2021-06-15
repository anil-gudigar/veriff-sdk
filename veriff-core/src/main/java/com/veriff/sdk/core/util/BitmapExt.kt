package com.veriff.sdk.core.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.view.View
import java.io.FileOutputStream

/**
 * Scale image
 *
 * @param view
 * @param isHorizontalRotation
 * @return Bitmap
 */
fun Bitmap.scaleImage(view: View, isHorizontalRotation: Boolean): Bitmap? {
    val ratio = view.width.toFloat() / view.height.toFloat()
    val newHeight = (view.width * ratio).toInt()

    return when (isHorizontalRotation) {
        true -> Bitmap.createScaledBitmap(this, view.width, newHeight, false)
        false -> Bitmap.createScaledBitmap(this, view.width, view.height, false)
    }
}

/**
 * Get base y by view
 *
 * @param view
 * @param isHorizontalRotation
 * @return
 */
fun Bitmap.getBaseYByView(view: View, isHorizontalRotation: Boolean): Float {
    return when (isHorizontalRotation) {
        true -> (view.height.toFloat() / 2) - (this.height.toFloat() / 2)
        false -> 0f
    }
}

/**
 * Save to gallery
 *
 * @param context
 * @return Uri
 */
fun Bitmap.saveToGallery(context: Context) :Uri?{
    var pictureURI :Uri?
    makeTempFile().apply {
        FileOutputStream(this).run {
            this@saveToGallery.compress(Bitmap.CompressFormat.JPEG, 100, this)
            flush()
            close()
        }
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { it ->
            it.data = Uri.fromFile(this)
            pictureURI = it.data
            context.sendBroadcast(it)
        }
    }
    return pictureURI
}