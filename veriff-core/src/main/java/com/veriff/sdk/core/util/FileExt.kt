package com.veriff.sdk.core.util

import android.content.Context
import android.os.Environment
import java.io.File

fun getRootFolder(context: Context): File = File(
    context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
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
fun makeTempFile(context: Context): File =
    File.createTempFile("${System.currentTimeMillis()}", ".png", getRootFolder(context))