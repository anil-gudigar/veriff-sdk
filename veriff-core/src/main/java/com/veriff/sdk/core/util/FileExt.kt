package com.veriff.sdk.core.util

import android.os.Environment
import java.io.File

val rootFolder =
    File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
        "Veriff${File.separator}"
    ).apply {
        if (!exists())
            mkdirs()
    }

/**
 * Make temp file
 *
 * @return File
 */
fun makeTempFile(): File = File.createTempFile("${System.currentTimeMillis()}", ".png", rootFolder)