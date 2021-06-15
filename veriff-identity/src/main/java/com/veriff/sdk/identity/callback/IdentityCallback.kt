package com.veriff.sdk.identity.callback

import android.graphics.Bitmap
import android.net.Uri

/**
 * Identity callback
 *
 * @param T
 * @constructor Create empty Identity callback
 */
interface IdentityCallback<T> {
    fun onSuccess(results: T)
    fun onFailure(e: Exception)
    fun onTakePictureSuccess(bitmap:Bitmap?)
}